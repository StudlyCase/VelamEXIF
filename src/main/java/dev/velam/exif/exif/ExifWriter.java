package dev.velam.exif.exif;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;

import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExifWriter {

    private ExifWriter() {}

    public static void write(Path source, Path destination, ExifData data) throws IOException {
        try {
            TiffOutputSet outputSet = existingOutputSet(source);

            applyFields(outputSet, data);

            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(destination))) {
                new ExifRewriter().updateExifMetadataLossless(source.toFile(), os, outputSet);
            }

        } catch (Exception e) {
            throw new IOException("Failed to write EXIF data to: " + destination, e);
        }
    }

    public static void writeInPlace(Path path, ExifData data) throws IOException {
        Path temp = path.resolveSibling(path.getFileName() + ".tmp");
        try {
            write(path, temp, data);
            Files.move(temp, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Files.deleteIfExists(temp);
            throw e;
        }
    }

    private static TiffOutputSet existingOutputSet(Path source) throws Exception {
        ImageMetadata metadata = Imaging.getMetadata(source.toFile());
        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            TiffOutputSet existing = jpegMetadata.getExif().getOutputSet();
            if (existing != null) return existing;
        }
        return new TiffOutputSet();
    }

    private static void applyFields(TiffOutputSet outputSet, ExifData data) throws Exception {
        TiffOutputDirectory root = outputSet.getOrCreateRootDirectory();
        TiffOutputDirectory exif = outputSet.getOrCreateExifDirectory();
        TiffOutputDirectory gps  = outputSet.getOrCreateGPSDirectory();

        for (ExifField field : ExifField.values()) {
            if (!data.has(field)) continue;
            String value = data.get(field);
            writeField(root, exif, gps, field, value);
        }
    }

    private static void writeField(
            TiffOutputDirectory root,
            TiffOutputDirectory exif,
            TiffOutputDirectory gps,
            ExifField field,
            String value) throws Exception {

        switch (field) {
            case MAKE                  -> replaceField(root, TiffTagConstants.TIFF_TAG_MAKE, value);
            case MODEL                 -> replaceField(root, TiffTagConstants.TIFF_TAG_MODEL, value);
            case SOFTWARE              -> replaceField(root, TiffTagConstants.TIFF_TAG_SOFTWARE, value);
            case ARTIST                -> replaceField(root, TiffTagConstants.TIFF_TAG_ARTIST, value);
            case COPYRIGHT             -> replaceField(root, TiffTagConstants.TIFF_TAG_COPYRIGHT, value);
            case IMAGE_DESCRIPTION     -> replaceField(root, TiffTagConstants.TIFF_TAG_IMAGE_DESCRIPTION, value);
            case DATE_TIME             -> replaceField(root, TiffTagConstants.TIFF_TAG_DATE_TIME, value);

            case EXPOSURE_TIME         -> replaceField(exif, ExifTagConstants.EXIF_TAG_EXPOSURE_TIME, value);
            case F_NUMBER              -> replaceField(exif, ExifTagConstants.EXIF_TAG_FNUMBER, value);
            case ISO                   -> replaceField(exif, ExifTagConstants.EXIF_TAG_ISO, value);
            case DATE_TIME_ORIGINAL    -> replaceField(exif, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, value);
            case DATE_TIME_DIGITIZED   -> replaceField(exif, ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED, value);
            case FOCAL_LENGTH          -> replaceField(exif, ExifTagConstants.EXIF_TAG_FOCAL_LENGTH, value);
            case FLASH                 -> replaceField(exif, ExifTagConstants.EXIF_TAG_FLASH, value);
            case WHITE_BALANCE         -> replaceField(exif, ExifTagConstants.EXIF_TAG_WHITE_BALANCE_1, value);
            case EXPOSURE_PROGRAM      -> replaceField(exif, ExifTagConstants.EXIF_TAG_EXPOSURE_PROGRAM, value);
            case METERING_MODE         -> replaceField(exif, ExifTagConstants.EXIF_TAG_METERING_MODE, value);
            case COLOR_SPACE           -> replaceField(exif, ExifTagConstants.EXIF_TAG_COLOR_SPACE, value);
            case LENS_MAKE             -> replaceField(exif, ExifTagConstants.EXIF_TAG_LENS_MAKE, value);
            case LENS_MODEL            -> replaceField(exif, ExifTagConstants.EXIF_TAG_LENS_MODEL, value);
            case BODY_SERIAL_NUMBER    -> replaceField(exif, ExifTagConstants.EXIF_TAG_BODY_SERIAL_NUMBER, value);
            case LENS_SERIAL_NUMBER    -> replaceField(exif, ExifTagConstants.EXIF_TAG_LENS_SERIAL_NUMBER, value);
            case USER_COMMENT          -> replaceField(exif, ExifTagConstants.EXIF_TAG_USER_COMMENT, value);

            case GPS_LATITUDE_REF      -> replaceField(gps, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF, value);
            case GPS_LONGITUDE_REF     -> replaceField(gps, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, value);
            case GPS_ALTITUDE_REF      -> replaceField(gps, GpsTagConstants.GPS_TAG_GPS_ALTITUDE_REF, value);
            case GPS_DATESTAMP         -> replaceField(gps, GpsTagConstants.GPS_TAG_GPS_DATE_STAMP, value);
            case GPS_SPEED_REF         -> replaceField(gps, GpsTagConstants.GPS_TAG_GPS_SPEED_REF, value);
            case GPS_IMG_DIRECTION_REF -> replaceField(gps, GpsTagConstants.GPS_TAG_GPS_IMG_DIRECTION_REF, value);

            default -> {}
        }
    }

    private static void replaceField(TiffOutputDirectory dir, org.apache.commons.imaging.formats.tiff.taginfos.TagInfo tag, String value) throws Exception {
        dir.removeField(tag);
        dir.add(tag, value);
    }
}
