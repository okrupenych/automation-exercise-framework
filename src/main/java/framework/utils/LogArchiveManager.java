package framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class LogArchiveManager {

    private static final Logger log = LoggerFactory.getLogger(LogArchiveManager.class);

    private static final Path LOG_DIR = Paths.get("logs");
    private static final Path ARCHIVE_DIR = LOG_DIR.resolve("archive");

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String LOG_PREFIX = "framework-";
    private static final String LOG_EXTENSION = ".log";

    private static final int RETENTION_DAYS = 30;
    private static final long MAX_ARCHIVE_SIZE = 500L * 1024 * 1024;

    private static boolean isArchived(Path archivePath, Path logFile) throws IOException {

        URI archiveUri = URI.create("jar:" + archivePath.toUri());

        try (FileSystem zip = FileSystems.newFileSystem(archiveUri, Map.of())) {

            Path archivedFile = zip.getPath("/" + logFile.getFileName());

            if (!Files.isRegularFile(archivedFile)) {
                return false;
            }

            return Files.mismatch(archivedFile, logFile) == -1;
        }
    }

    public static Map<LocalDate, List<Path>> findOldLogs() throws IOException {

        Map<LocalDate, List<Path>> groupedLogs = new TreeMap<>();

        if (!Files.isDirectory(LOG_DIR)) {
            return groupedLogs;
        }

        try (Stream<Path> files = Files.list(LOG_DIR)) {

            files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(LOG_PREFIX))
                    .filter(path -> path.getFileName().toString().endsWith(LOG_EXTENSION))
                    .forEach(path -> {

                        String filename = path.getFileName().toString();

                        try {
                            LocalDate date = LocalDate.parse(
                                    filename.substring(10, 20),
                                    DATE_FORMAT
                            );

                            if (date.isBefore(LocalDate.now())) {
                                groupedLogs
                                        .computeIfAbsent(date, key -> new ArrayList<>())
                                        .add(path);
                            }

                        } catch (RuntimeException e) {
                            log.warn("Skipping log file with invalid name: {}", filename);
                        }
                    });
        }

        return groupedLogs;
    }

    public static void archiveOldLogs() throws IOException {

        Map<LocalDate, List<Path>> groupedLogs = findOldLogs();

        if (groupedLogs.isEmpty()) {
            log.debug("No old logs found for archiving");
            return;
        }

        Files.createDirectories(ARCHIVE_DIR);

        for (Map.Entry<LocalDate, List<Path>> entry : groupedLogs.entrySet()) {

            LocalDate date = entry.getKey();
            List<Path> logFiles = entry.getValue();

            Path archivePath = ARCHIVE_DIR.resolve(date + ".zip");
            Path temporaryArchive = Files.createTempFile(
                    ARCHIVE_DIR,
                    "archive-",
                    ".zip"
            );

            try {
                // Prepare a temporary copy of the existing archive.
                if (Files.exists(archivePath)) {
                    Files.copy(
                            archivePath,
                            temporaryArchive,
                            StandardCopyOption.REPLACE_EXISTING
                    );
                } else {
                    Files.delete(temporaryArchive);
                }

                URI temporaryUri = URI.create(
                        "jar:" + temporaryArchive.toUri()
                );

                // Add new logs to the temporary archive.
                try (FileSystem zip = FileSystems.newFileSystem(
                        temporaryUri,
                        Map.of("create", "true")
                )) {

                    for (Path logFile : logFiles) {

                        Path target = zip.getPath(
                                "/" + logFile.getFileName()
                        );

                        if (Files.exists(target)) {
                            log.debug(
                                    "Log already archived: {}",
                                    logFile.getFileName()
                            );
                            continue;
                        }

                        Files.copy(logFile, target);

                        log.info(
                                "Added log to archive: {}",
                                logFile.getFileName()
                        );
                    }
                }

                // Replace the original archive only after ZIP is closed.
                Files.move(
                        temporaryArchive,
                        archivePath,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING
                );

                log.info("Archive updated: {}", archivePath.getFileName());

                // Delete only verified source files.
                for (Path logFile : logFiles) {

                    if (isArchived(archivePath, logFile)) {

                        Files.delete(logFile);

                        log.info(
                                "Deleted archived source log: {}",
                                logFile.getFileName()
                        );

                    } else {

                        log.warn(
                                "Archive verification failed: {}",
                                logFile.getFileName()
                        );
                    }
                }

            } finally {
                Files.deleteIfExists(temporaryArchive);
            }
        }
    }

    public static void cleanupArchives() throws IOException {

        if (!Files.isDirectory(ARCHIVE_DIR)) {
            return;
        }

        LocalDate cutoffDate = LocalDate.now().minusDays(RETENTION_DAYS);

        try (Stream<Path> files = Files.list(ARCHIVE_DIR)) {

            for (Path archive : files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString()
                            .matches("\\d{4}-\\d{2}-\\d{2}\\.zip"))
                    .toList()) {

                String filename = archive.getFileName().toString();

                try {
                    LocalDate date = LocalDate.parse(
                            filename.substring(0, 10),
                            DATE_FORMAT
                    );

                    if (date.isBefore(cutoffDate)) {
                        Files.delete(archive);
                        log.info("Deleted expired archive: {}", filename);
                    }

                } catch (java.time.format.DateTimeParseException e) {
                    log.warn("Skipping archive with invalid date: {}", filename);
                }
            }
        }

        List<Path> archives;

        try (Stream<Path> files = Files.list(ARCHIVE_DIR)) {

            archives = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString()
                            .matches("\\d{4}-\\d{2}-\\d{2}\\.zip"))
                    .sorted(Comparator.comparing(path ->
                            path.getFileName().toString()))
                    .toList();
        }

        long totalSize = 0;

        for (Path archive : archives) {
            totalSize += Files.size(archive);
        }

        for (Path archive : archives) {

            if (totalSize <= MAX_ARCHIVE_SIZE) {
                break;
            }

            long size = Files.size(archive);

            Files.delete(archive);
            totalSize -= size;

            log.info("Deleted archive to enforce size limit: {}",
                    archive.getFileName());
        }
    }

}