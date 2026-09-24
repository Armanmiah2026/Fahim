# Fahim

## যদি  মোবাইল  থেকে কোনো প্রকার প্রজেক্ট  github আপলোড  করত্র চাও তাহলে এই কমান্ড দাও তাহলে আপলোড  হয়ে যাবে খুব শজে
## অবশ্যই তোমার github নাম এবং github token Change করে দিবে, 

## github name : Armanmiah2026
## github token: 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

```
pkg update && pkg upgrade -y && pkg install git -y && termux-setup-storage && cd /storage/emulated/0/arman && git init && git branch -M main && git add . && git commit -m "First commit" 2>/dev/null || true && git remote set-url origin https://Armanmiah2026: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@github.com/Armanmiah2026/Fahim.git || git remote add origin https://Armanmiah2026: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@github.com/Armanmiah2026/Fahim.git && git push -u origin main --force

```


## ১/ যদি সোর্স কোড। apk বানাইতে চাও তাহলে এই কোড প্রথম তৈরি করো
```
.github/workflows/build_apk.yml
```

## ২/ আপ্স apk বানানোর পর কোথায় দেখবা এবং খুব দ্রুত বিল্ড হবে সেই জন্য এই কোড ১ নাম্বার  ফাইলের ভিতর বসিয়ে দাও
```name: Build Android APK

on:
  push:
    branches: [ "master", "main" ]
  pull_request:
    branches: [ "master", "main" ]
  workflow_dispatch:

jobs:
  build:
    name: Build APK with Gradle
    runs-on: ubuntu-latest

    steps:
    - name: Checkout Code
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        distribution: 'temurin'
        java-version: '17'
        cache: 'gradle'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build Debug APK with Gradle
      run: ./gradlew assembleDebug

    - name: Upload APK Artifact
      uses: actions/upload-artifact@v4
      with:
        name: app-debug
        path: |
          **/*.apk
          app/build/outputs/apk/**/*.apk
          build/outputs/apk/**/*.apk
```
