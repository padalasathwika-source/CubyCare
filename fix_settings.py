import os

settings_path = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\settings\SettingsScreen.kt"

with open(settings_path, 'r', encoding='utf-8') as f:
    content = f.read()

replacements = {
    'title = "App Color Theme & Palette"': 'title = tr("settings_theme_title")',
    'Text(\n                                text = "Select App Color Theme"': 'Text(\n                                text = tr("settings_theme_select")',
    'Text(\n                                text = "Tap a theme palette below to customize the app appearance instantaneously:"': 'Text(\n                                text = tr("settings_theme_desc")',
    'title = "Notifications & Reminders"': 'title = tr("settings_notif_title")',
    'title = "Vaccine Due Reminders"': 'title = tr("settings_notif_vaccine")',
    'subtitle = "Alerts 7 days before upcoming vaccine dates"': 'subtitle = tr("settings_notif_vaccine_desc")',
    'title = "Medicine Tracker Alerts"': 'title = tr("settings_notif_med")',
    'subtitle = "Push notifications for active medicine doses"': 'subtitle = tr("settings_notif_med_desc")',
    'title = "Daily Care & Joy Tips"': 'title = tr("settings_notif_tips")',
    'subtitle = "Get pediatric tips for growth & naps"': 'subtitle = tr("settings_notif_tips_desc")',
    'title = "Monthly Growth Milestone Logins"': 'title = tr("settings_notif_growth")',
    'subtitle = "Reminders to log height & weight updates"': 'subtitle = tr("settings_notif_growth_desc")',
    'title = "Units & Measurements"': 'title = tr("settings_units_title")',
    'title = if (useMetricUnits) "Metric Units (kg, cm)" else "Imperial Units (lbs, inches)"': 'title = if (useMetricUnits) tr("settings_units_metric") else tr("settings_units_imperial")',
    'subtitle = "Toggle between Metric (kg/cm) and Imperial (lbs/in)"': 'subtitle = tr("settings_units_desc")',
    'title = "Data Export & Cloud Sync"': 'title = tr("settings_export_title")',
    'title = "Cloud Backup & Sync Status"': 'title = tr("settings_export_sync")',
    'subtitle = "Auto-synced to secure Firebase account"': 'subtitle = tr("settings_export_sync_desc")',
    'title = "Export Medical Report Summary (Text/PDF)"': 'title = tr("settings_export_pdf")',
    'subtitle = "Share report via WhatsApp, Email, or Files"': 'subtitle = tr("settings_export_pdf_desc")',
    'title = "Export Child Data Sheet (CSV File)"': 'title = tr("settings_export_csv")',
    'subtitle = "Download raw CSV file for doctors & clinics"': 'subtitle = tr("settings_export_csv_desc")',
    'title = "Pediatric Support & User Guide"': 'title = tr("settings_support_title")',
    'title = "CubyCare FAQ & User Guide"': 'title = tr("settings_support_faq")',
    'subtitle = "Interactive guide for growth & vaccine tracking"': 'subtitle = tr("settings_support_faq_desc")',
    'title = "Contact Pediatric Support Team"': 'title = tr("settings_support_contact")',
    'subtitle = "Email support@cubycare.com directly"': 'subtitle = tr("settings_support_contact_desc")',
    'title = "About Application"': 'title = tr("settings_about_title")',
    'Text("CubyCare Version"': 'Text(tr("settings_about_version")',
    'Text("Pediatric Health & Growth Companion"': 'Text(tr("settings_about_desc")',
    'Text("HIPAA & Data Privacy Protected"': 'Text(tr("settings_about_privacy")',
    'title = if (cacheCleared) "Cache Cleared" else "Clear Temporary Storage"': 'title = if (cacheCleared) tr("settings_cache_cleared") else tr("settings_cache_clear")',
    'subtitle = if (cacheCleared) "0 KB temporary cache" else "Free up ~12.4 MB temporary app cache"': 'subtitle = if (cacheCleared) tr("settings_cache_cleared_desc") else tr("settings_cache_clear_desc")'
}

for old, new in replacements.items():
    content = content.replace(old, new)

if "import co.csedge.cubycare.utils.tr" not in content:
    last_import_idx = content.rfind("import ")
    end_of_line = content.find("\n", last_import_idx)
    content = content[:end_of_line+1] + "import co.csedge.cubycare.utils.tr\n" + content[end_of_line+1:]

with open(settings_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("SettingsScreen updated.")
