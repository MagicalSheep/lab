package cn.magicalsheep.myapplication.ui

import android.content.SharedPreferences
import cn.magicalsheep.myapplication.ui.model.WeatherViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import cn.magicalsheep.myapplication.R
import cn.magicalsheep.myapplication.data.WeatherDbHelper
import cn.magicalsheep.myapplication.data.entity.Weather
import cn.magicalsheep.myapplication.databinding.FragmentItemDetailBinding

class WeatherDetailFragment : Fragment() {

    lateinit var dateTextView: TextView
    lateinit var tempMaxTextView: TextView
    lateinit var tempMinTextView: TextView
    lateinit var weatherTextView: TextView
    lateinit var humidityTextView: TextView
    lateinit var pressureTextView: TextView
    lateinit var windTextView: TextView
    lateinit var iconImageView: ImageView

    private lateinit var viewModel: WeatherViewModel
    private lateinit var dbHelper: WeatherDbHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toolbar: Toolbar
    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(activity!!)[WeatherViewModel::class.java]
        viewModel.selectedItem = null // reload
        dbHelper = WeatherDbHelper(activity!!)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        toolbar = activity!!.findViewById(R.id.toolbar)

        arguments?.let {
            if (it.containsKey(ARG_LOCATION_ID) && it.containsKey(ARG_FX_DATE)) {
                viewModel.selectedItem = viewModel.getData(
                    it.getString(ARG_LOCATION_ID)!!,
                    it.getString(ARG_FX_DATE)!!,
                    dbHelper
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        dateTextView = binding.detailDate
        tempMaxTextView = binding.detailTempMax
        tempMinTextView = binding.detailTempMin
        weatherTextView = binding.detailWeather
        humidityTextView = binding.detailHumidity
        pressureTextView = binding.detailPressure
        windTextView = binding.detailWind
        iconImageView = binding.detailIcon

        updateContent()

        return rootView
    }

    private fun updateContent() {
        viewModel.selectedItem?.let {
            dateTextView.text = it.fxDate

            if (sharedPreferences.getString("temp_units", "celsius") == "celsius") {
                tempMaxTextView.text = "${it.tempMax}°C"
                tempMinTextView.text = "${it.tempMin}°C"
            } else {
                val tempMax = it.tempMax.toDouble() * 1.8 + 32
                val tempMin = it.tempMin.toDouble() * 1.8 + 32
                tempMaxTextView.text = String.format("%.1f°F", tempMax)
                tempMinTextView.text = String.format("%.1f°F", tempMin)
            }

            weatherTextView.text = it.textDay
            humidityTextView.text = "Humidity: ${it.humidity} %"
            pressureTextView.text = "Pressure: ${it.pressure} hPa"
            windTextView.text = "Wind: ${it.windSpeedDay} km/h SE"
            val id =
                activity!!.resources.getIdentifier(
                    "icon${it.iconDay}_fill",
                    "drawable",
                    activity!!.packageName
                )
            val drawable = ContextCompat.getDrawable(activity!!, id)
            iconImageView.setImageDrawable(drawable)
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.selectedItem != null) {
            toolbar.menu.findItem(R.id.share).isVisible = true
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.selectedItem != null) {
            toolbar.menu.findItem(R.id.share).isVisible = false
        }
    }

    companion object {
        const val ARG_LOCATION_ID = "location_id"
        const val ARG_FX_DATE = "fx_date"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}