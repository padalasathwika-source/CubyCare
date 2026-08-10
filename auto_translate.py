import os
import re

ui_dir = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui"

# The map matches EXACT English text to the translation key
translations = {
    "FAQ & Pediatric User Guide": "faq_title",
    "Themes & Appearance": "themes_appearance",
    "Dark Mode": "dark_mode",
    "Contact Support": "contact_support",
    "Privacy Policy": "privacy_policy",
    "Cancel": "cancel",
    "Delete": "delete",
    "Save Changes": "save_changes",
    "Child Name": "child_name",
    "Date of Birth (DD/MM/YYYY)": "dob",
    "Gender": "gender",
    "Blood Group": "blood_group",
    "Birth Weight": "birth_weight",
    "Birth Length": "birth_length",
    "Head Circumference at Birth": "head_circ",
    "Current Weight (Today)": "current_weight",
    "Current Length / Height (Today)": "current_length",
    "Genetic Issues / Disorders": "genetic_issues",
    "Allergies": "allergies",
    "Current Medical Conditions": "medical_conditions",
    "App Settings & Preferences": "app_settings",
    "Children Profiles": "children_profiles",
    "Vaccines": "vaccines",
    "Food Diary": "food_diary",
    "Medicines": "medicines",
    "Sleep & Naps": "sleep_naps",
    "Play & Joy": "play_joy",
    "Appointments": "appointments",
    "Parent Profile": "parent_profile",
    "Add Child": "add_child",
    "Remove Parent Account": "remove_account",
    "Sign Out": "sign_out",
    "Kids": "nav_kids",
    "Settings": "nav_settings",
    "Growth & Milestones": "growth_milestones",
    "Cuby Alert": "cuby_alert",
    "Cuby Parenting": "parenting_guide"
}

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original_content = content
    modified = False

    for eng, key in translations.items():
        # Match Text("Eng")
        pattern1 = r'Text\(\s*"' + re.escape(eng) + r'"\s*\)'
        repl1 = f'Text(tr("{key}"))'
        if re.search(pattern1, content):
            content = re.sub(pattern1, repl1, content)
            modified = True

        # Match Text("Eng", ...)
        pattern2 = r'Text\(\s*"' + re.escape(eng) + r'"\s*,'
        repl2 = f'Text(tr("{key}"),'
        if re.search(pattern2, content):
            content = re.sub(pattern2, repl2, content)
            modified = True

    if modified:
        # Check if import exists
        if "import co.csedge.cubycare.utils.tr" not in content:
            # Find the last import
            last_import_idx = content.rfind("import ")
            if last_import_idx != -1:
                end_of_line = content.find("\n", last_import_idx)
                content = content[:end_of_line+1] + "import co.csedge.cubycare.utils.tr\n" + content[end_of_line+1:]
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, _, files in os.walk(ui_dir):
    for file in files:
        if file.endswith(".kt"):
            process_file(os.path.join(root, file))

print("Auto-translation applied.")
