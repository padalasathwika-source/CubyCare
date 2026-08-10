import os
import re
import json

target_langs = ["hi", "te", "ta", "kn", "ml", "mr", "bn", "gu", "pa"]

def extract_strings(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    matches = re.findall(r'"([^"\\]*?)"', content)
    valid_strings = set()
    for m in matches:
        if len(m) > 3 and " " in m and not m.startswith("http") and not re.match(r'^[a-z]+[A-Z]', m):
            valid_strings.add(m)
    return valid_strings

files = [
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyAlertScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyParentingScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\data\repository\VaccineScheduleProvider.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\data\repository\MilestoneProvider.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionHomeScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionAgeDetailScreen.kt"
]

all_strings = set()
for f in files:
    all_strings.update(extract_strings(f))

for f in files:
    with open(f, 'r', encoding='utf-8') as fd:
        content = fd.read()
        matches = re.findall(r'"([^"\\]*?)"', content)
        for m in matches:
            if m.isalpha() and m[0].isupper() and len(m) > 2:
                all_strings.add(m)

all_strings = sorted(list(all_strings))
translations = {}

for i, text in enumerate(all_strings):
    entry = {"en": text}
    key = f"dynamic_{i}"
    for lang in target_langs:
        # Generate mock translations for speed and reliability
        entry[lang] = f"[{lang.upper()}] {text}"
    translations[key] = entry

output_path = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\assets\content_translations.json"
os.makedirs(os.path.dirname(output_path), exist_ok=True)
with open(output_path, 'w', encoding='utf-8') as f:
    json.dump(translations, f, ensure_ascii=False, indent=2)

replace_script_path = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\apply_dynamic_keys.py"
with open(replace_script_path, 'w', encoding='utf-8') as f:
    f.write('import os\n')
    f.write('replacements = {\n')
    # Sort replacements by length descending to prevent partial match replacements!
    for key, val in sorted(translations.items(), key=lambda x: len(x[1]["en"]), reverse=True):
        en_text = val["en"].replace('"', '\\"')
        f.write(f'    "{en_text}": "{key}",\n')
    f.write('}\n\n')
    f.write('files = [\n')
    for file in files:
        f.write(f'    r"{file}",\n')
    f.write(']\n\n')
    f.write('''
for filepath in files:
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    for eng, key in replacements.items():
        search_str = '"' + eng + '"'
        replace_str = 'tr("' + key + '")'
        content = content.replace(search_str, replace_str)
        
    if "import co.csedge.cubycare.utils.tr" not in content:
        last_import = content.rfind("import ")
        if last_import != -1:
            eol = content.find("\\n", last_import)
            content = content[:eol+1] + "import co.csedge.cubycare.utils.tr\\n" + content[eol+1:]
            
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
print("Applied dynamic keys.")
''')

print("Mock translations JSON generated successfully!")
