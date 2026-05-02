package dev.velam.exif.exif;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;

import java.util.EnumSet;
import java.util.Set;

public final class ExifSanitizer {

    private static final Set<ExifField> SENSITIVE_FIELDS = EnumSet.of(
        ExifField.GPS_LATITUDE,
        ExifField.GPS_LATITUDE_REF,
        ExifField.GPS_LONGITUDE,
        ExifField.GPS_LONGITUDE_REF,
        ExifField.GPS_ALTITUDE,
        ExifField.GPS_ALTITUDE_REF,
        ExifField.GPS_TIMESTAMP,
        ExifField.GPS_DATESTAMP,
        ExifField.GPS_SPEED,
        ExifField.GPS_SPEED_REF,
        ExifField.GPS_IMG_DIRECTION,
        ExifField.GPS_IMG_DIRECTION_REF,
        ExifField.CAMERA_SERIAL_NUMBER,
        ExifField.BODY_SERIAL_NUMBER,
        ExifField.LENS_SERIAL_NUMBER,
        ExifField.ARTIST,
        ExifField.COPYRIGHT,
        ExifField.IMAGE_DESCRIPTION,
        ExifField.USER_COMMENT,
        ExifField.DATE_TIME_ORIGINAL,
        ExifField.DATE_TIME_DIGITIZED,
        ExifField.DATE_TIME
    );

    private ExifSanitizer() {}

    public static ExifData strip(ExifData data) {
        ExifData.Builder builder = ExifData.builder();

        for (ExifField field : ExifField.values()) {
            if (!SENSITIVE_FIELDS.contains(field) && data.has(field)) {
                builder.put(field, data.get(field));
            }
        }

        return builder.build();
    }

    public static ExifData stripAll(ExifData data) {
        return ExifData.builder().build();
    }

    public static boolean hasSensitiveData(ExifData data) {
        for (ExifField field : SENSITIVE_FIELDS) {
            if (data.has(field)) return true;
        }
        return false;
    }

    public static Set<ExifField> sensitiveFields() {
        return EnumSet.copyOf(SENSITIVE_FIELDS);
    }
}
