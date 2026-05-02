package dev.velam.exif.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileValidator {

    private static final byte[] JPEG_MAGIC = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final long MAX_FILE_SIZE = 100L * 1024 * 1024;

    private FileValidator() {}

    public static void validate(Path path) throws IOException {
        checkExists(path);
        checkReadable(path);
        checkSize(path);
        checkJpegSignature(path);
    }

    public static boolean isValid(Path path) {
        try {
            validate(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void checkExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path);
        }
        if (Files.isDirectory(path)) {
            throw new IOException("Path is a directory: " + path);
        }
    }

    private static void checkReadable(Path path) throws IOException {
        if (!Files.isReadable(path)) {
            throw new IOException("File is not readable: " + path);
        }
    }

    private static void checkSize(Path path) throws IOException {
        long size = Files.size(path);
        if (size == 0) {
            throw new IOException("File is empty: " + path);
        }
        if (size > MAX_FILE_SIZE) {
            throw new IOException("File exceeds maximum allowed size of 100 MB: " + path);
        }
    }

    private static void checkJpegSignature(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            byte[] header = new byte[JPEG_MAGIC.length];
            int read = in.read(header);
            if (read < JPEG_MAGIC.length || !matchesMagic(header)) {
                throw new IOException("File is not a valid JPEG: " + path);
            }
        }
    }

    private static boolean matchesMagic(byte[] header) {
        for (int i = 0; i < JPEG_MAGIC.length; i++) {
            if (header[i] != JPEG_MAGIC[i]) return false;
        }
        return true;
    }
}
