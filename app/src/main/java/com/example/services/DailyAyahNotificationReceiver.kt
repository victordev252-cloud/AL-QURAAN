package com.example.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.local.QuranPrepopulatedData
import java.util.Calendar

class DailyAyahNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "daily_ayah_channel"
        const val NOTIFICATION_ID = 2002

        fun scheduleDailyNotification(context: Context, hour: Int = 7, minute: Int = 0) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, DailyAyahNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                100,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                }
            } catch (e: SecurityException) {
                // If exact alarms not permitted, fallback to inexact
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        createNotificationChannel(context)

        val pool = QuranPrepopulatedData.dailyAyahsPool
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val dailyAyah = pool[(dayOfYear % pool.size).coerceIn(0, pool.size - 1)]

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_surah", dailyAyah.surahNumber)
            putExtra("navigate_ayah", dailyAyah.ayahNumber)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.quran_logo)
            .setContentTitle("آية اليوم — Daily Ayah (Surah ${dailyAyah.surahNumber}:${dailyAyah.ayahNumber})")
            .setContentText(dailyAyah.arabicText)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${dailyAyah.arabicText}\n\n${dailyAyah.englishTranslation}\n\n${dailyAyah.somaliTranslation}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)

        // Reschedule for next day
        scheduleDailyNotification(context)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Ayah Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily inspirational verses from the Holy Quran"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
