package co.csedge.cubycare.data.repository

import android.content.Context
import android.util.Log
import co.csedge.cubycare.data.model.Child
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await

class ChildRepository(private val context: Context? = null) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val gson = Gson()
    
    private val usersCollection = db.collection("users")
    private val prefs = context?.getSharedPreferences("cubycare_local_data", Context.MODE_PRIVATE)
    private val sessionPrefs = context?.getSharedPreferences("cubycare_session", Context.MODE_PRIVATE)

    init {
        // Remove legacy un-scoped key to prevent cross-account pollution from older app builds
        if (prefs?.contains("local_children") == true) {
            prefs.edit().remove("local_children").apply()
        }
    }

    private fun getUserKey(): String {
        val uid = auth.currentUser?.uid
        if (!uid.isNullOrBlank()) return uid
        val isGuest = sessionPrefs?.getBoolean("is_guest_logged_in", false) == true
        return if (isGuest) "guest_user_local_id" else "default_user_local_id"
    }

    private fun getLocalChildrenKey(): String {
        return "local_children_${getUserKey()}"
    }

    private fun getLocalChildren(): List<Child> {
        if (prefs == null) return emptyList()
        val key = getLocalChildrenKey()
        val json = prefs.getString(key, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<Child>>() {}.type
            gson.fromJson<List<Child>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveLocalChildren(children: List<Child>) {
        if (prefs == null) return
        val key = getLocalChildrenKey()
        val json = gson.toJson(children)
        prefs.edit().putString(key, json).apply()
    }

    /**
     * Retrieves children specifically mapped to the authenticated parent account or guest session.
     */
    suspend fun getChildren(): List<Child> {
        val local = getLocalChildren()
        val currentUser = auth.currentUser

        var childrenList = if (currentUser == null) {
            local
        } else {
            try {
                val snapshot = usersCollection
                    .document(currentUser.uid)
                    .collection("children")
                    .get()
                    .await()
                
                val remote = snapshot.toObjects(Child::class.java)
                if (remote.isNotEmpty()) {
                    saveLocalChildren(remote)
                    remote
                } else if (local.isNotEmpty()) {
                    // If remote Firestore is empty, sync local account children up to Firestore for this user
                    for (child in local) {
                        try {
                            usersCollection
                                .document(currentUser.uid)
                                .collection("children")
                                .document(child.id)
                                .set(child)
                                .await()
                        } catch (e: Exception) {
                            Log.e("ChildRepository", "Failed to sync local child to Firestore", e)
                        }
                    }
                    local
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("ChildRepository", "Error fetching children, returning local account copy", e)
                local
            }
        }

        // Ensure Default General Baby Profile exists with all age information across all dashboards
        val hasGeneralBaby = childrenList.any { it.id == "default_general_baby" || it.name.contains("General Baby") }
        if (!hasGeneralBaby) {
            val defaultBaby = GeneralBabyProfileProvider.createDefaultGeneralBaby()
            val updated = mutableListOf(defaultBaby)
            updated.addAll(childrenList)
            childrenList = updated
            saveLocalChildren(childrenList)
        }

        return childrenList
    }

    /**
     * Adds a new child or updates an existing child under the authenticated user's profile or guest session.
     */
    suspend fun saveChild(child: Child): Boolean {
        val currentLocal = getLocalChildren().filter { it.id != child.id }.toMutableList()
        val childToSave = if (child.id.isEmpty()) child.copy(id = System.currentTimeMillis().toString()) else child
        currentLocal.add(childToSave)
        saveLocalChildren(currentLocal)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                usersCollection
                    .document(currentUser.uid)
                    .collection("children")
                    .document(childToSave.id)
                    .set(childToSave)
                    .await()
            } catch (e: Exception) {
                Log.e("ChildRepository", "Error saving child to Firestore (saved locally for user)", e)
            }
        }
        return true
    }

    /**
     * Delete a child profile from the authenticated user's account or guest session.
     */
    suspend fun deleteChild(childId: String): Boolean {
        if (childId == "default_general_baby") {
            Log.w("ChildRepository", "Deletion of default general baby profile is blocked.")
            return false
        }
        val currentLocal = getLocalChildren().filter { it.id != childId }
        saveLocalChildren(currentLocal)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                usersCollection
                    .document(currentUser.uid)
                    .collection("children")
                    .document(childId)
                    .delete()
                    .await()
            } catch (e: Exception) {
                Log.e("ChildRepository", "Error deleting child from Firestore (deleted locally for user)", e)
            }
        }
        return true
    }
}
