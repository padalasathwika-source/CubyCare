package co.csedge.cubycare.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class BookedAppointment(
    val id: String = java.util.UUID.randomUUID().toString(),
    val childId: String,
    val doctorName: String,
    val hospitalName: String,
    val appointmentDate: String, // e.g. "2026-08-15"
    val appointmentTime: String, // e.g. "10:30 AM"
    val notes: String = "",
    val isMallaReddyHospital: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

class AppointmentRepository(context: Context) {
    private val prefs = context.getSharedPreferences("cubycare_appointments_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getAppointmentsForChild(childId: String): List<BookedAppointment> {
        val json = prefs.getString("appointments_$childId", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<BookedAppointment>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveAppointment(appointment: BookedAppointment) {
        val currentList = getAppointmentsForChild(appointment.childId).toMutableList()
        currentList.removeAll { it.id == appointment.id }
        currentList.add(0, appointment)
        val json = gson.toJson(currentList)
        prefs.edit().putString("appointments_${appointment.childId}", json).apply()
    }

    fun deleteAppointment(childId: String, appointmentId: String) {
        val currentList = getAppointmentsForChild(childId).toMutableList()
        currentList.removeAll { it.id == appointmentId }
        val json = gson.toJson(currentList)
        prefs.edit().putString("appointments_$childId", json).apply()
    }
}
