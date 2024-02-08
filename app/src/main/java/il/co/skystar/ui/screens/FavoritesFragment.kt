package il.co.skystar.ui.screens

import android.content.ContentValues.TAG
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import il.co.skystar.R
import il.co.skystar.api.RecordsAdapter
import il.co.skystar.data.model.Record
import il.co.skystar.databinding.DetailsFlightLayoutBinding
import il.co.skystar.databinding.FavoriteLayoutBinding
import il.co.skystar.ui.RecordViewModel
import il.co.skystar.utils.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.favorite_layout) {

    private var binding: FavoriteLayoutBinding by autoCleared()
    private var itemBinding: DetailsFlightLayoutBinding by autoCleared()
    private val viewModel: RecordViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val currentLocale = Locale.getDefault()
    private val language = currentLocale.language

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteLayoutBinding.inflate(layoutInflater)

        checkUpdatesForFavorites()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        swipeRefreshLayout = binding.container

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            checkUpdatesForFavorites()
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(
                ItemTouchHelper.ACTION_STATE_SWIPE,
                ItemTouchHelper.RIGHT
            )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val favorite =
                    (binding.rvFlights.adapter as RecordsAdapter).itemAt(viewHolder.absoluteAdapterPosition)
                viewModel.removeFavorite(favorite)
                showToast(requireContext(), getString(R.string.has_been_remove_favorite))
                binding.rvFlights.adapter!!.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
                refreshRecyclerView()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightLabel(getString(R.string.remove_favorite))
                    .setSwipeRightLabelColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.rvFlights)

    }

    override fun onResume() {
        super.onResume()
        checkUpdatesForFavorites()
    }


    private fun checkUpdatesForFavorites() {

        val favorites = viewModel.getFavorites()
        viewModel.getAllFlights()
        viewModel.allFlights.observe(viewLifecycleOwner) {

            if (it.status is Success) {
                if (!it.status.data.isNullOrEmpty()){
                    for (item in favorites) {
                        if (viewModel.checkIfFlightStillExists(item.CHOPER, item.CHFLTN, item.CHSTOL)) {
                            val newItem =
                                viewModel.getSpecificRecord(item.CHOPER, item.CHFLTN, item.CHSTOL)
                            if (newItem.CHOPER != null && newItem.CHSTOL != null){
                                viewModel.updateRecord(
                                    item.CHOPER,
                                    item.CHFLTN,
                                    item.CHSTOL,
                                    newItem.CHPTOL,
                                    newItem.CHRMINE,
                                    newItem.CHRMINH
                                )
                            }

                        }
                        else {
                            val year = (item.CHSTOL.subSequence(0, 4)).toString().toInt()
                            val month = (item.CHSTOL.subSequence(5, 7)).toString().toInt()
                            val day = (item.CHSTOL.subSequence(8, 10)).toString().toInt()
                            val date = LocalDate.of(year, month, day)
                            if (date < ZonedDateTime.now(ZoneId.of("Asia/Jerusalem")).toLocalDate()
                                    .minusDays(1)
                            ) {
                                viewModel.removeFavorite(item)
                            }
                        }
                    }
                }
                if (viewModel.getCurrentNumberOfFavorites() == 0) {
                    Log.e(TAG, "Returned home")
                    binding.tvStatus.isVisible = true
                    binding.tvStatus.text = getString(R.string.empty_favorites)
                }
                else {
                    binding.rvFlights.adapter = setRecyclerViewer(favorites)
                    binding.rvFlights.layoutManager = GridLayoutManager(requireContext(), 1)
                }
            }
        }
    }

    private fun setRecyclerViewer(it: List<Record>): RecordsAdapter {
        return RecordsAdapter(it, object : RecordsAdapter.ItemListener {

            override fun onItemClicked(index: Int) {
                itemBinding = DetailsFlightLayoutBinding.inflate(layoutInflater)

                val builder = android.app.AlertDialog.Builder(requireContext())
                builder.setView(itemBinding.root)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.DKGRAY))

                itemBinding.apply {

                    if (language == "iw") {
                        tvCountryCity.text = it[index].CHLOC1TH + ", " + it[index].CHLOC1CH
                        tvFlightStatus.text = it[index].CHRMINH
                        tvFlightNumber.gravity = Gravity.END
                        tvAirportName.gravity = Gravity.END
                        tvAirline.gravity = Gravity.END

                    } else {
                        tvCountryCity.text = it[index].CHLOC1T + ", " + it[index].CHLOCCT
                        tvFlightStatus.text = it[index].CHRMINE
                    }

                    setStatusColor(tvFlightStatus)

                    tvFlightNumber.text = "${it[index].CHOPER}${it[index].CHFLTN}"
                    tvAirline.text = it[index].CHOPERD
                    flagGlideCaster(it[index].CHLOCCT, ivCountry)
                    airlineGlideCaster(it[index].CHOPERD, ivAirline)

                    val year = it[index].CHSTOL.subSequence(0, 4)
                    val month = it[index].CHSTOL.subSequence(5, 7)
                    val day = it[index].CHSTOL.subSequence(8, 10)
                    val date = "${day}/${month}/${year}"
                    val time = it[index].CHSTOL.subSequence(11, 16)

                    if (it[index].CHAORD == "D") {
                        tvTimeToFlight.text = "${getString(R.string.std)}: ${date} ${time}"
                    } else if (it[index].CHAORD == "A") {
                        tvTimeToFlight.text = "${getString(R.string.sta)}: ${date} ${time}"
                    }

                    val realyear = it[index].CHPTOL.subSequence(0, 4)
                    val realmonth = it[index].CHPTOL.subSequence(5, 7)
                    val realday = it[index].CHPTOL.subSequence(8, 10)
                    val realDate = "${realday}/${realmonth}/${realyear}"
                    val realTime = it[index].CHPTOL.subSequence(11, 16)

                    if (it[index].CHAORD == "D") {
                        tvRealTimeToFlight.text =
                            "${getString(R.string.atd)}: ${realDate} ${realTime}"
                        ivFlightType.setImageResource(R.drawable.departure_airplane)
                    } else if (it[index].CHAORD == "A") {
                        tvRealTimeToFlight.text =
                            "${getString(R.string.ata)}: ${realDate} ${realTime}"
                        ivFlightType.setImageResource(R.drawable.arrivals_airplane)
                    }

                    tvAirportName.text = it[index].CHLOC1D
                    dialog.show()
                }
            }

            override fun onItemLongClicked(index: Int) {}
        })
    }

    private fun refreshRecyclerView() {
        if (viewModel.getCurrentNumberOfFavorites() == 0) {
            binding.tvStatus.isVisible = true
            binding.tvStatus.text = getString(R.string.empty_favorites)
        } else {
            val favorites = viewModel.getFavorites()
            binding.rvFlights.adapter = setRecyclerViewer(favorites)
            binding.rvFlights.layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    private fun flagGlideCaster(country: String, flag: ImageView) {
        Glide.with(this)
            .load(Constants.flagDict.get(country))
            .override(200, 200)
            .into(flag)
    }

    private fun airlineGlideCaster(airline: String, icon: ImageView) {
        Glide.with(this)
            .load(Constants.airlineDict.get(airline))
            .placeholder(R.drawable.generalairlien)
            .override(200, 200)
            .into(icon)
    }
}