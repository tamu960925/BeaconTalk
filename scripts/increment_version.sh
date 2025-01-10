#!/bin/bash

# Get versionName from build.gradle file
VERSION_NAME=$(grep "versionName" build.gradle | awk -F'"' '{print $2}')

# Split version number into major and minor
MAJOR=$(echo $VERSION_NAME | cut -d'.' -f1)
MINOR=$(echo $VERSION_NAME | cut -d'.' -f2)

# Increment the minor version
NEW_MINOR=$((MINOR + 1))

# Create new version name
NEW_VERSION_NAME="$MAJOR.$NEW_MINOR"

# Replace versionName in build.gradle file
sed -i "s/versionName \"$VERSION_NAME\"/versionName \"$NEW_VERSION_NAME\"/g" build.gradle

echo "Updated version from $VERSION_NAME to $NEW_VERSION_NAME."