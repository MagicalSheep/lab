package cn.magicalsheep.myapplication.data.entity

import android.provider.BaseColumns

object CityEntry : BaseColumns {
    const val TABLE_NAME = "city"
    const val COLUMN_NAME_LOCATION_ID = "location_id"
    const val COLUMN_NAME_LOCATION_NAME_EN = "location_name_en"
    const val COLUMN_NAME_LOCATION_NAME_ZH = "location_name_zh"
    const val COLUMN_NAME_COUNTRY_CODE = "country_code"
    const val COLUMN_NAME_COUNTRY_NAME_EN = "country_came_en"
    const val COLUMN_NAME_COUNTRY_NAME_ZH = "country_name_zh"
    const val COLUMN_NAME_ADM1_NAME_EN = "adm1_name_en"
    const val COLUMN_NAME_ADM1_NAME_ZH = "adm1_name_zh"
    const val COLUMN_NAME_ADM2_NAME_EN = "adm2_name_en"
    const val COLUMN_NAME_ADM2_NAME_ZH = "adm2_name_zh"
    const val COLUMN_NAME_TIMEZONE = "timezone"
    const val COLUMN_NAME_LATITUDE = "latitude"
    const val COLUMN_NAME_LONGITUDE = "longitude"
    const val COLUMN_NAME_ADCODE = "adcode"
}
