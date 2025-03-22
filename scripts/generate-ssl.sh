#!/bin/bash
KEYSTORE_PATH="$(pwd)/src/main/resources/staffguard.p12"
mkdir -p "$(pwd)/src/main/resources"
keytool -genkeypair -alias staffguard -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore "$KEYSTORE_PATH" \
  -validity 3650 -storepass changeit -keypass changeit \
  -dname "CN=StaffGuard, OU=Dev, O=Company, L=Vienna, ST=Austria, C=AT"