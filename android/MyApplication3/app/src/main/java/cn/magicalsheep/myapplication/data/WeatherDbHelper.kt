package cn.magicalsheep.myapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import cn.magicalsheep.myapplication.data.entity.CityEntry
import cn.magicalsheep.myapplication.data.entity.WeatherEntry

private const val SQL_CREATE_CITY_ENTRIES =
    "CREATE TABLE ${CityEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${CityEntry.COLUMN_NAME_LOCATION_ID} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_LOCATION_NAME_EN} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_LOCATION_NAME_ZH} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_COUNTRY_CODE} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_COUNTRY_NAME_EN} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_COUNTRY_NAME_ZH} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_ADM1_NAME_EN} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_ADM1_NAME_ZH} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_ADM2_NAME_EN} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_ADM2_NAME_ZH} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_TIMEZONE} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_LATITUDE} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_LONGITUDE} TEXT NOT NULL," +
            "${CityEntry.COLUMN_NAME_ADCODE} TEXT NOT NULL)"

private const val SQL_CREATE_WEATHER_ENTRIES =
    "CREATE TABLE ${WeatherEntry.TABLE_NAME} (" +
            "${WeatherEntry.COLUMN_NAME_LOCATION_ID} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_FX_DATE} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_SUNRISE} TEXT," +
            "${WeatherEntry.COLUMN_NAME_SUNSET} TEXT," +
            "${WeatherEntry.COLUMN_NAME_MOONRISE} TEXT," +
            "${WeatherEntry.COLUMN_NAME_MOONSET} TEXT," +
            "${WeatherEntry.COLUMN_NAME_MOON_PHASE} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_MOON_PHASE_ICON} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_TEMP_MAX} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_TEMP_MIN} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_ICON_DAY} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_TEXT_DAY} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_ICON_NIGHT} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_TEXT_NIGHT} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_360_DAY} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_DIR_DAY} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_SCALE_DAY} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_SPEED_DAY} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_360_NIGHT} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_DIR_NIGHT} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_SCALE_NIGHT} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_WIND_SPEED_NIGHT} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_PRECIP} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_UV_INDEX} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_HUMIDITY} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_PRESSURE} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_VIS} TEXT NOT NULL," +
            "${WeatherEntry.COLUMN_NAME_CLOUD} TEXT NOT NULL)"

private const val SQL_CREATE_CITY_INDEX =
    "CREATE UNIQUE INDEX city_index ON ${CityEntry.TABLE_NAME} (${CityEntry.COLUMN_NAME_LOCATION_ID})"

private const val SQL_CREATE_WEATHER_INDEX =
    "CREATE UNIQUE INDEX weather_index ON ${WeatherEntry.TABLE_NAME} (${WeatherEntry.COLUMN_NAME_LOCATION_ID}, ${WeatherEntry.COLUMN_NAME_FX_DATE})"

private const val SQL_DELETE_CITY_ENTRIES = "DROP TABLE IF EXISTS ${CityEntry.TABLE_NAME}"

private const val SQL_DELETE_WEATHER_ENTRIES = "DROP TABLE IF EXISTS ${WeatherEntry.TABLE_NAME}"

class WeatherDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CITY_ENTRIES)
        db.execSQL(SQL_CREATE_CITY_INDEX)
        db.execSQL(SQL_CREATE_WEATHER_ENTRIES)
        db.execSQL(SQL_CREATE_WEATHER_INDEX)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_CITY_ENTRIES)
        db.execSQL(SQL_DELETE_WEATHER_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Weather.db"
    }
}