#!/bin/bash

./gradlew clean --task-graph

./gradlew publish --task-graph

./gradlew jreleaserFullRelease --task-graph
