package com.espica.tools.srtparser;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * This class is responsible for reading an SRT file.
 *
 * @author fredy
 */
public class SRTReader {

    private static class BufferedLineReader {
        private final BufferedReader reader;
        private long lineNumber;

        public BufferedLineReader(BufferedReader reader) {
            this.reader = reader;
        }

        public String readLine() throws IOException {
            String line = reader.readLine();
            lineNumber++;
            // remove the BOM
            if (line != null && lineNumber == 1) {
                return line.replace("\uFEFF", "");
            }
            return line;
        }

        public long getLineNumber() {
            return lineNumber;
        }
    }

    /**
     * Reads an SRT file and transforming it into SRT object.
     *
     * @param srtFile SRT file
     * @return the SRTInfo object
     * @throws SRTReaderException thrown while reading SRT file
     */
    public static SRTInfo read(File srtFile) throws SRTReaderException {
        if (!srtFile.exists()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " does not exist");
        }
        if (!srtFile.isFile()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " is not a regular file");
        }

        SRTInfo srtInfo = new SRTInfo();
        try (BufferedReader br = new BufferedReader(new FileReader(srtFile))) {
            BufferedLineReader reader = new BufferedLineReader(br);
            while (true) {
                srtInfo.add(parse(reader));
            }
        } catch (EOFException e) {
            // Do nothing
        } catch (IOException e) {
            throw new SRTReaderException(e);
        }

        return srtInfo;
    }

    private static SRT parse(BufferedLineReader reader) throws IOException, EOFException {
        String nString = reader.readLine();
        // ignore all empty lines
        while (nString != null && nString.isEmpty()) {
            nString = reader.readLine();
        }

        if (nString == null) {
            throw new EOFException();
        }

        int subtitleNumber = -1;
        try {
            subtitleNumber = Integer.parseInt(nString);
        } catch (NumberFormatException e) {
            throw new SRTReaderException(
                    String.format(
                            "[Line: %d] %s has an invalid subtitle number",
                            reader.getLineNumber(),
                            nString));
        }

        String tString = reader.readLine();
        if (tString == null) {
            throw new SRTReaderException(
                    String.format(
                            "[Line: %d] Start time and end time information is not present",
                            reader.getLineNumber()));
        }
        String[] times = tString.split(SRTTimeFormat.TIME_DELIMITER);
        if (times.length != 2) {
            throw new SRTReaderException(
                    String.format(
                            "[Line: %d] %s needs to be separated with %s",
                            reader.getLineNumber(),
                            tString,
                            SRTTimeFormat.TIME_DELIMITER));
        }

        long startTime;
        try {
            startTime = SRTTimeFormat.timeToMillisecond(times[0]);
        } catch (NumberFormatException e) {
            throw new SRTReaderException(String.format(
                    "[Line: %d] %s has an invalid end time format",
                    reader.getLineNumber(),
                    times[1]));
        }

//        Date endTime = null;
//        try {
//            endTime = SRTTimeFormat.parse(times[1]);
//        } catch (ParseException e) {
//            throw new SRTReaderException(String.format(
//                    "[Line: %d] %s has an invalid end time format",
//                    reader.getLineNumber(),
//                    times[1]));
//        }
//
//        List<String> subtitleLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                break;
            }
//            subtitleLines.add(line);
        }
//
//        if (subtitleLines.size() == 0) {
//            throw new SRTReaderException(String.format(
//                    "[Line: %d] Missing subtitle text information",
//                    reader.getLineNumber()));
//        }

        return new SRT(subtitleNumber, startTime);
    }
}