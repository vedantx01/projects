package edu.oslab;

import java.util.Map;
import java.util.TreeMap;

public final class VirtualFileSystem {
    private final Map<String, String> files = new TreeMap<>();

    public void create(String path, String contents) {
        String normalized = normalize(path);
        if (files.containsKey(normalized)) {
            throw new IllegalArgumentException("File already exists: " + normalized);
        }
        files.put(normalized, contents == null ? "" : contents);
    }

    public String read(String path) {
        String normalized = normalize(path);
        if (!files.containsKey(normalized)) {
            throw new IllegalArgumentException("File does not exist: " + normalized);
        }
        return files.get(normalized);
    }

    public void delete(String path) {
        String normalized = normalize(path);
        if (files.remove(normalized) == null) {
            throw new IllegalArgumentException("File does not exist: " + normalized);
        }
    }

    public Map<String, String> list() {
        return Map.copyOf(files);
    }

    private String normalize(String path) {
        if (path == null || path.isBlank() || !path.startsWith("/")) {
            throw new IllegalArgumentException("Use an absolute path beginning with '/'");
        }
        String normalized = path.replaceAll("/{2,}", "/");
        for (String part : normalized.split("/")) {
            if (part.equals(".") || part.equals("..")) {
                throw new IllegalArgumentException("Relative path components are not supported");
            }
        }
        return normalized;
    }

    public static void runDemo() {
        VirtualFileSystem fileSystem = new VirtualFileSystem();
        fileSystem.create("/home/student/notes.txt", "Filesystem metadata maps a name to data.");
        fileSystem.create("/tmp/output.txt", "Created in memory.");
        System.out.println("Read /home/student/notes.txt: "
                + fileSystem.read("/home/student/notes.txt"));
        System.out.println("Directory contents: " + fileSystem.list());
    }
}
