package ru.nsu.fit.kokorina.distributedsystems;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Too few arguments.");
        }

        String inputFilename = args[0];
        logger.info("File decompressing from BZip2 started...");
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(inputFilename))) {
            logger.info("File successfully decompressed.");
            try {
                OSMProcessor osmProcessor = new OSMProcessor();
                osmProcessor.processData(inputStream);
                printInfo(osmProcessor.getUsersEdits(), "USER - EDITS NUMBER");
                printInfo(osmProcessor.getKeysTags(), "KEYS - TAGS WITH THE KEY");
            } catch (XMLStreamException e) {
                logger.error("Error while processing file.", e);
            }
        } catch (FileNotFoundException e) {
            logger.error("Error while opening file.", e);
        } catch (IOException e) {
            logger.error("Error while reading file.", e);
        }
    }

    private static void printInfo(Map<String, Integer> info, String title) {
        HashMap<String, Integer> sorted = new HashMap<>();

        info.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

        System.out.println(title);
        sorted.forEach((k, v) -> {
            System.out.println(k + " - " + v);
        });
    }
}