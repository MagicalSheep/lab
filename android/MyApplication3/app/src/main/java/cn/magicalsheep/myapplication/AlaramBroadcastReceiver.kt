package cn.magicalsheep.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cn.magicalsheep.myapplication.data.entity.Weather
import cn.magicalsheep.myapplication.data.repository.key
import cn.magicalsheep.myapplication.data.repository.weatherRepository

const val CHANNEL_ID = "Notification"
const val INTENT_ACTION = "CLOCK_IN"
const val INTERVAL_MILLIS = 30000L

class AlarmBroadcastReceiver : BroadcastReceiver() {

    private lateinit var locationId: String

    @Throws(Exception::class)
    private fun getTodayWeather(): Weather {
        val resp =
            weatherRepository.get3dWeather(locationId, key).execute().body() ?: throw Exception()
        return resp.daily[0]
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(context: Context, msg: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.cloud_white_24dp)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(msg)
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        locationId = intent.getStringExtra("locationId") ?: "101250101" // Changsha

        createNotificationChannel(context)

        if (intent.action == INTENT_ACTION) {
            Thread {
                try {
                    sendNotification(context, getTodayWeather().toString())
                } catch (_: Exception) {
                    // ignored
                }
            }.start()
        }
    }
}