#!/usr/bin/env bash
set -e

if [ ! -d .git ]; then
  echo "This folder is not a Git repository yet. Run: git init"
  exit 1
fi

mkdir -p .git/hooks
cp hooks/pre-commit .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit

echo "Pre-commit hook installed successfully."
