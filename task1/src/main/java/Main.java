import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String STATISTIC_FORMAT = "%s : %d%n";

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Too few arguments.");
        }

        String inputFilename = args[0];
        logger.info("File decompressing from BZip2 started...");
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(inputFilename))) {
            logger.info("File successfully decompressed.");
            try {
                OsmProcessor osmProcessor = new OsmProcessor();
                osmProcessor.processData(inputStream);
                printStatistic(osmProcessor.getUsersEdits());
                printStatistic(osmProcessor.getKeysTags());
            } catch (XMLStreamException e) {
                logger.error("Error while processing file.", e);
            }
        } catch (FileNotFoundException e) {
            logger.error("Error while opening file.", e);
        } catch (IOException e) {
            logger.error("Error while reading file.", e);
        }
    }

    private static void printStatistic(Map<String, Integer> statistic) {
        statistic.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .forEach(e -> System.out.printf(STATISTIC_FORMAT, e.getKey(), e.getValue()));
    }
}
