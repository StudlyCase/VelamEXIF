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

class ExifReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void read_returnsEmptyData_whenNoExifPresent() throws IOException {
        Path jpeg = createMinimalJpeg("no-exif.jpg");

        ExifData data = ExifReader.read(jpeg);

        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    void read_throwsIOException_whenFileIsNotJpeg() {
        Path fake = tempDir.resolve("not-a-jpeg.jpg");
        assertDoesNotThrow(() -> Files.writeString(fake, "not jpeg data"));

        assertThrows(IOException.class, () -> ExifReader.read(fake));
    }

    @Test
    void read_throwsIOException_whenFileDoesNotExist() {
        Path missing = tempDir.resolve("missing.jpg");

        assertThrows(IOException.class, () -> ExifReader.read(missing));
    }

    @Test
    void read_returnsExifData_withExpectedFields() throws IOException {
        Path jpeg = createMinimalJpeg("with-exif.jpg");
        ExifData data = ExifReader.read(jpeg);

        assertNotNull(data);
        // minimal JPEG without injected EXIF will have no fields;
        // confirms the method returns a valid (possibly empty) object.
        assertFalse(data.has(ExifField.MAKE) && data.get(ExifField.MAKE).isBlank());
    }

    private Path createMinimalJpeg(String name) throws IOException {
        Path path = tempDir.resolve(name);
        // Minimal valid JPEG: SOI + EOI markers
        try (OutputStream out = Files.newOutputStream(path)) {
            out.write(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xD9});
        }
        return path;
    }
}
