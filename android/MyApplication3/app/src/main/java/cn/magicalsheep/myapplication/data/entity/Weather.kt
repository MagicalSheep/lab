package cn.magicalsheep.myapplication.data.entity

import android.database.Cursor

data class Weather(
    val fxDate: String,
    val sunrise: String?,
    val sunset: String?,
    val moonrise: String?,
    val moonset: String?,
    val moonPhase: String,
    val moonPhaseIcon: String,
    val tempMax: String,
    val tempMin: String,
    val iconDay: String,
    val textDay: String,
    val iconNight: String,
    val textNight: String,
    val wind360Day: String,
    val windDirDay: String,
    val windScaleDay: String,
    val windSpeedDay: String,
    val wind360Night: String,
    val windDirNight: String,
    val windScaleNight: String,
    val windSpeedNight: String,
    val precip: String,
    val uvIndex: String,
    val humidity: String,
    val pressure: String,
    val vis: String,
    val cloud: String
) {
    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_FX_DATE)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_SUNRISE)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_SUNSET)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_MOONRISE)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_MOONSET)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_MOON_PHASE)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_MOON_PHASE_ICON)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_TEMP_MAX)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_TEMP_MIN)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_ICON_DAY)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_TEXT_DAY)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_ICON_NIGHT)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_TEXT_NIGHT)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_360_DAY)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_DIR_DAY)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_SCALE_DAY)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_SPEED_DAY)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_360_NIGHT)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_DIR_NIGHT)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_SCALE_NIGHT)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_SPEED_NIGHT)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_PRECIP)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_UV_INDEX)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_HUMIDITY)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_PRESSURE)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_VIS)),
        cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_CLOUD))
    )

    override fun toString(): String {
        return "Date: ${fxDate}, " +
                "Temperature Max: ${tempMax}°C, " +
                "Temperature Min: ${tempMin}°C, " +
                "Weather: ${textDay}, " +
                "Humidity: $humidity %, " +
                "Pressure: $pressure hPa, " +
                "Wind: $windSpeedDay km/h SE"
    }
}
