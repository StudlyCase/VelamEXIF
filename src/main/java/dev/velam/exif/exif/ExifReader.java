package dev.velam.exif.exif;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ExifReader {

    private static final Map<ExifField, TagInfo> TAG_MAP = buildTagMap();

    private ExifReader() {}

    public static ExifData read(Path path) throws IOException {
        try {
            ImageMetadata metadata = Imaging.getMetadata(path.toFile());
            if (!(metadata instanceof JpegImageMetadata jpegMetadata)) {
                return ExifData.builder().build();
            }

            ExifData.Builder builder = ExifData.builder();

            for (Map.Entry<ExifField, TagInfo> entry : TAG_MAP.entrySet()) {
                TiffField field = jpegMetadata.findEXIFValueWithExactMatch(entry.getValue());
                if (field != null) {
                    String value = field.getValueDescription();
                    if (value != null) {
                        builder.put(entry.getKey(), value.replaceAll("^'|'$", "").trim());
                    }
                }
            }

            return builder.build();

        } catch (Exception e) {
            throw new IOException("Failed to read EXIF data from: " + path, e);
        }
    }

    private static Map<ExifField, TagInfo> buildTagMap() {
        Map<ExifField, TagInfo> map = new LinkedHashMap<>();

        map.put(ExifField.MAKE,                    TiffTagConstants.TIFF_TAG_MAKE);
        map.put(ExifField.MODEL,                   TiffTagConstants.TIFF_TAG_MODEL);
        map.put(ExifField.SOFTWARE,                TiffTagConstants.TIFF_TAG_SOFTWARE);
        map.put(ExifField.ORIENTATION,             TiffTagConstants.TIFF_TAG_ORIENTATION);
        map.put(ExifField.IMAGE_WIDTH,             TiffTagConstants.TIFF_TAG_IMAGE_WIDTH);
        map.put(ExifField.IMAGE_HEIGHT,            TiffTagConstants.TIFF_TAG_IMAGE_LENGTH);
        map.put(ExifField.X_RESOLUTION,            TiffTagConstants.TIFF_TAG_XRESOLUTION);
        map.put(ExifField.Y_RESOLUTION,            TiffTagConstants.TIFF_TAG_YRESOLUTION);
        map.put(ExifField.DATE_TIME,               TiffTagConstants.TIFF_TAG_DATE_TIME);
        map.put(ExifField.ARTIST,                  TiffTagConstants.TIFF_TAG_ARTIST);
        map.put(ExifField.COPYRIGHT,               TiffTagConstants.TIFF_TAG_COPYRIGHT);

        map.put(ExifField.EXPOSURE_TIME,           ExifTagConstants.EXIF_TAG_EXPOSURE_TIME);
        map.put(ExifField.F_NUMBER,                ExifTagConstants.EXIF_TAG_FNUMBER);
        map.put(ExifField.ISO,                     ExifTagConstants.EXIF_TAG_ISO);
        map.put(ExifField.DATE_TIME_ORIGINAL,      ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
        map.put(ExifField.DATE_TIME_DIGITIZED,     ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
        map.put(ExifField.FOCAL_LENGTH,            ExifTagConstants.EXIF_TAG_FOCAL_LENGTH);
        map.put(ExifField.FLASH,                   ExifTagConstants.EXIF_TAG_FLASH);
        map.put(ExifField.WHITE_BALANCE,           ExifTagConstants.EXIF_TAG_WHITE_BALANCE_1);
        map.put(ExifField.EXPOSURE_PROGRAM,        ExifTagConstants.EXIF_TAG_EXPOSURE_PROGRAM);
        map.put(ExifField.METERING_MODE,           ExifTagConstants.EXIF_TAG_METERING_MODE);
        map.put(ExifField.COLOR_SPACE,             ExifTagConstants.EXIF_TAG_COLOR_SPACE);
        map.put(ExifField.LENS_MAKE,               ExifTagConstants.EXIF_TAG_LENS_MAKE);
        map.put(ExifField.LENS_MODEL,              ExifTagConstants.EXIF_TAG_LENS_MODEL);
        map.put(ExifField.BODY_SERIAL_NUMBER,      ExifTagConstants.EXIF_TAG_BODY_SERIAL_NUMBER);
        map.put(ExifField.LENS_SERIAL_NUMBER,      ExifTagConstants.EXIF_TAG_LENS_SERIAL_NUMBER);
        map.put(ExifField.IMAGE_DESCRIPTION,       TiffTagConstants.TIFF_TAG_IMAGE_DESCRIPTION);
        map.put(ExifField.USER_COMMENT,            ExifTagConstants.EXIF_TAG_USER_COMMENT);

        map.put(ExifField.GPS_LATITUDE,            GpsTagConstants.GPS_TAG_GPS_LATITUDE);
        map.put(ExifField.GPS_LATITUDE_REF,        GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
        map.put(ExifField.GPS_LONGITUDE,           GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
        map.put(ExifField.GPS_LONGITUDE_REF,       GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
        map.put(ExifField.GPS_ALTITUDE,            GpsTagConstants.GPS_TAG_GPS_ALTITUDE);
        map.put(ExifField.GPS_ALTITUDE_REF,        GpsTagConstants.GPS_TAG_GPS_ALTITUDE_REF);
        map.put(ExifField.GPS_TIMESTAMP,           GpsTagConstants.GPS_TAG_GPS_TIME_STAMP);
        map.put(ExifField.GPS_DATESTAMP,           GpsTagConstants.GPS_TAG_GPS_DATE_STAMP);
        map.put(ExifField.GPS_SPEED,               GpsTagConstants.GPS_TAG_GPS_SPEED);
        map.put(ExifField.GPS_SPEED_REF,           GpsTagConstants.GPS_TAG_GPS_SPEED_REF);
        map.put(ExifField.GPS_IMG_DIRECTION,       GpsTagConstants.GPS_TAG_GPS_IMG_DIRECTION);
        map.put(ExifField.GPS_IMG_DIRECTION_REF,   GpsTagConstants.GPS_TAG_GPS_IMG_DIRECTION_REF);

        return map;
    }
}
