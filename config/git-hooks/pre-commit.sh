#!/bin/bash

if [[ $CI == "true" ]]; then
  exit 0
fi

if [[ -n $(git rev-parse --abbrev-ref HEAD | grep '^hotfix/\|^master$\|^release/\|^develop$') ]]; then
  echo -e "\033[31mDirect commit on protected branch is prohibited. Please change your branch name\033[31m"
  exit 1
fi

bold=$(tput bold)
normal=$(tput sgr0)

# 1. Run Detekt + Spotless
echo -e "👩‍🏫 ${bold}Running auto-format and analyze\n${normal}"

filesToAddAfterFormatting=()

# Collect all files currently in staging area, and check if there are any kotlin files
for entry in $(git status --porcelain | sed -r 's/[ ]+/-/g'); do
  # Entry can be for example:
  # MM-src/main/java/com/glovo/MyActivity.kt
  # -M-src/main/java/com/glovo/AnotherActivity.kt
  # When the format starts with an M then the file has been added to the index

  if [[ $entry == M* ]]; then
    filesToAddAfterFormatting+=(${entry:2}) # strips the prefix
  fi
done

# Define GIT_DIR env variable before executing, as during pre-hook execution `git` will set it to
# the relative path (".git"). Whereas gradle expects an absolute path ("~/<glovo-project-path>/.git") to work properly.
echo -e "👀 ${bold}Applying auto-formatting and linting${normal}\n"
GIT_DIR="$PWD/.git" ./gradlew detektAffected --no-build-cache

EXIT_CODE=$?

if [[ ${EXIT_CODE} -ne 0 ]]; then
  echo -e " ***********************************************\n"
  echo -e "       🚨 Auto-format and analyze failed       \n"
  echo -e " ${bold}Please, fix the above issues before committing${normal}\n"
  echo -e " ***********************************************"
  exit ${EXIT_CODE}
fi

# Add the files that were in the staging area back
for fileToAdd in $filesToAddAfterFormatting; do
  git add "$fileToAdd"
done

echo -e "\n✅ ${bold} Auto-format and analyze completed${normal}\n"


# GPT
KTLINT_COMMAND="ktlint"

# Get the list of staged files
STAGED_FILES=$(git diff --cached --name-only --diff-filter=ACM | grep '\.kt$')

# Run KtLint on staged files
if [ -n "$STAGED_FILES" ]; then
  echo "Running KtLint on staged files..."
  $KTLINT_COMMAND --format --relative $STAGED_FILES

  # Add the changes made by KtLint to the staging area
  git add $STAGED_FILES

  echo "KtLint auto-formatting completed."
fi
#

exit 0
