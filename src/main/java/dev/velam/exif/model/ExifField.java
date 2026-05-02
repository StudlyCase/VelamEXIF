package dev.velam.exif.model;

public enum ExifField {

    // Camera
    MAKE("Make"),
    MODEL("Model"),
    SOFTWARE("Software"),
    LENS_MAKE("LensMake"),
    LENS_MODEL("LensModel"),

    // Image
    IMAGE_WIDTH("ImageWidth"),
    IMAGE_HEIGHT("ImageHeight"),
    ORIENTATION("Orientation"),
    X_RESOLUTION("XResolution"),
    Y_RESOLUTION("YResolution"),
    COLOR_SPACE("ColorSpace"),

    // Capture settings
    EXPOSURE_TIME("ExposureTime"),
    F_NUMBER("FNumber"),
    ISO("PhotographicSensitivity"),
    FOCAL_LENGTH("FocalLength"),
    FLASH("Flash"),
    WHITE_BALANCE("WhiteBalance"),
    EXPOSURE_PROGRAM("ExposureProgram"),
    METERING_MODE("MeteringMode"),

    // Time
    DATE_TIME_ORIGINAL("DateTimeOriginal"),
    DATE_TIME_DIGITIZED("DateTimeDigitized"),
    DATE_TIME("DateTime"),

    // Location
    GPS_LATITUDE("GPSLatitude"),
    GPS_LATITUDE_REF("GPSLatitudeRef"),
    GPS_LONGITUDE("GPSLongitude"),
    GPS_LONGITUDE_REF("GPSLongitudeRef"),
    GPS_ALTITUDE("GPSAltitude"),
    GPS_ALTITUDE_REF("GPSAltitudeRef"),
    GPS_TIMESTAMP("GPSTimeStamp"),
    GPS_DATESTAMP("GPSDateStamp"),
    GPS_SPEED("GPSSpeed"),
    GPS_SPEED_REF("GPSSpeedRef"),
    GPS_IMG_DIRECTION("GPSImgDirection"),
    GPS_IMG_DIRECTION_REF("GPSImgDirectionRef"),

    // Author / rights
    ARTIST("Artist"),
    COPYRIGHT("Copyright"),
    IMAGE_DESCRIPTION("ImageDescription"),
    USER_COMMENT("UserComment"),

    // Device identifiers
    CAMERA_SERIAL_NUMBER("CameraSerialNumber"),
    BODY_SERIAL_NUMBER("BodySerialNumber"),
    LENS_SERIAL_NUMBER("LensSerialNumber");

    private final String tagName;

    ExifField(String tagName) {
        this.tagName = tagName;
    }

    public String tagName() {
        return tagName;
    }
}
