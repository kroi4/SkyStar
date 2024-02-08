package il.co.skystar.ui.screens

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import il.co.skystar.databinding.DeparturesFlightsLayoutBinding
import il.co.skystar.databinding.DetailsFlightLayoutBinding
import il.co.skystar.ui.RecordViewModel
import il.co.skystar.utils.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.Error

@AndroidEntryPoint
class DeparturesFlightsFragment : Fragment(R.layout.departures_flights_layout) {

    private var binding: DeparturesFlightsLayoutBinding by autoCleared()
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
        binding = DeparturesFlightsLayoutBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetch()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = binding.container
        fetch()

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            fetch()
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
                if (!viewModel.checkIfAlreadyFavorite(
                        favorite.CHOPER,
                        favorite.CHFLTN,
                        favorite.CHSTOL
                    )
                ) {
                    favorite._id = calculateIdForFavorite()
                    viewModel.addFavorite(favorite)
                    showToast(requireContext(),getString(R.string.add_new_to_favorite))
                } else {
                    showToast(requireContext(),getString(R.string.error_add_to_favorite))
                }
                (binding.rvFlights.adapter as RecordsAdapter).notifyItemChanged(viewHolder.absoluteAdapterPosition)
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
                            il.co.skystar.R.color.gold
                        )
                    )

                    .addSwipeRightActionIcon(il.co.skystar.R.drawable.ic_baseline_star_24)
                    .addSwipeRightLabel(getString(R.string.add_to_favorite))
                    .setSwipeRightLabelColor(
                        ContextCompat.getColor(
                            requireContext(),
                            il.co.skystar.R.color.white
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

    private fun setRecyclerViewer(it: Resource<List<Record>>): RecordsAdapter {
        return RecordsAdapter(it.status.data!!, object : RecordsAdapter.ItemListener {

            override fun onItemClicked(index: Int) {
                itemBinding = DetailsFlightLayoutBinding.inflate(layoutInflater)

                val builder = AlertDialog.Builder(requireContext())
                builder.setView(itemBinding.root)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.DKGRAY))

                itemBinding.apply {
                    if (language == "iw") {
                        tvCountryCity.text = it.status.data[index].CHLOC1TH  + ", " + it.status.data[index].CHLOC1CH
                        tvFlightStatus.text = it.status.data[index].CHRMINH
                        tvFlightNumber.gravity = Gravity.END
                        tvAirportName.gravity = Gravity.END
                        tvAirline.gravity = Gravity.END

                    } else {
                        tvCountryCity.text = it.status.data[index].CHLOC1T + ", " + it.status.data[index].CHLOCCT
                        tvFlightStatus.text = it.status.data[index].CHRMINE
                    }

                    setStatusColor(tvFlightStatus)

                    tvFlightNumber.text =
                        it.status.data[index].CHOPER + it.status.data[index].CHFLTN
                    tvAirline.text = it.status.data[index].CHOPERD
                    flagGlideCaster(it.status.data[index].CHLOCCT, ivCountry)
                    airlineGlideCaster(it.status.data[index].CHOPERD, ivAirline)


                    val year = it.status.data[index].CHSTOL.subSequence(0, 4)
                    val month = it.status.data[index].CHSTOL.subSequence(5, 7)
                    val day = it.status.data[index].CHSTOL.subSequence(8, 10)
                    val date = "${day}/${month}/${year}"
                    val time = it.status.data[index].CHSTOL.subSequence(11, 16)

                    tvTimeToFlight.text = "${getString(R.string.std)}: ${date} ${time}"

                    val realyear = it.status.data[index].CHPTOL.subSequence(0, 4)
                    val realmonth = it.status.data[index].CHPTOL.subSequence(5, 7)
                    val realday = it.status.data[index].CHPTOL.subSequence(8, 10)
                    val realDate = "${realday}/${realmonth}/${realyear}"
                    val realTime = it.status.data[index].CHPTOL.subSequence(11, 16)

                    tvRealTimeToFlight.text = "${getString(R.string.atd)}: ${realDate} ${realTime}"

                    tvAirportName.text = it.status.data[index].CHLOC1D

                    dialog.show()
                }
            }
            override fun onItemLongClicked(index: Int) {}
        })
    }

    private fun fetch() {
        viewModel.getDepartures()
        viewModel.departures.observe(viewLifecycleOwner) {
            when (it.status) {
                is Loading -> {
                    binding.rvFlights.isVisible = false
                    binding.progressBar.isVisible = true
                }
                is Success -> {
                    if (!it.status.data.isNullOrEmpty()) {
                        binding.progressBar.isVisible = false
                        binding.rvFlights.isVisible = true
                        binding.tvStatus.isVisible = false
                        binding.rvFlights.adapter = setRecyclerViewer(it)
                        binding.rvFlights.layoutManager = GridLayoutManager(requireContext(), 1)
                    }
                }
                else -> { // Error
                    binding.progressBar.isVisible = false
                    binding.rvFlights.isVisible = false
                    showToast(requireContext(),getString(R.string.couldnt_connect_to_server))
                    binding.tvStatus.text = getString(R.string.couldnt_connect_to_server)
                    binding.tvStatus.isVisible = true
                }
            }

        }
    }

    private fun calculateIdForFavorite(): Int {
        val position = viewModel.getCurrentNumberOfFavorites()

        return if (position > 0) {
            viewModel.getFavorites()[position - 1]._id + 1
        } else {
            0
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
