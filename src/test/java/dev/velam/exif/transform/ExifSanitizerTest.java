package dev.velam.exif.exif;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExifSanitizerTest {

    @Test
    void strip_removesAllSensitiveFields() {
        ExifData data = ExifData.builder()
                .put(ExifField.GPS_LATITUDE,         "48/1,8/1,0/100")
                .put(ExifField.GPS_LATITUDE_REF,     "N")
                .put(ExifField.GPS_LONGITUDE,        "11/1,34/1,0/100")
                .put(ExifField.GPS_LONGITUDE_REF,    "E")
                .put(ExifField.GPS_ALTITUDE,         "520.00 m")
                .put(ExifField.GPS_ALTITUDE_REF,     "0")
                .put(ExifField.GPS_DATESTAMP,        "2024:06:01")
                .put(ExifField.GPS_SPEED,            "0.0")
                .put(ExifField.GPS_SPEED_REF,        "K")
                .put(ExifField.GPS_IMG_DIRECTION,    "180.0")
                .put(ExifField.GPS_IMG_DIRECTION_REF,"T")
                .put(ExifField.BODY_SERIAL_NUMBER,   "12345678")
                .put(ExifField.LENS_SERIAL_NUMBER,   "87654321")
                .put(ExifField.ARTIST,               "Max Mustermann")
                .put(ExifField.COPYRIGHT,            "© 2024")
                .put(ExifField.IMAGE_DESCRIPTION,    "A photo")
                .put(ExifField.USER_COMMENT,         "private note")
                .put(ExifField.DATE_TIME_ORIGINAL,   "2024:06:01 12:00:00")
                .put(ExifField.DATE_TIME_DIGITIZED,  "2024:06:01 12:00:00")
                .put(ExifField.DATE_TIME,            "2024:06:01 12:00:00")
                .put(ExifField.MAKE,                 "Canon")
                .put(ExifField.MODEL,                "EOS R5")
                .build();

        ExifData stripped = ExifSanitizer.strip(data);

        assertFalse(stripped.has(ExifField.GPS_LATITUDE));
        assertFalse(stripped.has(ExifField.GPS_LONGITUDE));
        assertFalse(stripped.has(ExifField.GPS_ALTITUDE));
        assertFalse(stripped.has(ExifField.BODY_SERIAL_NUMBER));
        assertFalse(stripped.has(ExifField.LENS_SERIAL_NUMBER));
        assertFalse(stripped.has(ExifField.ARTIST));
        assertFalse(stripped.has(ExifField.COPYRIGHT));
        assertFalse(stripped.has(ExifField.USER_COMMENT));
        assertFalse(stripped.has(ExifField.DATE_TIME_ORIGINAL));
    }

    @Test
    void strip_preservesNonSensitiveFields() {
        ExifData data = ExifData.builder()
                .put(ExifField.MAKE,  "Sony")
                .put(ExifField.MODEL, "A7R V")
                .put(ExifField.ISO,   "400")
                .build();

        ExifData stripped = ExifSanitizer.strip(data);

        assertTrue(stripped.has(ExifField.MAKE));
        assertTrue(stripped.has(ExifField.MODEL));
        assertTrue(stripped.has(ExifField.ISO));
    }

    @Test
    void strip_onEmptyData_returnsEmptyData() {
        ExifData stripped = ExifSanitizer.strip(ExifData.builder().build());
        assertTrue(stripped.isEmpty());
    }

    @Test
    void stripAll_returnsEmptyData_regardless() {
        ExifData data = ExifData.builder()
                .put(ExifField.MAKE,  "Fujifilm")
                .put(ExifField.MODEL, "X-T5")
                .build();

        ExifData result = ExifSanitizer.stripAll(data);
        assertTrue(result.isEmpty());
    }

    @Test
    void hasSensitiveData_returnsTrueWhenSensitiveFieldPresent() {
        ExifData data = ExifData.builder()
                .put(ExifField.GPS_LATITUDE, "48/1,8/1,0/100")
                .build();

        assertTrue(ExifSanitizer.hasSensitiveData(data));
    }

    @Test
    void hasSensitiveData_returnsFalseWhenNoSensitiveFields() {
        ExifData data = ExifData.builder()
                .put(ExifField.MAKE,  "Leica")
                .put(ExifField.MODEL, "M11")
                .build();

        assertFalse(ExifSanitizer.hasSensitiveData(data));
    }

    @Test
    void sensitiveFields_isNotEmpty() {
        Set<ExifField> fields = ExifSanitizer.sensitiveFields();
        assertFalse(fields.isEmpty());
    }

    @Test
    void sensitiveFields_returnsDefensiveCopy() {
        Set<ExifField> a = ExifSanitizer.sensitiveFields();
        Set<ExifField> b = ExifSanitizer.sensitiveFields();
        assertNotSame(a, b);
    }
}
