package co.csedge.cubycare.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object HospitalUtils {
    const val MALLA_REDDY_PEDIATRICS_NEONATOLOGY_URL = "https://www.mallareddynarayana.com/speciality/pediatrics-and-neonatolgy"

    fun openPediatricsSpecialty(context: Context, url: String = MALLA_REDDY_PEDIATRICS_NEONATOLOGY_URL) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening Malla Reddy Narayana Hospital Pediatrics...", Toast.LENGTH_SHORT).show()
        }
    }

    fun openNearbyHospitals(context: Context) {
        try {
            val gmmIntentUri = Uri.parse("geo:0,0?q=children+hospitals+and+pediatricians+near+me")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                val searchUri = Uri.parse("https://www.google.com/maps/search/children+hospitals+and+pediatricians+near+me")
                val webIntent = Intent(Intent.ACTION_VIEW, searchUri)
                context.startActivity(webIntent)
            }
        } catch (e: Exception) {
            val fallbackUri = Uri.parse("https://www.google.com/search?q=children+hospitals+and+pediatricians+near+me")
            val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
            context.startActivity(fallbackIntent)
        }
    }

    fun openNearbyVaccinationCenters(context: Context) {
        try {
            val gmmIntentUri = Uri.parse("geo:0,0?q=vaccination+centers+and+pediatric+immunization+clinics+near+me")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                val searchUri = Uri.parse("https://www.google.com/maps/search/vaccination+centers+and+pediatric+immunization+clinics+near+me")
                val webIntent = Intent(Intent.ACTION_VIEW, searchUri)
                context.startActivity(webIntent)
            }
        } catch (e: Exception) {
            val fallbackUri = Uri.parse("https://www.google.com/search?q=vaccination+centers+near+me")
            val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
            context.startActivity(fallbackIntent)
        }
    }
}
