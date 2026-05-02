package dev.velam.exif.exif;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;
import dev.velam.exif.util.RandomGenerator;

public final class ExifFaker {

    private ExifFaker() {}

    public static ExifData generate() {
        String latitude  = RandomGenerator.gpsLatitude();
        String longitude = RandomGenerator.gpsLongitude();
        String dateTime  = RandomGenerator.dateTime();

        return ExifData.builder()
            .put(ExifField.MAKE,                   RandomGenerator.cameraMake())
            .put(ExifField.MODEL,                  RandomGenerator.cameraModel())
            .put(ExifField.SOFTWARE,               RandomGenerator.software())
            .put(ExifField.LENS_MAKE,              RandomGenerator.lensMake())
            .put(ExifField.LENS_MODEL,             RandomGenerator.lensModel())
            .put(ExifField.EXPOSURE_TIME,          RandomGenerator.exposureTime())
            .put(ExifField.F_NUMBER,               RandomGenerator.fNumber())
            .put(ExifField.ISO,                    RandomGenerator.iso())
            .put(ExifField.FOCAL_LENGTH,           RandomGenerator.focalLength())
            .put(ExifField.DATE_TIME_ORIGINAL,     dateTime)
            .put(ExifField.DATE_TIME_DIGITIZED,    dateTime)
            .put(ExifField.DATE_TIME,              dateTime)
            .put(ExifField.GPS_LATITUDE,           latitude)
            .put(ExifField.GPS_LATITUDE_REF,       RandomGenerator.gpsLatitudeRef(latitude))
            .put(ExifField.GPS_LONGITUDE,          longitude)
            .put(ExifField.GPS_LONGITUDE_REF,      RandomGenerator.gpsLongitudeRef(longitude))
            .put(ExifField.GPS_ALTITUDE,           RandomGenerator.gpsAltitude())
            .put(ExifField.GPS_ALTITUDE_REF,       "0")
            .put(ExifField.BODY_SERIAL_NUMBER,     RandomGenerator.serialNumber())
            .put(ExifField.LENS_SERIAL_NUMBER,     RandomGenerator.serialNumber())
            .build();
    }

    public static ExifData generateFrom(ExifData original) {
        ExifData fake = generate();
        ExifData stripped = ExifSanitizer.strip(original);

        ExifData.Builder builder = ExifData.builder();
        builder.putAll(stripped.fields());
        builder.putAll(fake.fields());

        return builder.build();
    }
}
