package dev.velam.exif.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class ExifData {

    private final Map<ExifField, String> fields;

    private ExifData(Map<ExifField, String> fields) {
        this.fields = Collections.unmodifiableMap(new EnumMap<>(fields));
    }

    public Map<ExifField, String> fields() {
        return fields;
    }

    public String get(ExifField field) {
        return fields.get(field);
    }

    public boolean has(ExifField field) {
        return fields.containsKey(field);
    }

    public boolean isEmpty() {
        return fields.isEmpty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Map<ExifField, String> fields = new EnumMap<>(ExifField.class);

        private Builder() {}

        public Builder put(ExifField field, String value) {
            if (value != null && !value.isBlank()) {
                fields.put(field, value);
            }
            return this;
        }

        public Builder putAll(Map<ExifField, String> entries) {
            entries.forEach(this::put);
            return this;
        }

        public ExifData build() {
            return new ExifData(fields);
        }
    }
}
