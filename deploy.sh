#!/bin/sh
./gradlew annotation:bintrayUpload
./gradlew library:bintrayUpload
./gradlew plugin:bintrayUpload
