#!/bin/bash

HockeyAppToken="092a0f588a394240b054067f85d5a0f5"
AndroidAppId="2814f91ea2dc45cd9a63b05ff1c1266f"

./gradlew clean
./gradlew assembleDebug

curl -v \
-F "status=2" \
-F "notify=0" \
-F "ipa=@app/build/outputs/apk/debug/app-debug.apk" \
-H "X-HockeyAppToken: $HockeyAppToken" \
https://rink.hockeyapp.net/api/2/apps/$AndroidAppId/app_versions/upload
