import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;

public class OsmProcessor {

    private static final Logger logger = LogManager.getLogger(OsmProcessor.class);

    private final QName ID = new QName("id");
    private final QName USER = new QName("user");
    private final QName KEY = new QName("key");
    private final String TAG = "tag";
    private final String NODE = "node";

    private HashMap<String, Integer> usersEdits = new HashMap<>();
    private HashMap<String, Integer> keysTags = new HashMap<>();

    public void processData(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(inputStream);
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("node")) {
                    Attribute userAttribute = startElement.getAttributeByName(USER);
                    String user = userAttribute.getValue();
                    usersEdits.put(user, usersEdits.get(user) + 1);
                    countTagsPerKey(reader);
                }
            }
        }
        reader.close();
    }

    private void countTagsPerKey(XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isEndElement() &&
                    nextEvent.asEndElement().getName().getLocalPart().equals("node")) {
                return;
            }
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("tag")) {
                    Attribute keyAttribute = startElement.getAttributeByName(KEY);
                    String key = keyAttribute.getValue();
                    keysTags.put(key, keysTags.get(key) + 1);
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
