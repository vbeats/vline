#!/bin/bash

./gradlew clean

./gradlew publish

./gradlew jreleaserFullRelease
