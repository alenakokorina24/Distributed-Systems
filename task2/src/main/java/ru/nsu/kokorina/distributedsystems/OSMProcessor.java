package ru.nsu.kokorina.distributedsystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokorina.distributedsystems.generated.Node;
import ru.nsu.kokorina.distributedsystems.processor.INodeProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

public class OSMProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OSMProcessor.class);

    public static void process(InputStream inputStream, INodeProcessor nodeProcessor) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = null;
        double totalTime = 0;
        double nNodes = 0;
        double nTags = 0;

        try {
            xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
            JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.peek();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("node")) {
                        Node node = unmarshaller.unmarshal(xmlEventReader, Node.class).getValue();

                        long start = System.currentTimeMillis();
                        nNodes++;
                        nTags += node.getTag().size();
                        nodeProcessor.insertNode(node);
                        long end = System.currentTimeMillis();

                        totalTime += end - start;
                        continue;
                    }
                }
                xmlEventReader.nextEvent();
            }
            nodeProcessor.flush();
        } catch (JAXBException e) {
            logger.error("JAXB error.", e);
        } catch (XMLStreamException e) {
            logger.error("XML stream error.", e);
        } finally {
            if (xmlEventReader != null) {
                xmlEventReader.close();
            }
        }
        logger.info("OSM processing with " + nodeProcessor.getClass() + " finished.");
        logger.info("Speed: {}", (nNodes + nTags) / totalTime * 1000);
    }
}
