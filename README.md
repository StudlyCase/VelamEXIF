# VelamEXIF

A command-line tool that replaces EXIF metadata in JPEG images with randomly generated fake data before you share photos online.

## Why

Every JPEG you take contains hidden metadata — GPS coordinates, device model, timestamps, and more. VelamEXIF strips that data and replaces it with convincing but entirely fabricated values, so you can share photos without exposing where you were, what device you used, or when the shot was taken.

## Features

- Reads all existing EXIF fields from a JPEG
- Strips sensitive metadata
- Writes randomized fake values in place of real ones
- Validates that the input is a well-formed JPEG before touching it
- Terminal UI with formatted output

## Requirements

- Java 17 or higher
- Maven 3.8+

## Build

```bash
mvn clean package
```

This produces a runnable JAR in `target/`.

## Usage

```bash
java -jar velamexif.jar <image.jpg>
```

The original file is overwritten with the sanitized version. Make a copy first if you want to keep the original.

## Project Structure

```
src/main/java/dev/velam/exif/
├── Main.java
├── cli/
│   ├── App.java
│   └── Printer.java
├── exif/
│   ├── ExifReader.java
│   ├── ExifWriter.java
│   ├── ExifSanitizer.java
│   └── ExifFaker.java
├── model/
│   ├── ExifData.java
│   └── ExifField.java
└── util/
    ├── FileValidator.java
    └── RandomGenerator.java
```

## License

CC0 1.0 Universal — public domain. See [LICENSE](LICENSE).
