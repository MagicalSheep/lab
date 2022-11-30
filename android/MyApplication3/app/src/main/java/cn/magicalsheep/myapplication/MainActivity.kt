package cn.magicalsheep.myapplication

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import cn.magicalsheep.myapplication.data.WeatherDbHelper
import cn.magicalsheep.myapplication.data.entity.City
import cn.magicalsheep.myapplication.data.entity.CityEntry
import cn.magicalsheep.myapplication.databinding.ActivityItemDetailBinding
import cn.magicalsheep.myapplication.ui.model.WeatherViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var defaultLocation: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: WeatherViewModel
    private val dbHelper = WeatherDbHelper(this)

    private fun preLocationData() {
        val cursor = dbHelper.readableDatabase.rawQuery(
            "select ${CityEntry.COLUMN_NAME_LOCATION_ID} from ${CityEntry.TABLE_NAME}",
            null
        )
        if (cursor.count == 0) {
            val cityJson = resources.openRawResource(R.raw.city)
                .bufferedReader().use { it.readText() }
            val listType = object : TypeToken<ArrayList<City?>?>() {}.type
            val cities: List<City> = Gson().fromJson(cityJson, listType)
            val writeDb = dbHelper.writableDatabase
            writeDb.beginTransaction()
            try {
                for (c in cities) {
                    val values = ContentValues().apply {
                        put(CityEntry.COLUMN_NAME_LOCATION_ID, c.Location_ID)
                        put(CityEntry.COLUMN_NAME_LOCATION_NAME_EN, c.Location_Name_EN)
                        put(CityEntry.COLUMN_NAME_LOCATION_NAME_ZH, c.Location_Name_ZH)
                        put(CityEntry.COLUMN_NAME_COUNTRY_CODE, c.Country_Code)
                        put(CityEntry.COLUMN_NAME_COUNTRY_NAME_EN, c.Country_Name_EN)
                        put(CityEntry.COLUMN_NAME_COUNTRY_NAME_ZH, c.Country_Name_ZH)
                        put(CityEntry.COLUMN_NAME_ADM1_NAME_EN, c.Adm1_Name_EN)
                        put(CityEntry.COLUMN_NAME_ADM1_NAME_ZH, c.Adm1_Name_ZH)
                        put(CityEntry.COLUMN_NAME_ADM2_NAME_EN, c.Adm2_Name_EN)
                        put(CityEntry.COLUMN_NAME_ADM2_NAME_ZH, c.Adm2_Name_ZH)
                        put(CityEntry.COLUMN_NAME_TIMEZONE, c.Timezone)
                        put(CityEntry.COLUMN_NAME_LATITUDE, c.Latitude)
                        put(CityEntry.COLUMN_NAME_LONGITUDE, c.Longitude)
                        put(CityEntry.COLUMN_NAME_ADCODE, c.Adcode)
                    }
                    writeDb?.insert(
                        CityEntry.TABLE_NAME,
                        null,
                        values,
                    )
                }
                writeDb.setTransactionSuccessful()
            } catch (_: Exception) {
                // ignored
            } finally {
                writeDb.endTransaction()
            }
        }
        cursor.close()
    }

    private fun getCity(locationName: String): City {
        val readDb = dbHelper.readableDatabase
        var cursor = readDb.query(
            CityEntry.TABLE_NAME,
            null,
            "${CityEntry.COLUMN_NAME_LOCATION_NAME_ZH} = ?",
            arrayOf(locationName),
            null,
            null,
            null
        )
        if (cursor.count == 0) {
            cursor = readDb.query(
                CityEntry.TABLE_NAME,
                null,
                "${CityEntry.COLUMN_NAME_LOCATION_NAME_ZH} = ?",
                arrayOf(defaultLocation),
                null,
                null,
                null
            )
        }
        cursor.moveToNext()
        val city = City(cursor)
        cursor.close()
        return city
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preLocationData()

        defaultLocation = getString(R.string.default_location)
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.toolbar_menu)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.share -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, viewModel.selectedItem!!.toString())
                    intent.type = "text/plain"
                    startActivity(intent)
                }
                R.id.menu_map_location -> {
                    val locationName =
                        sharedPreferences.getString("location", defaultLocation) ?: defaultLocation
                    val city = getCity(locationName)
                    val uri =
                        Uri.parse("geo:${city.Latitude},${city.Longitude}?q=${city.Latitude},${city.Longitude}(${city.Location_Name_ZH})")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.menu_settings -> {
                    navController.navigate(R.id.settings_fragment)
                }
            }
            return@setOnMenuItemClickListener true
        }
    }
}