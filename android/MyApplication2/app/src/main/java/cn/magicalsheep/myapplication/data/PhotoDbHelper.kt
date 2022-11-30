package cn.magicalsheep.myapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import cn.magicalsheep.myapplication.data.model.PhotoEntry

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${PhotoEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${PhotoEntry.COLUMN_NAME_URL} TEXT," +
            "${PhotoEntry.COLUMN_NAME_IMAGE} BLOB)"

private const val SQL_CREATE_INDEX =
    "CREATE INDEX url_index ON ${PhotoEntry.TABLE_NAME} (${PhotoEntry.COLUMN_NAME_URL})"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PhotoEntry.TABLE_NAME}"

class PhotoDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_INDEX)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Photo.db"
    }
}