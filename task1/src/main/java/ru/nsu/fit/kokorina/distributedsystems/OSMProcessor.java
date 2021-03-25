package ru.nsu.fit.kokorina.distributedsystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;

public class OSMProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OSMProcessor.class);

    private final QName USER = new QName("user");
    private final QName KEY = new QName("k");
    private final String TAG = "tag";
    private final String NODE = "node";

    private HashMap<String, Integer> usersEdits = new HashMap<>();
    private HashMap<String, Integer> keysTags = new HashMap<>();

    public void processData(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(inputStream);
        logger.info("Data processing started...");
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals(NODE)) {
                    Attribute userAttribute = startElement.getAttributeByName(USER);
                    String user = userAttribute.getValue();
                    if (usersEdits.containsKey(user)) {
                        usersEdits.put(user, usersEdits.get(user) + 1);
                    } else {
                        usersEdits.put(user, 1);
                    }
                    countTagsPerKey(reader);
                }
            }
        }
        logger.info("Data processing successfully finished.");
        reader.close();
    }

    private void countTagsPerKey(XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isEndElement() &&
                    nextEvent.asEndElement().getName().getLocalPart().equals(NODE)) {
                return;
            }
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals(TAG)) {
                    Attribute keyAttribute = startElement.getAttributeByName(KEY);
                    String key = keyAttribute.getValue();
                    if (keysTags.containsKey(key)) {
                        keysTags.put(key, keysTags.get(key) + 1);
                    } else {
                        keysTags.put(key, 1);
                    }
                }
            }
        }
    }

    public HashMap<String, Integer> getUsersEdits() {
        return usersEdits;
    }

    public HashMap<String, Integer> getKeysTags() {
        return keysTags;
    }
}