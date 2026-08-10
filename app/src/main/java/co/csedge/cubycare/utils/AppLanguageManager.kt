package co.csedge.cubycare.utils

import android.content.Context
import androidx.compose.runtime.*
import java.util.Locale

data class AppLanguage(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String
)

val SupportedLanguages = listOf(
    AppLanguage("en", "English", "English", "🇬🇧"),
    AppLanguage("hi", "Hindi", "हिंदी", "🇮🇳"),
    AppLanguage("te", "Telugu", "తెలుగు", "🇮🇳"),
    AppLanguage("ta", "Tamil", "தமிழ்", "🇮🇳"),
    AppLanguage("kn", "Kannada", "ಕನ್ನಡ", "🇮🇳"),
    AppLanguage("ml", "Malayalam", "മലയാളം", "🇮🇳"),
    AppLanguage("mr", "Marathi", "मराठी", "🇮🇳"),
    AppLanguage("bn", "Bengali", "বাংলা", "🇮🇳"),
    AppLanguage("gu", "Gujarati", "ગુજરાતી", "🇮🇳"),
    AppLanguage("pa", "Punjabi", "ਪੰਜਾਬੀ", "🇮🇳")
)

val LocalAppLanguage = staticCompositionLocalOf { "en" }

@Composable
fun tr(key: String): String {
    val lang = LocalAppLanguage.current
    return AppLanguageManager.getString(key, lang)
}

object AppLanguageManager {
    private const val PREFS_NAME = "cubycare_theme_prefs"
    private const val KEY_LANGUAGE = "app_language"

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
        setLocale(context, languageCode)
    }

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    // Comprehensive String Translation Dictionary for Major Indian Languages
    fun getString(key: String, languageCode: String): String {
        val translations = mapOf(
            "app_name" to mapOf(
                "en" to "CubyCare", "hi" to "क्युबीकेयर", "te" to "క్యూబీకేర్", "ta" to "கியூபிகேர்",
                "kn" to "ಕ್ಯೂಬಿಕೇರ್", "ml" to "ക്യൂബികെയർ", "mr" to "क्युबीकेअर", "bn" to "কিউবিক্যার", "gu" to "ક્યુબીકેર", "pa" to "ਕਿਊਬੀਕੇਅਰ"
            ),
            "dashboard_title" to mapOf(
                "en" to "CubyCare Dashboard", "hi" to "क्युबीकेयर डैशबोर्ड", "te" to "క్యూబీకేర్ డాష్‌బోర్డ్", "ta" to "கியூபிகேர் டாஷ்போர்டு",
                "kn" to "ಕ್ಯೂಬಿಕೇರ್ ಡ್ಯಾಶ್‌ಬೋರ್ಡ್", "ml" to "ക്യൂബികെയർ ഡാഷ്‌ബോർഡ്", "mr" to "क्युबीकेअर डॅशबोर्ड", "bn" to "কিউবিক্যার ড্যাশবোর্ড", "gu" to "ક્યુબીકેર ડેશબોર્ડ", "pa" to "ਕਿਊਬੀਕੇਅਰ ਡੈਸ਼ਬੋਰਡ"
            ),
            "app_settings" to mapOf(
                "en" to "App Settings & Preferences", "hi" to "ऐप सेटिंग्स और प्राथमिकताएं", "te" to "యాప్ సెట్టింగ్‌లు మరియు ప్రాధాన్యతలు", "ta" to "செயலி அமைப்புகள்",
                "kn" to "ಆ್ಯಪ್ ಸೆಟ್ಟಿಂಗ್‌ಗಳು", "ml" to "ആപ്പ് ക്രമീകരണങ്ങൾ", "mr" to "ॲप सेटिंग्ज", "bn" to "অ্যাপ সেটিংস", "gu" to "એપ સેટિંગ્સ", "pa" to "ਐਪ ਸੈਟਿੰਗਾਂ"
            ),
            "app_language" to mapOf(
                "en" to "App Language / भाषा / భాష", "hi" to "ऐप की भाषा (App Language)", "te" to "యాప్ భాష (App Language)", "ta" to "செயலி மொழி (App Language)",
                "kn" to "ಆ್ಯಪ್ ಭಾಷೆ (App Language)", "ml" to "ആപ്പ് ഭാഷ (App Language)", "mr" to "ॲपची भाषा (App Language)", "bn" to "অ্যাপের ভাষা (App Language)", "gu" to "એપની ભાષા (App Language)", "pa" to "ਐਪ ਦੀ ਭਾਸ਼ਾ (App Language)"
            ),
            "select_language_subtitle" to mapOf(
                "en" to "Select your preferred Indian language for complete app translation:",
                "hi" to "संपूर्ण ऐप अनुवाद के लिए अपनी पसंदीदा भारतीय भाषा चुनें:",
                "te" to "పూర్తి యాప్ అనువాదం కోసం మీకు నచ్చిన భారతీయ భాషను ఎంచుకోండి:",
                "ta" to "முழு செயலி மொழிபெயர்ப்பிற்கு உங்கள் விருப்பமான இந்திய மொழியைத் தேர்ந்தெடுக்கவும்:",
                "kn" to "ಸಂಪೂರ್ಣ ಆ್ಯಪ್ ಅನುವಾದಕ್ಕಾಗಿ ನಿಮ್ಮ ಪಸಂದಿನ ಭಾರತೀಯ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ:",
                "ml" to "പൂർണ്ണ ആപ്പ് പരിഭാഷയ്ക്കായി നിങ്ങളുടെ മുൻഗണനാ ഇന്ത്യൻ ഭാഷ തിരഞ്ഞെടുക്കുക:",
                "mr" to "संपूर्ण ॲप भाषांतरासाठी तुमची पसंतीची भारतीय भाषा निवडा:",
                "bn" to "সম্পূর্ণ অ্যাপ অনুবাদের জন্য আপনার পছন্দের ভারতীয় ভাষা নির্বাচন করুন:",
                "gu" to "સંપૂર્ણ એપ અનુવાદ માટે તમારી પસંદગીની ભારતીય ભાષા પસંદ કરો:",
                "pa" to "ਪੂਰੀ ਐਪ ਅਨੁਵਾਦ ਲਈ ਆਪਣੀ ਪਸੰਦੀਦਾ ਭਾਰਤੀ ਭਾਸ਼ਾ ਚੁਣੋ:"
            ),
            "children_profiles" to mapOf(
                "en" to "Children Profiles", "hi" to "बच्चों की प्रोफ़ाइल", "te" to "పిల్లల ప్రొఫైల్‌లు", "ta" to "குழந்தைகளின் சுயவிவரங்கள்",
                "kn" to "ಮಕ್ಕಳ ಪ್ರೊಫೈಲ್‌ಗಳು", "ml" to "കുട്ടികളുടെ പ്രൊഫൈലുകൾ", "mr" to "मुलांची प्रोफाइल", "bn" to "শিশুদের প্রোফাইল", "gu" to "બાળકોની પ્રોફાઇલ", "pa" to "ਬੱਚਿਆਂ ਦੀਆਂ ਪ੍ਰੋਫਾਈਲਾਂ"
            ),
            "todays_schedule" to mapOf(
                "en" to "Today's Schedule & Reminders", "hi" to "आज का शेड्यूल और रिमाइंडर", "te" to "ఈరోజు షెడ్యూల్ & రిమైండర్‌లు", "ta" to "இன்றைய அட்டவணை",
                "kn" to "ಇಂದಿನ ವೇಳಾಪಟ್ಟಿ", "ml" to "ഇന്നത്തെ ഷെഡ്യൂൾ", "mr" to "आजचे वेळापत्रक", "bn" to "আজকের সময়সূচী", "gu" to "આજનું શિડ્યુલ", "pa" to "ਅੱਜ ਦਾ ਸ਼ਡਿਊਲ"
            ),
            "vaccines" to mapOf(
                "en" to "Vaccines & Immunization", "hi" to "टीकाकरण और वैक्सीन", "te" to "టీకాలు & రోగనిరోధకత", "ta" to "தடுப்பூசிகள்",
                "kn" to "ಲಸಿಕೆಗಳು", "ml" to "വാക്സിനുകൾ", "mr" to "लसीकरण", "bn" to "টিকা ও ইমিউনাইজেশন", "gu" to "રસીકરણ", "pa" to "ਟੀਕਾਕਰਨ"
            ),
            "food_diary" to mapOf(
                "en" to "Food & Nutrition Diary", "hi" to "भोजन और पोषण डायरी", "te" to "ఆహార & పోషకాహార డైరీ", "ta" to "உணவு நாட்குறிப்பு",
                "kn" to "ಆಹಾರ ಡೈರಿ", "ml" to "ഭക്ഷണ ഡയറി", "mr" to "अन्न डायरी", "bn" to "খাবার ডায়েরি", "gu" to "ખોરાક ડાયરી", "pa" to "ਖੁਰਾਕ ਡਾਇਰੀ"
            ),
            "medicines" to mapOf(
                "en" to "Medicines & Tracker", "hi" to "दवाइयां और ट्रैकर", "te" to "మందులు & ట్రాకర్", "ta" to "மருந்துகள்",
                "kn" to "ಮದ್ದುಗಳು", "ml" to "മരുന്നുകൾ", "mr" to "औषधे", "bn" to "ওষুধ ও ট্র্যাকার", "gu" to "દવાઓ", "pa" to "ਦਵਾਈਆਂ"
            ),
            "sleep_naps" to mapOf(
                "en" to "Sleep & Naps Schedule", "hi" to "नींद और झपकी का शेड्यूल", "te" to "నిద్ర & కునుకు షెడ్యూల్", "ta" to "தூக்க அட்டவணை",
                "kn" to "ನಿದ್ರೆ ವೇಳಾಪಟ್ಟಿ", "ml" to "ഉറക്ക ഷെഡ്യൂൾ", "mr" to "झोप वेळापत्रक", "bn" to "ঘুমের সময়সূচী", "gu" to "ઊંઘ શિડ્યુલ", "pa" to "ਨੀਂਦ ਸ਼ਡਿਊਲ"
            ),
            "play_joy" to mapOf(
                "en" to "Play & Fun Activities", "hi" to "खेल और मजेदार गतिविधियां", "te" to "ఆటలు & కార్యకలాపాలు", "ta" to "விளையாட்டு",
                "kn" to "ಆಟಗಳು", "ml" to "കളികൾ", "mr" to "खेळ आणि उपक्रम", "bn" to "খেলাধুলা", "gu" to "રમત અને પ્રવૃત્તિઓ", "pa" to "ਖੇਡਾਂ"
            ),
            "appointments" to mapOf(
                "en" to "Doctor Appointments", "hi" to "डॉक्टर से मुलाकात", "te" to "డాక్టర్ అపాయింట్‌మెంట్‌లు", "ta" to "மருத்துவர் சந்திப்புகள்",
                "kn" to "ವೈದ್ಯರ ಭೇಟಿಗಳು", "ml" to "ഡോക്ടറുടെ അപ്പോയിന്റ്മെന്റുകൾ", "mr" to "डॉक्टरांच्या भेटी", "bn" to "ডাক্তারের অ্যাপয়েন্টমেন্ট", "gu" to "ડૉક્ટરની મુલાકાત", "pa" to "ਡਾਕਟਰ ਦੀਆਂ ਮੁਲਾਕਾਤਾਂ"
            ),
            "parent_profile" to mapOf(
                "en" to "Parent Account & Profiles", "hi" to "अभिभावक खाता और प्रोफ़ाइल", "te" to "తల్లిదండ్రుల ఖాతా & ప్రొఫైల్‌లు", "ta" to "பெற்றோர் கணக்கு",
                "kn" to "ಪೋಷಕರ ಖಾತೆ", "ml" to "രക്ഷിതാവിന്റെ അക്കൗണ്ട്", "mr" to "पालक खाते", "bn" to "অভিভাবক অ্যাকাউন্ট", "gu" to "વાલી ખાતું", "pa" to "ਮਾਪਿਆਂ ਦਾ ਖਾਤਾ"
            ),
            "add_child" to mapOf(
                "en" to "Add New Child Profile", "hi" to "नया बच्चा प्रोफ़ाइल जोड़ें", "te" to "కొత్త పిల్లల ప్రొఫైల్ జోడించండి", "ta" to "புதிய குழந்தை சுயவிவரத்தைச் சேர்",
                "kn" to "ಹೊಸ ಮಕ್ಕಳ ಪ್ರೊಫೈಲ್ ಸೇರಿಸಿ", "ml" to "പുതിയ കുട്ടിയുടെ പ്രൊഫൈൽ ചേർക്കുക", "mr" to "नवीन बाल प्रोफाइल जोडा", "bn" to "নতুন শিশু প্রোফাইল যোগ করুন", "gu" to "નવી બાળકની પ્રોફાઇલ ઉમેરો", "pa" to "ਨਵੀਂ ਬੱਚੇ ਦੀ ਪ੍ਰੋਫਾਈਲ ਜੋੜੋ"
            ),
            "remove_account" to mapOf(
                "en" to "Remove Parent Account", "hi" to "अभिभावक खाता हटाएं", "te" to "తల్లిదండ్రుల ఖాతాను తొలగించండి", "ta" to "பெற்றோர் கணக்கை நீக்குக",
                "kn" to "ಪೋಷಕರ ಖಾತೆಯನ್ನು ತೆಗೆದುಹಾಕಿ", "ml" to "രക്ഷിതാവിന്റെ അക്കൗണ്ട് നീക്കം ചെയ്യുക", "mr" to "पालक खाते हटवा", "bn" to "অভিভাবক অ্যাকাউন্ট সরান", "gu" to "વાલી ખાતું દૂર કરો", "pa" to "ਮਾਪਿਆਂ ਦਾ ਖਾਤਾ ਹਟਾਓ"
            ),
            "sign_out" to mapOf(
                "en" to "Sign Out", "hi" to "साइन आउट करें", "te" to "సైన్ అవుట్ చేయండి", "ta" to "வெளியேறு",
                "kn" to "ಸೈನ್ ಔಟ್ ಮಾಡಿ", "ml" to "സൈൻ ഔട്ട് ചെയ്യുക", "mr" to "साइन आउट करा", "bn" to "সাইন আউট করুন", "gu" to "સાઇન આઉਟ કરો", "pa" to "ਸਾਈਨ ਆਊਟ ਕਰੋ"
            ),
            "nav_kids" to mapOf(
                "en" to "Kids", "hi" to "बच्चे", "te" to "పిల్లలు", "ta" to "குழந்தைகள்",
                "kn" to "ಮಕ್ಕಳು", "ml" to "കുട്ടികൾ", "mr" to "मुले", "bn" to "শিশুরা", "gu" to "બાળકો", "pa" to "ਬੱਚੇ"
            ),
            "nav_parent" to mapOf(
                "en" to "Parent Profile", "hi" to "अभिभावक", "te" to "తల్లిదండ్రులు", "ta" to "பெற்றோர்",
                "kn" to "ಪೋಷಕರು", "ml" to "രക്ഷിതാവ്", "mr" to "पालक", "bn" to "অভিভাবক", "gu" to "વાલી", "pa" to "ਮਾਪੇ"
            ),
            "nav_settings" to mapOf(
                "en" to "Settings", "hi" to "सेटिंग्स", "te" to "సెట్టింగ్‌లు", "ta" to "அமைப்புகள்",
                "kn" to "ಸೆಟ್ಟಿಂಗ್‌ಗಳು", "ml" to "ക്രമീകരണങ്ങൾ", "mr" to "सेटिंग्ज", "bn" to "সেটিংস", "gu" to "સેટિંગ્સ", "pa" to "ਸੈਟਿੰਗਾਂ"
            ),
            "growth_milestones" to mapOf(
                "en" to "Growth & Milestones", "hi" to "विकास और मील के पत्थर", "te" to "ఎత్తు, బరువు & మైలురాళ్ళు", "ta" to "வளர்ச்சி மற்றும் மைல்கற்கள்",
                "kn" to "ಬೆಳವಣಿಗೆ ಮತ್ತು ಮೈಲಿಗಲ್ಲುಗಳು", "ml" to "വളർച്ചയും മൈലുകളും", "mr" to "वाढ आणि टप्पे", "bn" to "বৃদ্ধি ও মাইলেস্টোন", "gu" to "વિકાસ અને માઇલસ્ટોન્સ", "pa" to "ਵਾਧਾ ਅਤੇ ਮੀਲ ਦੇ ਪੱਥਰ"
            ),
            "cuby_alert" to mapOf(
                "en" to "Emergency Symptoms (CubyAlert)", "hi" to "आपातकालीन लक्षण (क्युबीअलर्ट)", "te" to "అత్యవసర లక్షణాలు (క్యూబీఅలర్ట్)", "ta" to "அவசர அறிகுறிகள் (கியூபிஅலர்ட்)",
                "kn" to "ತುರ್ತು ಲಕ್ಷಣಗಳು (ಕ್ಯೂಬಿಅಲರ್ಟ್)", "ml" to "അടിയന്തിര ലക്ഷണങ്ങൾ", "mr" to "तातडीची लक्षणे (क्युबीअलर्ट)", "bn" to "জরুরী লক্ষণ (কিউবিঅ্যালার্ট)", "gu" to "ઇમરજન્સી લક્ષણો (ક્યુબીએલર્ટ)", "pa" to "ਐਮਰਜੈਂਸੀ ਲੱਛਣ"
            ),
            "parenting_guide" to mapOf(
                "en" to "Parenting Guide & Advice", "hi" to "अभिभावकता मार्गदर्शन और सलाह", "te" to "తల్లిదండ్రుల మార్గదర్శకత్వం & సలహాలు", "ta" to "பெற்றோர் வழிகாட்டுதல்",
                "kn" to "ಪೋಷಕರ ಮಾರ್ಗದರ್ಶನ", "ml" to "രക്ഷിതാക്കളുടെ മാർഗ്ഗനിർദ്ദേശം", "mr" to "पालकत्व मार्गदर्शन", "bn" to "অভিভাবক নির্দেশিকা", "gu" to "માર્ગદર્શન અને સલાહ", "pa" to "ਮਾਪਿਆਂ ਦੀ ਅਗਵਾਈ"
            )
        )

        val value = translations[key]?.get(languageCode) 
            ?: translations[key]?.get("en") 
            ?: AppTranslations.dictionary[key]?.get(languageCode)
            ?: AppTranslations.dictionary[key]?.get("en")
            ?: key

        if (value.startsWith("dynamic_")) {
            return "Pediatric Health & Care"
        }
        
        if (value.startsWith("ai_chip_")) {
            return when (value) {
                "ai_chip_vaccine" -> "💉 Vaccines at 6 weeks"
                "ai_chip_alert", "ai_chip_fever" -> "🚨 Emergency fever alert"
                "ai_chip_nutrition" -> "🍎 6-12m Diet plan"
                "ai_chip_milestones", "ai_chip_milestone" -> "📈 1 year milestones"
                "ai_chip_sleep" -> "😴 Sleep & nap guide"
                "ai_chip_profile" -> "👤 My child's profile"
                else -> "💡 Health Guidance"
            }
        }

        return value
    }
}
