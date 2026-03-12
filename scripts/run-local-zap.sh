#!/usr/bin/env bash
set -e

TARGET_URL=${1:-http://127.0.0.1:8080}

docker run --rm --network host \
  -v "$(pwd):/zap/wrk/:rw" \
  -t ghcr.io/zaproxy/zaproxy:stable \
  zap-baseline.py -t "$TARGET_URL" -r zap-report.html -J zap-report.json || true

echo "ZAP reports generated in the project root: zap-report.html and zap-report.json"
