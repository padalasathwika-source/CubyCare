import json
import sys

sys.stdout.reconfigure(encoding='utf-8')

with open('app/src/main/assets/content_translations.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

for k in ['dynamic_7', 'dynamic_204', 'dynamic_462']:
    print(f"{k} ==> {data.get(k, {}).get('en')}")
