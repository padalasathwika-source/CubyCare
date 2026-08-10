package co.csedge.cubycare.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import co.csedge.cubycare.data.model.Medicine
import java.text.SimpleDateFormat
import java.util.*

object MedicineAlarmScheduler {
    fun scheduleAlarm(context: Context, medicine: Medicine) {
        if (medicine.time.isBlank()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MedicineReminderReceiver::class.java).apply {
            putExtra("MEDICINE_NAME", medicine.name)
            putExtra("MEDICINE_DOSE", medicine.dose)
        }
        
        // Use a unique ID for the PendingIntent based on medicine ID hash
        val requestCode = medicine.id.hashCode()
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Parse "hh:mm a"
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        try {
            val date = format.parse(medicine.time)
            if (date != null) {
                val calendar = Calendar.getInstance()
                val parsedCalendar = Calendar.getInstance().apply { time = date }
                
                calendar.set(Calendar.HOUR_OF_DAY, parsedCalendar.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, parsedCalendar.get(Calendar.MINUTE))
                calendar.set(Calendar.SECOND, 0)

                // If the time has already passed today, schedule for tomorrow
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1)
                }

                // Need exact alarms
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    } else {
                        // Fallback if permission not granted
                        alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
