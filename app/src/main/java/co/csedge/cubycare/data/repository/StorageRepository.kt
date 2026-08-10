package co.csedge.cubycare.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageRepository {
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storageRef = storage.reference

    suspend fun uploadVaccineCertificate(childId: String, vaccineId: String, fileUri: Uri): String? {
        val userId = auth.currentUser?.uid ?: return null
        return try {
            val certRef = storageRef.child("users/$userId/children/$childId/vaccines/$vaccineId/certificate.jpg")
            certRef.putFile(fileUri).await()
            val downloadUrl = certRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            Log.e("StorageRepository", "Error uploading certificate", e)
            null
        }
    }
}
