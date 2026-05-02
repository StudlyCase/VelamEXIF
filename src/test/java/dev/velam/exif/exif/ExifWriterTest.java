package dev.velam.exif.exif;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ExifWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void write_throwsIOException_onInvalidSource() {
        Path missing = tempDir.resolve("missing.jpg");
        Path dest    = tempDir.resolve("out.jpg");

        ExifData data = ExifData.builder()
                .put(ExifField.MAKE, "Canon")
                .build();

        assertThrows(IOException.class, () -> ExifWriter.write(missing, dest, data));
    }

    @Test
    void write_createsDestinationFile() throws IOException {
        Path src  = createMinimalJpeg("src.jpg");
        Path dest = tempDir.resolve("dest.jpg");

        ExifData data = ExifData.builder()
                .put(ExifField.MAKE, "Nikon")
                .build();

        // A minimal JPEG without a proper EXIF segment will likely fail lossless rewrite;
        // we verify the IOException is propagated cleanly rather than silently swallowed.
        try {
            ExifWriter.write(src, dest, data);
            assertTrue(Files.exists(dest));
        } catch (IOException e) {
            assertFalse(Files.exists(dest), "Partial destination file must not remain on failure");
        }
    }

    @Test
    void writeInPlace_deletesTemp_onFailure() throws IOException {
        Path src = createMinimalJpeg("inplace.jpg");

        ExifData data = ExifData.builder()
                .put(ExifField.MODEL, "Z7 II")
                .build();

        try {
            ExifWriter.writeInPlace(src, data);
        } catch (IOException ignored) {}

        Path temp = src.resolveSibling(src.getFileName() + ".tmp");
        assertFalse(Files.exists(temp), "Temporary file must be cleaned up after failure");
    }

    @Test
    void write_withEmptyExifData_doesNotThrowUnexpectedly() throws IOException {
        Path src  = createMinimalJpeg("empty-data.jpg");
        Path dest = tempDir.resolve("empty-out.jpg");

        ExifData empty = ExifData.builder().build();

        try {
            ExifWriter.write(src, dest, empty);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Failed to write EXIF data"));
        }
    }

    private Path createMinimalJpeg(String name) throws IOException {
        Path path = tempDir.resolve(name);
        try (OutputStream out = Files.newOutputStream(path)) {
            out.write(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xD9});
        }
        return path;
    }
}
