import json
import sys

sys.stdout.reconfigure(encoding='utf-8')

with open('app/src/main/assets/content_translations.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

found = []
for k, v in data.items():
    if isinstance(v, dict):
        for lang, text in v.items():
            if '$' in text or 'age' in text:
                if '$age' in text or '$ageRange' in text:
                    found.append((k, lang, text))

for item in found:
    print(item)
