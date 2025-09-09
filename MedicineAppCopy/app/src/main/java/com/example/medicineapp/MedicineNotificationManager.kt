package com.example.medicineapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.util.*

object MedicineNotificationManager {

    private const val CHANNEL_ID = "medicine_reminders"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Medicine Reminders"
            val descriptionText = "Notifications for taking medicines"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun checkAndNotify(context: Context) {
  //      Log.d("MedicineReminder", "Checkinggg")
        // Check permission first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val medicinesToNotify = MedicineRepository.medicines.filter { medicine ->
            when {
                TimeOfDay.MORNING in medicine.whenToTake && hour == 8 -> true
                TimeOfDay.NOON in medicine.whenToTake && hour == 12 -> true
                TimeOfDay.EVENING in medicine.whenToTake && hour == 20 -> true
                else -> false
            }
        }

        medicinesToNotify.forEachIndexed { index, medicine ->
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Time to take your medicine")
                .setContentText("${medicine.name} (${medicine.altName}) - ${medicine.howManyToTake} pill(s)")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            with(NotificationManagerCompat.from(context)) {
                notify(index, builder.build())
            }
        }
    }
}
