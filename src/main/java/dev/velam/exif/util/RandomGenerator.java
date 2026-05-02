package dev.velam.exif.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomGenerator {

    private static final DateTimeFormatter EXIF_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    private static final String[] CAMERA_MAKES = {
        "Canon", "Nikon", "Sony", "Fujifilm", "Panasonic",
        "Olympus", "Leica", "Pentax", "Ricoh", "Hasselblad"
    };

    private static final String[] CAMERA_MODELS = {
        "EOS R5", "Z7 II", "A7R V", "X-T5", "S5 II",
        "OM-5", "M11", "K-3 III", "GR IIIx", "X2D 100C"
    };

    private static final String[] LENS_MAKES = {
        "Canon", "Nikon", "Sony", "Sigma", "Tamron",
        "Zeiss", "Voigtlander", "Tokina", "Samyang", "Laowa"
    };

    private static final String[] LENS_MODELS = {
        "24-70mm f/2.8", "50mm f/1.4", "85mm f/1.8",
        "16-35mm f/4", "70-200mm f/2.8", "35mm f/2",
        "100mm f/2.8 Macro", "14mm f/1.8", "24mm f/1.4", "135mm f/1.8"
    };

    private static final String[] SOFTWARE_VERSIONS = {
        "Lightroom 7.0", "Capture One 23", "darktable 4.4",
        "RawTherapee 5.9", "Photoshop 25.0", "Luminar Neo 1.14"
    };

    private RandomGenerator() {}

    public static String cameraMake() {
        return randomFrom(CAMERA_MAKES);
    }

    public static String cameraModel() {
        return randomFrom(CAMERA_MODELS);
    }

    public static String lensMake() {
        return randomFrom(LENS_MAKES);
    }

    public static String lensModel() {
        return randomFrom(LENS_MODELS);
    }

    public static String software() {
        return randomFrom(SOFTWARE_VERSIONS);
    }

    public static String exposureTime() {
        int[] denominators = {30, 60, 100, 125, 250, 500, 1000, 2000, 4000};
        return "1/" + randomFrom(denominators);
    }

    public static String fNumber() {
        double[] apertures = {1.4, 1.8, 2.0, 2.8, 4.0, 5.6, 8.0, 11.0, 16.0};
        return String.format("f/%.1f", randomFrom(apertures));
    }

    public static String iso() {
        int[] isoValues = {100, 200, 400, 800, 1600, 3200, 6400, 12800};
        return String.valueOf(randomFrom(isoValues));
    }

    public static String focalLength() {
        int[] lengths = {14, 20, 24, 28, 35, 50, 85, 100, 135, 200};
        return randomFrom(lengths) + " mm";
    }

    public static String dateTime() {
        LocalDateTime base = LocalDateTime.now();
        long offsetDays = ThreadLocalRandom.current().nextLong(0, 365 * 5L);
        long offsetSeconds = ThreadLocalRandom.current().nextLong(0, 86400L);
        return base.minusDays(offsetDays).minusSeconds(offsetSeconds).format(EXIF_DATE_FORMAT);
    }

    public static String gpsLatitude() {
        double lat = ThreadLocalRandom.current().nextDouble(-90.0, 90.0);
        return formatDms(Math.abs(lat));
    }

    public static String gpsLatitudeRef(String latitude) {
        return latitude.startsWith("-") ? "S" : "N";
    }

    public static String gpsLongitude() {
        double lon = ThreadLocalRandom.current().nextDouble(-180.0, 180.0);
        return formatDms(Math.abs(lon));
    }

    public static String gpsLongitudeRef(String longitude) {
        return longitude.startsWith("-") ? "W" : "E";
    }

    public static String gpsAltitude() {
        double alt = ThreadLocalRandom.current().nextDouble(0.0, 4000.0);
        return String.format("%.2f m", alt);
    }

    public static String serialNumber() {
        return String.format("%08d", ThreadLocalRandom.current().nextInt(10_000_000, 99_999_999));
    }

    public static int nextInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static double nextDouble(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    private static String formatDms(double decimal) {
        int degrees = (int) decimal;
        double minutesDecimal = (decimal - degrees) * 60;
        int minutes = (int) minutesDecimal;
        double seconds = (minutesDecimal - minutes) * 60;
        return String.format("%d/1,%d/1,%d/100", degrees, minutes, (int) (seconds * 100));
    }

    private static <T> T randomFrom(T[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    private static int randomFrom(int[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    private static double randomFrom(double[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
}
