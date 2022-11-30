package cn.magicalsheep.myapplication

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import cn.magicalsheep.myapplication.data.PhotoDbHelper
import cn.magicalsheep.myapplication.data.model.Photo
import cn.magicalsheep.myapplication.data.model.PhotoEntry
import cn.magicalsheep.myapplication.data.model.Photos
import cn.magicalsheep.myapplication.data.photoService
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainHandler(
    private val ref: WeakReference<MainActivity>,
    private val executor: ExecutorService = Executors.newFixedThreadPool(20)
) :
    Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {

        val activity: MainActivity = ref.get() ?: return

        when (msg.what) {
            MainActivity.StatusCode.OK -> {
                val photos: List<Photo> = (msg.obj as Photos).photos
                Thread {
                    for (photo in photos) {
                        executor.execute {
                            loadImage(photo, activity.dbHelper)
                        }
                    }
                }.start()
            }
            MainActivity.StatusCode.IMG_OK -> {
                val bitmap = msg.obj as Bitmap
                activity.adapter.add(bitmap)
            }
            MainActivity.StatusCode.FAILED -> {
                Toast.makeText(activity, activity.getString(R.string.fail_note), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun fetchRemoteImage(url: String, dbHelper: PhotoDbHelper) {
        try {
            val bytes = photoService.downloadPhoto(url).execute().body()?.bytes() ?: return

            sendMessage(
                Message.obtain(
                    null,
                    MainActivity.StatusCode.IMG_OK,
                    BitmapFactory.decodeStream(bytes.inputStream())
                )
            )

            val writeDb = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(PhotoEntry.COLUMN_NAME_URL, url)
                put(PhotoEntry.COLUMN_NAME_IMAGE, bytes)
            }
            writeDb?.insert(PhotoEntry.TABLE_NAME, null, values)
        } catch (ex: Exception) {
            return
        }
    }

    private fun loadImage(photo: Photo, dbHelper: PhotoDbHelper) {
        val readDb = dbHelper.readableDatabase
        val cursor = readDb.query(
            PhotoEntry.TABLE_NAME,
            arrayOf(PhotoEntry.COLUMN_NAME_IMAGE),
            "${PhotoEntry.COLUMN_NAME_URL} = ?",
            arrayOf(photo.imgSrc),
            null,
            null,
            null
        )
        if (cursor.count != 0) {
            Log.i(TAG, "Fetch image from database: ${photo.imgSrc}")
            val bytes: ByteArray
            with(cursor) {
                cursor.moveToNext()
                bytes = getBlob(0)
            }
            sendMessage(
                Message.obtain(
                    null,
                    MainActivity.StatusCode.IMG_OK,
                    BitmapFactory.decodeStream(bytes.inputStream())
                )
            )
        } else {
            Log.i(TAG, "Fetch image from url: ${photo.imgSrc}")
            fetchRemoteImage(photo.imgSrc, dbHelper)
        }
        cursor.close()
    }
}