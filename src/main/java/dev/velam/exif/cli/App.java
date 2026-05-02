package dev.velam.exif.cli;

import dev.velam.exif.exif.ExifFaker;
import dev.velam.exif.exif.ExifReader;
import dev.velam.exif.exif.ExifSanitizer;
import dev.velam.exif.exif.ExifWriter;
import dev.velam.exif.model.ExifData;
import dev.velam.exif.util.FileValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String VERSION = "1.0.0";

    private boolean fake = false;
    private boolean strip = false;
    private boolean verbose = false;
    private boolean dryRun = false;
    private final List<File> inputFiles = new ArrayList<>();

    public void run(String[] args) {
        if (args.length == 0) {
            Printer.usage();
            return;
        }

        try {
            parseArgs(args);
        } catch (IllegalArgumentException e) {
            Printer.error(e.getMessage());
            Printer.usage();
            return;
        }

        if (inputFiles.isEmpty()) {
            Printer.error("No input files specified.");
            Printer.usage();
            return;
        }

        for (File file : inputFiles) {
            process(file);
        }
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-f", "--fake"    -> fake = true;
                case "-s", "--strip"   -> strip = true;
                case "-v", "--verbose" -> verbose = true;
                case "-n", "--dry-run" -> dryRun = true;
                case "--version"       -> { Printer.version(VERSION); System.exit(0); }
                case "-h", "--help"    -> { Printer.usage(); System.exit(0); }
                default -> {
                    if (args[i].startsWith("-")) {
                        throw new IllegalArgumentException("Unknown option: " + args[i]);
                    }
                    inputFiles.add(new File(args[i]));
                }
            }
        }

        if (!fake && !strip) {
            throw new IllegalArgumentException("Specify at least one mode: --fake or --strip.");
        }

        if (fake && strip) {
            throw new IllegalArgumentException("--fake and --strip are mutually exclusive.");
        }
    }

    private void process(File file) {
        Printer.fileHeader(file.getName());

        try {
            FileValidator.validate(file);
        } catch (IllegalArgumentException e) {
            Printer.error(e.getMessage());
            return;
        }

        try {
            ExifData original = ExifReader.read(file);

            if (verbose) {
                Printer.exifData("Original EXIF", original);
            }

            ExifData result = fake
                    ? ExifFaker.generate(original)
                    : ExifSanitizer.strip(original);

            if (verbose) {
                Printer.exifData(fake ? "Faked EXIF" : "Stripped EXIF", result);
            }

            if (!dryRun) {
                ExifWriter.write(file, result);
                Printer.success(file.getName());
            } else {
                Printer.dryRun(file.getName());
            }

        } catch (Exception e) {
            Printer.error("Failed to process " + file.getName() + ": " + e.getMessage());
        }
    }
}
