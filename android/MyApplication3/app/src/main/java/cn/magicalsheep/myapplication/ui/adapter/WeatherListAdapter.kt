package cn.magicalsheep.myapplication.ui.adapter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cn.magicalsheep.myapplication.data.entity.Weather
import cn.magicalsheep.myapplication.databinding.ItemListContentBinding
import cn.magicalsheep.myapplication.ui.WeatherDetailFragment
import cn.magicalsheep.myapplication.ui.model.WeatherViewModel
import cn.magicalsheep.myapplication.R
import cn.magicalsheep.myapplication.data.WeatherDbHelper

class WeatherListAdapter(
    private val context: Context,
    private val dbHelper: WeatherDbHelper,
    private val viewModel: WeatherViewModel,
    private val defaultLocation: String,
    private val sharedPreferences: SharedPreferences,
    private val itemDetailFragmentContainer: View?
) :
    RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {

    init {
        if (viewModel.data.isEmpty()) {
            val location =
                sharedPreferences.getString("location", defaultLocation) ?: defaultLocation
            val locationId = viewModel.getLocationId(location, dbHelper)

            viewModel.updateData(locationId, dbHelper) {
                viewModel.data = viewModel.getData(locationId, dbHelper)
                notifyItemInserted(itemCount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.data[position]
        val id =
            context.resources.getIdentifier(
                "icon${item.iconDay}_fill",
                "drawable",
                context.packageName
            )
        val drawable = ContextCompat.getDrawable(context, id)
        holder.icon.setImageDrawable(drawable)
        holder.date.text = item.fxDate
        holder.weather.text = item.textDay

        if (sharedPreferences.getString("temp_units", "celsius") == "celsius") {
            holder.tempMax.text = "${item.tempMax}°C"
            holder.tempMin.text = "${item.tempMin}°C"
        } else {
            val tempMax = item.tempMax.toDouble() * 1.8 + 32
            val tempMin = item.tempMin.toDouble() * 1.8 + 32
            holder.tempMax.text = String.format("%.1f°F", tempMax)
            holder.tempMin.text = String.format("%.1f°F", tempMin)
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener { itemView ->
                val it = itemView.tag as Weather
                val bundle = Bundle()
                val locationName =
                    sharedPreferences.getString("location", defaultLocation) ?: defaultLocation
                bundle.putString(
                    WeatherDetailFragment.ARG_LOCATION_ID,
                    viewModel.getLocationId(locationName, dbHelper)
                )
                bundle.putString(WeatherDetailFragment.ARG_FX_DATE, it.fxDate)
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
            }
        }
    }

    override fun getItemCount() = viewModel.data.size

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val icon: ImageView = binding.icon
        val date: TextView = binding.date
        val weather: TextView = binding.weather
        val tempMax: TextView = binding.tempMax
        val tempMin: TextView = binding.tempMin
    }

}