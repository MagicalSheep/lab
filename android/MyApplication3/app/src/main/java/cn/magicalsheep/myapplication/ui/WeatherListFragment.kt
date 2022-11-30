package cn.magicalsheep.myapplication.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import cn.magicalsheep.myapplication.R
import cn.magicalsheep.myapplication.data.WeatherDbHelper
import cn.magicalsheep.myapplication.ui.adapter.WeatherListAdapter
import cn.magicalsheep.myapplication.ui.model.WeatherViewModel
import cn.magicalsheep.myapplication.databinding.FragmentItemListBinding

class WeatherListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private lateinit var viewModel: WeatherViewModel
    private lateinit var dbHelper: WeatherDbHelper
    private lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(activity!!)[WeatherViewModel::class.java]
        dbHelper = WeatherDbHelper(activity!!)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        recyclerView.adapter = WeatherListAdapter(
            activity!!,
            dbHelper,
            viewModel,
            getString(R.string.default_location),
            sharedPreferences,
            itemDetailFragmentContainer
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}