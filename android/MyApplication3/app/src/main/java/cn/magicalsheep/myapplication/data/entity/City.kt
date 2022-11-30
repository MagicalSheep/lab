package cn.magicalsheep.myapplication.data.entity

import android.database.Cursor
import java.io.Serializable

data class City(
    val Location_ID: String,
    val Location_Name_EN: String,
    val Location_Name_ZH: String,
    val Country_Code: String,
    val Country_Name_EN: String,
    val Country_Name_ZH: String,
    val Adm1_Name_EN: String,
    val Adm1_Name_ZH: String,
    val Adm2_Name_EN: String,
    val Adm2_Name_ZH: String,
    val Timezone: String,
    val Latitude: String,
    val Longitude: String,
    val Adcode: String
) : Serializable {

    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_LOCATION_ID)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_LOCATION_NAME_EN)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_LOCATION_NAME_ZH)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_COUNTRY_CODE)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_COUNTRY_NAME_EN)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_COUNTRY_NAME_ZH)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_ADM1_NAME_EN)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_ADM1_NAME_ZH)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_ADM2_NAME_EN)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_ADM2_NAME_ZH)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_TIMEZONE)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_LATITUDE)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_LONGITUDE)),
        cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME_ADCODE))
    )

}
