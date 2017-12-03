#!/bin/sh
./gradlew annotation:bintrayUpload -P release_bintray=true
./gradlew library:bintrayUpload -P release_bintray=true
./gradlew plugin:bintrayUpload -P release_bintray=true
