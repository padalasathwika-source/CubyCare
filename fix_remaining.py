import os

replacements = {
    'c:\\Users\\ABDUL MANNAN\\Downloads\\CubyCare-main\\CubyCare-main\\app\\src\\main\\java\\co\\csedge\\cubycare\\ui\\auth\\AuthScreen.kt': {
        'Text("Welcome to CubyCare"': 'Text(tr("welcome")',
        'Text("Sign in with Google"': 'Text(tr("sign_in_google")'
    },
    'c:\\Users\\ABDUL MANNAN\\Downloads\\CubyCare-main\\CubyCare-main\\app\\src\\main\\java\\co\\csedge\\cubycare\\ui\\dashboard\\DashboardScreen.kt': {
        'text = "Monitor your baby\'s growth and milestones"': 'text = tr("monitor_growth")',
        'text = "No child profile found. Please add a child."': 'text = tr("no_child")'
    },
    'c:\\Users\\ABDUL MANNAN\\Downloads\\CubyCare-main\\CubyCare-main\\app\\src\\main\\java\\co\\csedge\\cubycare\\ui\\dashboard\\NutritionHomeScreen.kt': {
        'text = "No food diary entries yet. Tap + to add one."': 'text = tr("no_food")'
    },
    'c:\\Users\\ABDUL MANNAN\\Downloads\\CubyCare-main\\CubyCare-main\\app\\src\\main\\java\\co\\csedge\\cubycare\\ui\\dashboard\\MedicineTrackerScreen.kt': {
        'text = "No medicines added yet.\\nTap + to add one."': 'text = tr("no_meds")'
    },
    'c:\\Users\\ABDUL MANNAN\\Downloads\\CubyCare-main\\CubyCare-main\\app\\src\\main\\java\\co\\csedge\\cubycare\\ui\\dashboard\\GrowthScreen.kt': {
        'text = "No growth logs yet. Click + to add one."': 'text = tr("no_growth")'
    }
}

for filepath, file_replacements in replacements.items():
    if not os.path.exists(filepath): continue
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    modified = False
    for old, new in file_replacements.items():
        if old in content:
            content = content.replace(old, new)
            modified = True
            
    if modified:
        if "import co.csedge.cubycare.utils.tr" not in content:
            last_import_idx = content.rfind("import ")
            if last_import_idx != -1:
                end_of_line = content.find("\n", last_import_idx)
                content = content[:end_of_line+1] + "import co.csedge.cubycare.utils.tr\n" + content[end_of_line+1:]
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated {filepath}")
