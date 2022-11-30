package cn.magicalsheep.myapplication

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.magicalsheep.myapplication.data.PhotoDbHelper
import cn.magicalsheep.myapplication.data.photoService
import java.lang.ref.WeakReference

const val TAG = "NASA Curiosity"
const val apiKey = "1Zh5bWjw3sxhht8aE7hVyx70aqpAesxFnDbIpOPb"

class MainActivity : AppCompatActivity() {

    val dbHelper = PhotoDbHelper(this)
    val adapter = RecyclerViewAdapter()

    object StatusCode {
        const val OK = 200
        const val FAILED = 500
        const val IMG_OK = 201
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val photosView: RecyclerView = findViewById(R.id.photos_view)
        photosView.layoutManager = GridLayoutManager(this, 3)
        photosView.adapter = adapter
        photosView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        photosView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val handler = MainHandler(WeakReference(this))

        Thread {
            val resp = photoService.allPhotos(sol = 1000, apiKey).execute().body()
            if (resp == null) {
                handler.sendMessage(Message.obtain(null, StatusCode.FAILED))
            } else {
                handler.sendMessage(Message.obtain(null, StatusCode.OK, resp))
            }
        }.start();
    }
}