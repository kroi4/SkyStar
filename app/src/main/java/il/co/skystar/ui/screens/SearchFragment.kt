package il.co.skystar.ui.screens

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.*
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import il.co.skystar.R
import il.co.skystar.databinding.SearchLayoutBinding
import il.co.skystar.ui.RecordViewModel
import il.co.skystar.utils.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_layout) {

    private var binding: SearchLayoutBinding by autoCleared()
    private val viewModel: RecordViewModel by viewModels()
    private val currentLocale = Locale.getDefault()
    private val language = currentLocale.language

    private var startDateTime: String = ""
    private var endDateTime: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchLayoutBinding.inflate(layoutInflater)

        binding.btnSendSearch.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_searchResultsFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeZone = TimeZone.getTimeZone("Asia/Jerusalem")


        val dateValidatorMin: DateValidator =
            DateValidatorPointForward.from(
                Calendar.getInstance(timeZone).timeInMillis - 2.days.toLong(DurationUnit.MILLISECONDS)
            )

        val dateValidatorMax: DateValidator =
            DateValidatorPointBackward.before(
                Calendar.getInstance(timeZone).timeInMillis + 4.days.toLong(DurationUnit.MILLISECONDS)
            )

        val dateValidator: DateValidator =
            CompositeDateValidator.allOf(listOf(dateValidatorMin, dateValidatorMax))


// Makes only dates from today forward selectable.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(dateValidator)

        binding.apply {
            if (tilDates.text.toString() == "") {
                tilStartTime.isEnabled = false
                tilEndTime.isEnabled = false
            } else {
                tilStartTime.isEnabled = true
                tilEndTime.isEnabled = true
            }
            tilDates.setOnClickListener {

                val builder = MaterialDatePicker.Builder
                    .dateRangePicker()
                    .setTitleText(getString(R.string.select_dates))
                    .setCalendarConstraints(constraintsBuilder.build())
                val picker = builder.build()

                picker.show(childFragmentManager, "DatePicker")

                picker.addOnPositiveButtonClickListener {

                    val startDate = Date(it.first!!)
                    val endDate = Date(it.second!!)
                    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedStartDate = format.format(startDate)
                    val formattedEndDate = format.format(endDate)
                    tilDates.setText("$formattedStartDate - $formattedEndDate")
                    if (tilDates.text.toString() != "") {
                        tilStartTime.isEnabled = true
                        tilEndTime.isEnabled = true
                    }
                }
            }

            tilFlightNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!Regex("[A-Z0-9]*").matches(s.toString())) {
                        tilFlightNumber.setError(getString(R.string.uppercase_digits_regex))
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            tilDates.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (tilDates.text.toString() != "") {
                        if (tilDates.text.toString().subSequence(0, 2)
                                .toString() != tilDates.text.toString().subSequence(13, 15)
                                .toString()
                        ) {
                            tilStartTime.setError(null)
                            tilEndTime.setError(null)
                        } else {
                            if (!isEmpty(tilStartTime) && !isEmpty(tilEndTime)) {
                                val start = SimpleDateFormat(
                                    "HH:mm",
                                    Locale.getDefault()
                                ).parse(tilStartTime.text.toString())

                                val finish = SimpleDateFormat(
                                    "HH:mm",
                                    Locale.getDefault()
                                ).parse(tilEndTime.text.toString())

                                if (start.compareTo(finish) >= 0) {
                                    tilStartTime.setError(getString(R.string.time_regex))
                                    tilEndTime.setError(getString(R.string.time_regex))
                                }
                            }
                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            tilStartTime.setOnClickListener {

                val builder = MaterialTimePicker.Builder()
                val picker = builder.build()

                picker.show(childFragmentManager, "TimePicker")

                picker.addOnPositiveButtonClickListener {
                    val hourOfDay = picker.hour
                    val minute = picker.minute
                    val formattedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                    tilStartTime.setText(formattedTime)
                }

            }

            tilStartTime.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (tilDates.text.toString() != "") {
                        if (tilDates.text.toString().subSequence(0, 2)
                                .toString() == tilDates.text.toString().subSequence(13, 15)
                                .toString()
                        ) {
                            val start = SimpleDateFormat(
                                "HH:mm",
                                Locale.getDefault()
                            ).parse(tilStartTime.text.toString())
                            if (tilEndTime.text.toString() != "") {
                                val finish = SimpleDateFormat(
                                    "HH:mm",
                                    Locale.getDefault()
                                ).parse(tilEndTime.text.toString())
                                if (start.compareTo(finish) >= 0) {
                                    tilStartTime.setError(getString(R.string.time_regex))
                                } else {
                                    tilStartTime.setError(null)
                                    tilEndTime.setError(null)
                                }
                            } else {
                                tilStartTime.setError(null)
                                tilEndTime.setError(null)
                            }
                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            tilEndTime.setOnClickListener {
                val builder = MaterialTimePicker.Builder()
                val picker = builder.build()

                picker.show(childFragmentManager, "TimePicker")

                picker.addOnPositiveButtonClickListener {
                    val hourOfDay = picker.hour
                    val minute = picker.minute
                    val formattedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                    tilEndTime.setText(formattedTime)
                }
            }

            tilEndTime.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (tilDates.text.toString() != "") {
                        if (tilDates.text.toString().subSequence(0, 2)
                                .toString() == tilDates.text.toString().subSequence(13, 15)
                                .toString()
                        ) {
                            val finish = SimpleDateFormat(
                                "HH:mm",
                                Locale.getDefault()
                            ).parse(tilEndTime.text.toString())
                            if (tilStartTime.text.toString() != "") {
                                val start = SimpleDateFormat(
                                    "HH:mm",
                                    Locale.getDefault()
                                ).parse(tilStartTime.text.toString())
                                if (start.compareTo(finish) >= 0) {
                                    tilEndTime.setError(getString(R.string.time_regex))
                                } else {
                                    tilStartTime.setError(null)
                                    tilEndTime.setError(null)
                                }
                            } else {
                                tilStartTime.setError(null)
                                tilEndTime.setError(null)
                            }
                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            val airlines = resources.getStringArray(il.co.skystar.R.array.airlines)
            val arrayAdapterAirliens = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                airlines
            )
            tilAirline.setAdapter(arrayAdapterAirliens)


            if (language == "iw") {
                val cityes = resources.getStringArray(il.co.skystar.R.array.cityesHe)
                val arrayAdapterCityes = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    cityes
                )
                tilCityFlight.setAdapter(arrayAdapterCityes)
            } else {
                val cityes = resources.getStringArray(il.co.skystar.R.array.cityesEn)
                val arrayAdapterCityes = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    cityes
                )
                tilCityFlight.setAdapter(arrayAdapterCityes)
            }

            btnSendSearch.setOnClickListener {
                if (tilAirline.error != null || tilFlightNumber.error != null || tilStartTime.error != null || tilEndTime.error != null) {
                    showAlertDialog(
                        requireContext(),
                        getString(R.string.attention),
                        getString(R.string.error_regex),
                        getString(R.string.ok)
                    )
                } else {
                    if (isEmpty(tilAirline) && isEmpty(tilFlightNumber) && isEmpty(tilDates) && isEmpty(
                            tilStartTime // When everything is empty
                        ) && isEmpty(tilEndTime) && isEmpty(tilCityFlight)
                    ) {
                        if (switchDeparturesArrivals.isChecked) { // Checking the switch
                            findNavController().navigate(R.id.action_searchFragment_to_arrivalsFlightsFragment)
                        } else {
                            findNavController().navigate(R.id.action_searchFragment_to_departuresFlightsFragment)
                        }
                    } else { // If at least one field has a value
                        if (!isEmpty(tilCityFlight)) {
                            tilCityFlight.setText(
                                tilCityFlight.text.toString()
                                    .uppercase(Locale.getDefault())
                            )
                        }
                        if (isEmpty(tilStartTime) && !isEmpty(tilEndTime) || (!isEmpty(tilStartTime) && isEmpty(
                                tilEndTime // When the user filled only start or end time (not both)
                            ))
                        ) {
                            showAlertDialog(
                                requireContext(),
                                getString(R.string.attention),
                                getString(R.string.fill_time_fields),
                                getString(R.string.ok)
                            )
                        } else { // When the user filled both time fields or none

                            if (isEmpty(tilDates)) { // If the user hasn't chosen a date range
                                startDateTime = ""
                                endDateTime = ""
                            } else { // When the user has chosen a date range
                                if (isEmpty(tilStartTime) && isEmpty(tilEndTime)) {
                                    tilStartTime.setText("00:00")
                                    tilEndTime.setText("23:59")
                                }
                                val startYear =
                                    tilDates.text.toString().subSequence(6, 10).toString()
                                val endYear =
                                    tilDates.text.toString().subSequence(19, 23).toString()
                                val startMonth =
                                    tilDates.text.toString().subSequence(3, 5).toString()
                                val endMonth =
                                    tilDates.text.toString().subSequence(16, 18).toString()
                                val startDay = tilDates.text.toString().subSequence(0, 2).toString()
                                val endDay = tilDates.text.toString().subSequence(13, 15).toString()
                                startDateTime =
                                    "${startYear}-${startMonth}-${startDay}T${tilStartTime.text.toString()}:00"
                                endDateTime =
                                    "${endYear}-${endMonth}-${endDay}T${tilEndTime.text.toString()}:00"
                            }

                            viewModel.getAllFlights()
                            viewModel.allFlights.observe(viewLifecycleOwner) {
                                when (it.status) {
                                    is Loading -> {}
                                    is Success -> {
                                        if (!it.status.data.isNullOrEmpty()) {
                                            val switch = if (switchDeparturesArrivals.isChecked) {
                                                "A"
                                            } else {
                                                "D"
                                            }
                                            val bundle = bundleOf(
                                                "flightType" to switch,
                                                "airline" to tilAirline.text.toString(),
                                                "flightNumber" to tilFlightNumber.text.toString(),
                                                "startDateTime" to startDateTime,
                                                "endDateTime" to endDateTime,
                                                "flightCity" to tilCityFlight.text.toString()
                                            )
                                            try {
                                                findNavController().navigate(
                                                    R.id.action_searchFragment_to_searchResultsFragment,
                                                    bundle
                                                )
                                            } catch (e: Exception) {
                                                Log.e(TAG, "Exception")
                                            }
                                        }
                                    }
                                    else -> {
                                        if (it.status.data.isNullOrEmpty()) {
                                            showAlertDialog(
                                                requireContext(),
                                                getString(R.string.attention),
                                                getString(R.string.search_failed),
                                                getString(R.string.ok)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (binding.tilDates.text.toString() == "") {
            binding.tilStartTime.isEnabled = false
            binding.tilEndTime.isEnabled = false
        } else {
            binding.tilStartTime.isEnabled = true
            binding.tilEndTime.isEnabled = true
        }
    }
}