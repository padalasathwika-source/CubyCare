package co.csedge.cubycare.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject

data class UserAccount(
    val email: String,
    val displayName: String,
    val isCurrent: Boolean = false
)

fun getDerivedDisplayName(email: String?, displayName: String?, isAnonymous: Boolean? = false): String {
    if (isAnonymous == true || email == "guest@cubycare.app") return "Guest"
    if (!displayName.isNullOrBlank() && displayName != "Parent Account") return displayName
    if (!email.isNullOrBlank()) {
        val prefix = email.substringBefore("@")
        if (prefix.isNotBlank() && prefix != "guest") {
            return prefix
                .replace(".", " ")
                .replace("_", " ")
                .replace("-", " ")
                .split(" ")
                .filter { it.isNotBlank() }
                .joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
                }
        }
    }
    return "Guest"
}

class AccountManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cubycare_accounts_prefs", Context.MODE_PRIVATE)

    fun getSavedAccounts(): List<UserAccount> {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        val currentEmail = currentFirebaseUser?.email ?: if (currentFirebaseUser?.isAnonymous == true) "guest@cubycare.app" else "parent@cubycare.app"
        val currentName = getDerivedDisplayName(currentEmail, currentFirebaseUser?.displayName, currentFirebaseUser?.isAnonymous)

        val jsonStr = prefs.getString("saved_accounts_json", null)
        val list = mutableListOf<UserAccount>()

        if (!jsonStr.isNull_or_blank()) {
            try {
                val array = JSONArray(jsonStr)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val email = obj.optString("email")
                    val name = obj.optString("displayName")
                    if (email.isNotBlank()) {
                        list.add(UserAccount(email = email, displayName = name, isCurrent = (email == currentEmail)))
                    }
                }
            } catch (e: Exception) {
                // Ignore parse error
            }
        }

        // Ensure current logged in account is always present in list
        if (list.none { it.email == currentEmail }) {
            list.add(0, UserAccount(email = currentEmail, displayName = currentName, isCurrent = true))
            saveAccountsList(list)
        }

        return list.map { it.copy(isCurrent = (it.email == currentEmail)) }
    }

    fun addAccount(email: String, displayName: String) {
        val currentList = getSavedAccounts().toMutableList()
        if (currentList.none { it.email == email }) {
            currentList.add(UserAccount(email = email, displayName = displayName, isCurrent = true))
            saveAccountsList(currentList)
        }
    }

    fun switchAccount(targetEmail: String) {
        val currentList = getSavedAccounts().map {
            it.copy(isCurrent = (it.email == targetEmail))
        }
        saveAccountsList(currentList)
    }

    fun removeAccount(targetEmail: String) {
        val currentList = getSavedAccounts().filter { it.email != targetEmail }
        saveAccountsList(currentList)
    }

    private fun saveAccountsList(list: List<UserAccount>) {
        val array = JSONArray()
        list.forEach { acc ->
            val obj = JSONObject()
            obj.put("email", acc.email)
            obj.put("displayName", acc.displayName)
            array.put(obj)
        }
        prefs.edit().putString("saved_accounts_json", array.toString()).apply()
    }
}

private fun String?.isNull_or_blank(): Boolean {
    return this == null || this.trim().isEmpty()
}
