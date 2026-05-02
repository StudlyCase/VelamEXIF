package dev.velam.exif.cli;

import dev.velam.exif.model.ExifData;
import dev.velam.exif.model.ExifField;

import java.util.Map;

public final class Printer {

    private static final String RESET  = "\033[0m";
    private static final String BOLD   = "\033[1m";
    private static final String DIM    = "\033[2m";
    private static final String GREEN  = "\033[32m";
    private static final String YELLOW = "\033[33m";
    private static final String RED    = "\033[31m";
    private static final String CYAN   = "\033[36m";

    private Printer() {}

    public static void printBanner() {
        System.out.println(BOLD + CYAN);
        System.out.println("  ██╗   ██╗███████╗██╗      █████╗ ███╗   ███╗");
        System.out.println("  ██║   ██║██╔════╝██║     ██╔══██╗████╗ ████║");
        System.out.println("  ██║   ██║█████╗  ██║     ███████║██╔████╔██║");
        System.out.println("  ╚██╗ ██╔╝██╔══╝  ██║     ██╔══██║██║╚██╔╝██║");
        System.out.println("   ╚████╔╝ ███████╗███████╗██║  ██║██║ ╚═╝ ██║");
        System.out.println("    ╚═══╝  ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝");
        System.out.println("  EXIF  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
        System.out.println();
    }

    public static void printExifTable(String label, ExifData data) {
        System.out.println(BOLD + "  " + label + RESET);
        System.out.println(DIM + "  " + "─".repeat(52) + RESET);

        Map<ExifField, String> fields = data.fields();
        if (fields.isEmpty()) {
            System.out.println(DIM + "  (no data)" + RESET);
        } else {
            for (Map.Entry<ExifField, String> entry : fields.entrySet()) {
                String key   = String.format("%-28s", entry.getKey().tagName());
                String value = entry.getValue();
                System.out.println("  " + DIM + key + RESET + "  " + value);
            }
        }

        System.out.println();
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + "  ✔  " + RESET + message);
    }

    public static void printWarning(String message) {
        System.out.println(YELLOW + "  ⚠  " + RESET + message);
    }

    public static void printError(String message) {
        System.out.println(RED + "  ✖  " + RESET + message);
    }

    public static void printInfo(String message) {
        System.out.println(CYAN + "  ℹ  " + RESET + message);
    }

    public static void printSeparator() {
        System.out.println(DIM + "  " + "─".repeat(52) + RESET);
    }

    public static void printNewline() {
        System.out.println();
    }
}
