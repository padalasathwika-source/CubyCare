import json

with open('app/src/main/assets/content_translations.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# Update dynamic_7
if 'dynamic_7' in data:
    data['dynamic_7'] = {
        'en': 'Nutrition & Diet',
        'hi': 'पोषण और आहार',
        'te': 'పోషణ మరియు ఆహారం',
        'ta': 'ஊட்டச்சத்து மற்றும் உணவு',
        'kn': 'ಪೋಷಣೆ ಮತ್ತು ಆಹಾರ',
        'ml': 'പോഷകാഹാരവും ഭക്ഷണവും',
        'mr': 'पोषण आणि आहार',
        'bn': 'পুষ্টি এবং খাদ্য',
        'gu': 'પોષણ અને આહાર',
        'pa': 'ਪੋਸ਼ਣ ਅਤੇ ਖੁਰਾਕ'
    }

# Update dynamic_204
if 'dynamic_204' in data:
    data['dynamic_204'] = {
        'en': 'Feeding & Nutrition Guidelines',
        'hi': 'आहार और पोषण संबंधी दिशानिर्देश',
        'te': 'ఆహారం మరియు పోషకాహార మార్గదర్శకాలు',
        'ta': 'உணவு மற்றும் ஊட்டச்சத்து வழிகாட்டுதல்கள்',
        'kn': 'ಆಹಾರ ಮತ್ತು ಪೋಷಣೆ ಮಾರ್ಗದರ್ಶಿಗಳು',
        'ml': 'ഭക്ഷണവും പോഷകാഹാര മാർഗ്ഗനിർദ്ദേശങ്ങളും',
        'mr': 'आहार आणि पोषण मार्गदर्शक तत्त्वे',
        'bn': 'খাদ্য এবং পুষ্টি নির্দেশিকা',
        'gu': 'આહાર અને પોષણ માર્ગદર્શિકા',
        'pa': 'ਖੁਰਾਕ ਅਤੇ ਪੋਸ਼ਣ ਸੰਬੰਧੀ ਦਿਸ਼ਾ-ਨਿਰਦੇਸ਼'
    }

# Update dynamic_462
if 'dynamic_462' in data:
    data['dynamic_462'] = {
        'en': 'Weekly Meal Plan',
        'hi': 'साप्ताहिक भोजन योजना',
        'te': 'వారంవారీ భోజన ప్రణాళిక',
        'ta': 'வாராந்திர உணவுத் திட்டம்',
        'kn': 'ವಾರದ ಊಟದ ಯೋಜನೆ',
        'ml': 'ആഴ്ചയിലെ ഭക്ഷണ പദ്ധതി',
        'mr': 'साप्ताहिक जेवण योजना',
        'bn': 'সাপ্তাহিক খাবার পরিকল্পনা',
        'gu': 'સાપ્તાહિક ભોજન યોજના',
        'pa': 'ਹਫ਼ਤਾਵਾਰੀ ਖਾਣੇ ਦੀ ਯੋਜਨਾ'
    }

with open('app/src/main/assets/content_translations.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, ensure_ascii=False, indent=2)

print("Updated content_translations.json successfully!")
