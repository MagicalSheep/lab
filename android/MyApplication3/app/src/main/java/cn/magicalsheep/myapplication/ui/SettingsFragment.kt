package cn.magicalsheep.myapplication.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import cn.magicalsheep.myapplication.AlarmBroadcastReceiver
import cn.magicalsheep.myapplication.INTENT_ACTION
import cn.magicalsheep.myapplication.INTERVAL_MILLIS
import cn.magicalsheep.myapplication.R
import cn.magicalsheep.myapplication.data.WeatherDbHelper
import cn.magicalsheep.myapplication.ui.model.WeatherViewModel

class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {

    private lateinit var toolbar: Toolbar
    private lateinit var viewModel: WeatherViewModel
    private lateinit var dbHelper: WeatherDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = activity!!.findViewById(R.id.toolbar)
        viewModel = ViewModelProvider(activity!!)[WeatherViewModel::class.java]
        dbHelper = WeatherDbHelper(activity!!)
    }

    override fun onResume() {
        super.onResume()
        toolbar.menu.findItem(R.id.menu_map_location).isVisible = false
        toolbar.menu.findItem(R.id.menu_settings).isVisible = false
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        toolbar.menu.findItem(R.id.menu_map_location).isVisible = true
        toolbar.menu.findItem(R.id.menu_settings).isVisible = true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        when (key) {
            "is_notice" -> {
                val defaultLocation = activity!!.getString(R.string.default_location)
                val locationName =
                    sharedPreferences.getString("location", defaultLocation) ?: defaultLocation
                val locationId = viewModel.getLocationId(locationName, dbHelper)
                val alarmManager =
                    activity!!.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                val noteIntent = Intent(activity, AlarmBroadcastReceiver::class.java)
                noteIntent.action = INTENT_ACTION
                noteIntent.putExtra("locationId", locationId)
                if (sharedPreferences.getBoolean("is_notice", false)) {
                    val pendingIntent =
                        PendingIntent.getBroadcast(
                            activity, 0, noteIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                    alarmManager?.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + INTERVAL_MILLIS,
                        INTERVAL_MILLIS,
                        pendingIntent
                    )
                } else {
                    val pendingIntent =
                        PendingIntent.getBroadcast(
                            activity, 0, noteIntent,
                            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                        )
                    if (pendingIntent != null && alarmManager != null) {
                        alarmManager.cancel(pendingIntent)
                    }
                }
            }
            "location" -> {
                viewModel.data.clear()
            }
        }


    }
}