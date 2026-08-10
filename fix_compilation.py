import os

dict_path = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\utils\AppTranslations.kt"

with open(dict_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace("$name", "\\$name")

with open(dict_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Escaped $name in AppTranslations.kt")
