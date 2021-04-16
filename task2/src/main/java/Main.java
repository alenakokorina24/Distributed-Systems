import dao.NodeDAO;
import dao.TagDAO;
import database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import processor.INodeProcessor;
import processor.NodeProcessor;
import processor.NodesBatchProcessor;
import processor.PreparedNodeProcessor;

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

            NodeDAO nodeDAO = new NodeDAO();
            TagDAO tagDAO = new TagDAO();

            List<INodeProcessor> nodeProcessors = new ArrayList<>();
            nodeProcessors.add(new NodeProcessor(nodeDAO, tagDAO));
            nodeProcessors.add(new PreparedNodeProcessor(nodeDAO, tagDAO));
            nodeProcessors.add(new NodesBatchProcessor(nodeDAO, tagDAO));

            for (INodeProcessor processor : nodeProcessors) {
                DatabaseConnection.init();
                OSMProcessor.process(inputStream, processor);
                DatabaseConnection.closeConnection();
            };
        } catch (FileNotFoundException e) {
            logger.error("Error while opening file.", e);
        } catch (IOException e) {
            logger.error("Error while reading file.", e);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
