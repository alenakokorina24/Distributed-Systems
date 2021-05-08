package ru.nsu.kokorina.distributedsystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.kokorina.distributedsystems.generated.Node;
import ru.nsu.kokorina.distributedsystems.model.NodeEntity;
import ru.nsu.kokorina.distributedsystems.model.Tag;
import ru.nsu.kokorina.distributedsystems.repository.NodeRepository;
import ru.nsu.kokorina.distributedsystems.repository.TagRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.stream.Collectors;

@Service
public class OSMProcessor {

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private TagRepository tagRepository;

    private static final Logger logger = LoggerFactory.getLogger(OSMProcessor.class);

    public void process(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = null;

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
                        NodeEntity nodeEntity = NodeEntity.builder()
                                .username(node.getUser())
                                .lon(node.getLon())
                                .lat(node.getLat())
                                .tags(node.getTag().stream()
                                        .map(t -> Tag.builder()
                                                .key(t.getK())
                                                .value(t.getV())
                                                .build()).collect(Collectors.toList()))
                                .build();
                        tagRepository.saveAll(nodeEntity.getTags());
                        nodeRepository.save(nodeEntity);
                    }
                }
                xmlEventReader.nextEvent();
            }
        } catch (JAXBException e) {
            logger.error("JAXB error.", e);
        } catch (XMLStreamException e) {
            logger.error("XML stream error.", e);
        } finally {
            if (xmlEventReader != null) {
                xmlEventReader.close();
            }
        }
    }
}
