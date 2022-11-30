package cn.magicalsheep.myapplication.data.model

import android.provider.BaseColumns

object PhotoEntry : BaseColumns {
    const val TABLE_NAME = "photo_cache"
    const val COLUMN_NAME_URL = "url"
    const val COLUMN_NAME_IMAGE = "image"
}