# Encryption

Encryption transforms plaintext into ciphertext using a key; decryption
requires the corresponding key. Authenticated encryption such as AES-GCM also
detects tampering. Each AES-GCM encryption must use a unique nonce/IV for a
given key. The lab uses JDK cryptographic APIs and randomly generated demo
keys; it is not production key management.
