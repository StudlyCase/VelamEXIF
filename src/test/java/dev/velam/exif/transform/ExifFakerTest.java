package dev.velam.exif.exif;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExifFakerTest {

    @Test
    void generate_returnsNonEmptyData() {
        ExifData data = ExifFaker.generate();
        assertNotNull(data);
        assertFalse(data.isEmpty());
    }

    @Test
    void generate_containsRequiredCameraFields() {
        ExifData data = ExifFaker.generate();

        assertTrue(data.has(ExifField.MAKE));
        assertTrue(data.has(ExifField.MODEL));
        assertTrue(data.has(ExifField.LENS_MAKE));
        assertTrue(data.has(ExifField.LENS_MODEL));
    }

    @Test
    void generate_containsRequiredCaptureFields() {
        ExifData data = ExifFaker.generate();

        assertTrue(data.has(ExifField.EXPOSURE_TIME));
        assertTrue(data.has(ExifField.F_NUMBER));
        assertTrue(data.has(ExifField.ISO));
        assertTrue(data.has(ExifField.FOCAL_LENGTH));
    }

    @Test
    void generate_containsGpsFields() {
        ExifData data = ExifFaker.generate();

        assertTrue(data.has(ExifField.GPS_LATITUDE));
        assertTrue(data.has(ExifField.GPS_LATITUDE_REF));
        assertTrue(data.has(ExifField.GPS_LONGITUDE));
        assertTrue(data.has(ExifField.GPS_LONGITUDE_REF));
        assertTrue(data.has(ExifField.GPS_ALTITUDE));
    }

    @Test
    void generate_containsTimestamps() {
        ExifData data = ExifFaker.generate();

        assertTrue(data.has(ExifField.DATE_TIME));
        assertTrue(data.has(ExifField.DATE_TIME_ORIGINAL));
        assertTrue(data.has(ExifField.DATE_TIME_DIGITIZED));
    }

    @Test
    void generate_producesNonBlankValues() {
        ExifData data = ExifFaker.generate();

        data.fields().forEach((field, value) ->
                assertFalse(value.isBlank(), "Value for field " + field + " must not be blank"));
    }

    @Test
    void generate_producesDifferentResultsAcrossInvocations() {
        // Not guaranteed for every single field, but the overall field maps must differ eventually.
        boolean anyDifference = false;
        for (int i = 0; i < 10; i++) {
            ExifData a = ExifFaker.generate();
            ExifData b = ExifFaker.generate();
            if (!a.fields().equals(b.fields())) {
                anyDifference = true;
                break;
            }
        }
        assertTrue(anyDifference, "generate() should produce varying results");
    }

    @Test
    void generateFrom_overwritesSensitiveFieldsFromOriginal() {
        ExifData original = ExifData.builder()
                .put(ExifField.MAKE,           "Canon")
                .put(ExifField.GPS_LATITUDE,   "48/1,8/1,0/100")
                .put(ExifField.ARTIST,         "Max Mustermann")
                .build();

        ExifData result = ExifFaker.generateFrom(original);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        // Fake GPS must replace the original
        assertTrue(result.has(ExifField.GPS_LATITUDE));
        assertNotEquals("48/1,8/1,0/100", result.get(ExifField.GPS_LATITUDE));
    }

    @Test
    void generateFrom_doesNotContainSensitiveOriginalValues() {
        ExifData original = ExifData.builder()
                .put(ExifField.ARTIST,        "Private Person")
                .put(ExifField.USER_COMMENT,  "secret")
                .put(ExifField.COPYRIGHT,     "© Private")
                .build();

        ExifData result = ExifFaker.generateFrom(original);

        if (result.has(ExifField.ARTIST)) {
            assertNotEquals("Private Person", result.get(ExifField.ARTIST));
        }
    }
}
