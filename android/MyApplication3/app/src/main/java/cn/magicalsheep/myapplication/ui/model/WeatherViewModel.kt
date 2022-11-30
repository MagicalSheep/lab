package cn.magicalsheep.myapplication.ui.model

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.magicalsheep.myapplication.data.WeatherDbHelper
import cn.magicalsheep.myapplication.data.entity.City
import cn.magicalsheep.myapplication.data.entity.CityEntry
import cn.magicalsheep.myapplication.data.entity.Weather
import cn.magicalsheep.myapplication.data.entity.WeatherEntry
import cn.magicalsheep.myapplication.data.repository.key
import cn.magicalsheep.myapplication.data.repository.weatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var data = ArrayList<Weather>()
    var selectedItem: Weather? = null

    fun getLocationId(locationName: String, db: WeatherDbHelper): String {
        val readDb = db.readableDatabase
        val cursor = readDb.query(
            CityEntry.TABLE_NAME,
            arrayOf(CityEntry.COLUMN_NAME_LOCATION_ID),
            "${CityEntry.COLUMN_NAME_LOCATION_NAME_ZH} = ?",
            arrayOf(locationName),
            null,
            null,
            null
        )
        if (cursor.count == 0) return "101250101" // Changsha
        cursor.moveToNext()
        val id = cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_LOCATION_ID))
        cursor.close()
        return id
    }

    fun getData(locationId: String, date: String, db: WeatherDbHelper): Weather? {
        val readDb = db.readableDatabase
        val cursor = readDb.query(
            WeatherEntry.TABLE_NAME,
            null,
            "${WeatherEntry.COLUMN_NAME_LOCATION_ID} = ? and ${WeatherEntry.COLUMN_NAME_FX_DATE} = ?",
            arrayOf(locationId, date),
            null,
            null,
            null
        )
        if (cursor.count == 0) return null
        cursor.moveToNext()
        val weather = Weather(cursor)
        cursor.close()
        return weather
    }

    fun getData(locationId: String, db: WeatherDbHelper): ArrayList<Weather> {
        val readDb = db.readableDatabase
        val cursor = readDb.query(
            WeatherEntry.TABLE_NAME,
            null,
            "${WeatherEntry.COLUMN_NAME_LOCATION_ID} = ?",
            arrayOf(locationId),
            null,
            null,
            null
        )
        val ret = ArrayList<Weather>()
        while (cursor.moveToNext()) {
            ret.add(Weather(cursor))
        }
        cursor.close()
        return ret
    }

    fun updateData(locationId: String, db: WeatherDbHelper, callback: () -> Unit) {
        viewModelScope.launch {
            val writeDb = db.writableDatabase
            writeDb.beginTransaction()
            try {
                val resp = weatherRepository.get7dWeather(locationId, key)
                if (resp.code != "200") {
                    return@launch
                }
                for (w in resp.daily) {
                    val values = ContentValues().apply {
                        put(WeatherEntry.COLUMN_NAME_LOCATION_ID, locationId)
                        put(WeatherEntry.COLUMN_NAME_FX_DATE, w.fxDate)
                        put(WeatherEntry.COLUMN_NAME_SUNRISE, w.sunrise)
                        put(WeatherEntry.COLUMN_NAME_SUNSET, w.sunset)
                        put(WeatherEntry.COLUMN_NAME_MOONRISE, w.moonrise)
                        put(WeatherEntry.COLUMN_NAME_MOONSET, w.moonset)
                        put(WeatherEntry.COLUMN_NAME_MOON_PHASE, w.moonPhase)
                        put(WeatherEntry.COLUMN_NAME_MOON_PHASE_ICON, w.moonPhaseIcon)
                        put(WeatherEntry.COLUMN_NAME_TEMP_MAX, w.tempMax)
                        put(WeatherEntry.COLUMN_NAME_TEMP_MIN, w.tempMin)
                        put(WeatherEntry.COLUMN_NAME_ICON_DAY, w.iconDay)
                        put(WeatherEntry.COLUMN_NAME_TEXT_DAY, w.textDay)
                        put(WeatherEntry.COLUMN_NAME_ICON_NIGHT, w.iconNight)
                        put(WeatherEntry.COLUMN_NAME_TEXT_NIGHT, w.textNight)
                        put(WeatherEntry.COLUMN_NAME_WIND_360_DAY, w.wind360Day)
                        put(WeatherEntry.COLUMN_NAME_WIND_DIR_DAY, w.windDirDay)
                        put(WeatherEntry.COLUMN_NAME_WIND_SCALE_DAY, w.windScaleDay)
                        put(WeatherEntry.COLUMN_NAME_WIND_SPEED_DAY, w.windSpeedDay)
                        put(WeatherEntry.COLUMN_NAME_WIND_360_NIGHT, w.wind360Night)
                        put(WeatherEntry.COLUMN_NAME_WIND_DIR_NIGHT, w.windDirNight)
                        put(WeatherEntry.COLUMN_NAME_WIND_SCALE_NIGHT, w.windScaleNight)
                        put(WeatherEntry.COLUMN_NAME_WIND_SPEED_NIGHT, w.windSpeedNight)
                        put(WeatherEntry.COLUMN_NAME_PRECIP, w.precip)
                        put(WeatherEntry.COLUMN_NAME_UV_INDEX, w.uvIndex)
                        put(WeatherEntry.COLUMN_NAME_HUMIDITY, w.humidity)
                        put(WeatherEntry.COLUMN_NAME_PRESSURE, w.pressure)
                        put(WeatherEntry.COLUMN_NAME_VIS, w.vis)
                        put(WeatherEntry.COLUMN_NAME_CLOUD, w.cloud)
                    }
                    writeDb?.insertWithOnConflict(
                        WeatherEntry.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE
                    )
                }
                writeDb.setTransactionSuccessful()
            } catch (_: Exception) {
                // ignored
            } finally {
                writeDb.endTransaction()
                callback()
            }
        }
    }
}