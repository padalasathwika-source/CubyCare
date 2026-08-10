package co.csedge.cubycare.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object VaccineScanner {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    // Looks for common date formats like dd/MM/yyyy, dd-MM-yy, etc.
    private val datePattern = Regex("""\b(\d{1,2})[-/.](\d{1,2})[-/.](\d{2,4})\b""")

    /**
     * Scans the bitmap for text, attempts to find dates, and returns the most likely date found.
     */
    suspend fun scanForAdministeredDate(bitmap: Bitmap): Long? {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val result = recognizer.process(image).await()
            val text = result.text

            // Find all date matches
            val matches = datePattern.findAll(text).toList()
            
            if (matches.isEmpty()) return null

            val formatters = listOf(
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()),
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()),
                SimpleDateFormat("dd/MM/yy", Locale.getDefault()),
                SimpleDateFormat("dd-MM-yy", Locale.getDefault())
            )

            var bestDate: Date? = null

            for (match in matches) {
                val dateStr = match.value
                for (formatter in formatters) {
                    try {
                        val parsed = formatter.parse(dateStr)
                        if (parsed != null && parsed.time < System.currentTimeMillis()) {
                            // If we found multiple dates, we'll just take the most recent one that is not in the future
                            if (bestDate == null || parsed.time > bestDate.time) {
                                bestDate = parsed
                            }
                        }
                    } catch (e: Exception) {
                        // ignore parse exception
                    }
                }
            }

            bestDate?.time
        } catch (e: Exception) {
            Log.e("VaccineScanner", "Error scanning text", e)
            null
        }
    }
}
