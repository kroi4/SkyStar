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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import il.co.skystar.databinding.SearchResultsLayoutBinding
import il.co.skystar.ui.RecordViewModel
import il.co.skystar.utils.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.search_results_layout) {

    private var binding: SearchResultsLayoutBinding by autoCleared()
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
        binding = SearchResultsLayoutBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = binding.container

        val flightType = arguments?.getString("flightType")!!
        val airline = arguments?.getString("airline")!!
        val flightNumber = arguments?.getString("flightNumber")!!
        val startDateTime = arguments?.getString("startDateTime")!!
        val endDateTime = arguments?.getString("endDateTime")!!
        val flightCity = arguments?.getString("flightCity")!!

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            val results = if (startDateTime == "" || endDateTime == "") {
                viewModel.searchFlightWithoutDate(flightNumber, flightType, airline, flightCity)
            } else {
                viewModel.searchFlightWithDate(
                    flightNumber,
                    startDateTime,
                    endDateTime,
                    flightType,
                    airline,
                    flightCity
                )
            }
            if (results.isEmpty()) {
                findNavController().navigate(R.id.action_searchResultsFragment_to_searchFragment)
                showAlertDialog(
                    requireContext(),
                    getString(R.string.attention),
                    getString(R.string.no_search_results),
                    getString(R.string.ok)
                )
            } else {
                binding.rvFlights.adapter = setRecyclerViewer(results)
                binding.rvFlights.layoutManager = GridLayoutManager(requireContext(), 1)
            }
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
                    showToast(requireContext(), getString(R.string.add_new_to_favorite))
                } else {
                    showToast(requireContext(), getString(R.string.error_add_to_favorite))
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
                            R.color.gold
                        )
                    )

                    .addSwipeRightActionIcon(R.drawable.ic_baseline_star_24)
                    .addSwipeRightLabel(getString(R.string.add_to_favorite))
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

        val results = if (startDateTime == "" || endDateTime == "") {
            viewModel.searchFlightWithoutDate(flightNumber, flightType, airline, flightCity)
        } else {
            viewModel.searchFlightWithDate(
                flightNumber,
                startDateTime,
                endDateTime,
                flightType,
                airline,
                flightCity
            )
        }

        if (results.isEmpty()) {
            showAlertDialog(
                requireContext(),
                getString(R.string.attention),
                getString(R.string.no_search_results),
                getString(R.string.ok)
            )
            findNavController().navigate(R.id.action_searchResultsFragment_to_searchFragment)
        } else {
            binding.rvFlights.adapter = setRecyclerViewer(results)
            binding.rvFlights.layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    private fun setRecyclerViewer(it: List<Record>): RecordsAdapter {
        return RecordsAdapter(it, object : RecordsAdapter.ItemListener {

            override fun onItemClicked(index: Int) {
                itemBinding = DetailsFlightLayoutBinding.inflate(layoutInflater)

                val builder = AlertDialog.Builder(requireContext())
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

                    tvFlightNumber.text = it[index].CHOPER + it[index].CHFLTN
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