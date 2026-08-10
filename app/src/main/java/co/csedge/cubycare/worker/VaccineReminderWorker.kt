package co.csedge.cubycare.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.csedge.cubycare.R
import co.csedge.cubycare.data.repository.ChildRepository

class VaccineReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val repository = ChildRepository(context)
            val children = repository.getChildren()
            val now = System.currentTimeMillis()
            val sevenDaysInMillis = 7L * 24 * 60 * 60 * 1000

            var notificationsSent = 0

            for (child in children) {
                for (vaccine in child.vaccines) {
                    if (vaccine.administeredDateMillis == null && vaccine.nextDueDateMillis != null) {
                        val diff = vaccine.nextDueDateMillis - now

                        // Overdue by more than a day but less than 14 days (don't spam old ones)
                        if (diff < 0 && diff > -14L * 24 * 60 * 60 * 1000) {
                            sendNotification(
                                id = vaccine.id.hashCode(),
                                title = "Vaccine Overdue!",
                                message = "${child.name} is overdue for the ${vaccine.name} vaccine."
                            )
                            notificationsSent++
                        } 
                        // Due within 7 days
                        else if (diff > 0 && diff <= sevenDaysInMillis) {
                            sendNotification(
                                id = vaccine.id.hashCode(),
                                title = "Upcoming Vaccine Reminder",
                                message = "${child.name} is due for ${vaccine.name} soon."
                            )
                            notificationsSent++
                        }
                    }
                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun sendNotification(id: Int, title: String, message: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "vaccine_reminders")
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        manager.notify(id, notification)
    }
}
