package edu.oslab;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Base64;
import java.util.HashSet;

public final class SecurityLab {
    public enum Permission {
        READ,
        WRITE,
        EXECUTE
    }

    private record Credential(byte[] salt, byte[] hash) { }
    public record EncryptedMessage(byte[] iv, byte[] ciphertext) {
        public EncryptedMessage {
            if (iv == null || ciphertext == null) {
                throw new IllegalArgumentException("IV and ciphertext are required");
            }
            iv = iv.clone();
            ciphertext = ciphertext.clone();
        }

        @Override
        public byte[] iv() {
            return iv.clone();
        }

        @Override
        public byte[] ciphertext() {
            return ciphertext.clone();
        }
    }

    private static final int ITERATIONS = 120_000;
    private static final int KEY_BITS = 256;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, Credential> credentials = new HashMap<>();
    private final Map<String, Set<Permission>> rolePermissions = new HashMap<>();
    private final Map<String, String> userRoles = new HashMap<>();
    private final Set<String> authenticatedUsers = new HashSet<>();

    public void register(String user, char[] password, String role) throws GeneralSecurityException {
        if (user == null || user.isBlank() || role == null || role.isBlank()
                || password == null || password.length < 8) {
            throw new IllegalArgumentException("Use a user, role, and password of at least 8 characters");
        }
        if (credentials.containsKey(user)) {
            throw new IllegalArgumentException("User already exists: " + user);
        }
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        try {
            credentials.put(user, new Credential(salt, deriveKey(password, salt)));
            userRoles.put(user, role);
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    public boolean authenticate(String user, char[] password) throws GeneralSecurityException {
        Credential credential = credentials.get(user);
        if (password == null) {
            throw new IllegalArgumentException("Password must not be null");
        }
        if (credential == null) {
            authenticatedUsers.remove(user);
            Arrays.fill(password, '\0');
            return false;
        }
        byte[] candidate;
        try {
            candidate = deriveKey(password, credential.salt());
        } finally {
            Arrays.fill(password, '\0');
        }
        boolean authenticated = MessageDigest.isEqual(candidate, credential.hash());
        if (authenticated) {
            authenticatedUsers.add(user);
        } else {
            authenticatedUsers.remove(user);
        }
        return authenticated;
    }

    public void grant(String role, Permission... permissions) {
        if (role == null || role.isBlank() || permissions == null) {
            throw new IllegalArgumentException("Role and permissions are required");
        }
        if (Arrays.stream(permissions).anyMatch(permission -> permission == null)) {
            throw new IllegalArgumentException("Permission entries must not be null");
        }
        Set<Permission> grants = rolePermissions.computeIfAbsent(role,
                ignored -> EnumSet.noneOf(Permission.class));
        grants.addAll(Arrays.asList(permissions));
    }

    public boolean isAuthorized(String user, Permission permission) {
        String role = userRoles.get(user);
        return authenticatedUsers.contains(user) && role != null
                && rolePermissions.getOrDefault(role, Set.of()).contains(permission);
    }

    public SecretKey generateAesKey() throws GeneralSecurityException {
        javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("AES");
        generator.init(KEY_BITS);
        return generator.generateKey();
    }

    public EncryptedMessage encrypt(SecretKey key, String plaintext) throws GeneralSecurityException {
        if (key == null || plaintext == null) {
            throw new IllegalArgumentException("Encryption key and plaintext are required");
        }
        byte[] iv = new byte[12];
        random.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
        return new EncryptedMessage(iv, cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(SecretKey key, EncryptedMessage encrypted) throws GeneralSecurityException {
        if (key == null || encrypted == null) {
            throw new IllegalArgumentException("Encryption key and ciphertext are required");
        }
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, encrypted.iv()));
        return new String(cipher.doFinal(encrypted.ciphertext()), StandardCharsets.UTF_8);
    }

    private byte[] deriveKey(char[] password, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }

    public static void runDemo() throws GeneralSecurityException {
        SecurityLab security = new SecurityLab();
        security.grant("student", Permission.READ);
        security.register("alex", "demo-password".toCharArray(), "student");
        System.out.println("Authentication succeeds: "
                + security.authenticate("alex", "demo-password".toCharArray()));
        System.out.println("Authenticated user's WRITE access: "
                + security.isAuthorized("alex", Permission.WRITE));
        SecretKey key = security.generateAesKey();
        EncryptedMessage encrypted = security.encrypt(key, "Operating systems lab");
        System.out.println("AES-GCM ciphertext (Base64): "
                + Base64.getEncoder().encodeToString(encrypted.ciphertext()));
        System.out.println("Decrypted: " + security.decrypt(key, encrypted));
    }

}
