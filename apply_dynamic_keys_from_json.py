import os
import json

files = [
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyAlertScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyParentingScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\data\repository\VaccineScheduleProvider.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\data\repository\MilestoneProvider.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionHomeScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionAgeDetailScreen.kt"
]

json_path = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\assets\content_translations.json"
with open(json_path, 'r', encoding='utf-8') as f:
    translations = json.load(f)

# Sort by length descending
replacements = []
for key, val in sorted(translations.items(), key=lambda x: len(x[1]["en"]), reverse=True):
    replacements.append((val["en"], key))

for filepath in files:
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    for eng, key in replacements:
        # Reconstruct exactly how it appears in Kotlin
        # A simple approach: escape backslashes and quotes as it would be in Kotlin
        eng_escaped = eng.replace('\n', '\\n').replace('"', '\\"')
        search_str = '"' + eng_escaped + '"'
        replace_str = 'tr("' + key + '")'
        content = content.replace(search_str, replace_str)
        
    if "import co.csedge.cubycare.utils.tr" not in content:
        last_import = content.rfind("import ")
        if last_import != -1:
            eol = content.find("\n", last_import)
            content = content[:eol+1] + "import co.csedge.cubycare.utils.tr\n" + content[eol+1:]
            
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

print("Applied dynamic keys successfully.")
