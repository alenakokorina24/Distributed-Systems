package ru.nsu.kokorina.distributedsystems.processor;

import ru.nsu.kokorina.distributedsystems.dao.NodeDAO;
import ru.nsu.kokorina.distributedsystems.dao.TagDAO;
import ru.nsu.kokorina.distributedsystems.generated.Node;
import ru.nsu.kokorina.distributedsystems.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeProcessor implements INodeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NodeProcessor.class);

    private final NodeDAO nodeDAO;
    private final TagDAO tagDAO;

    public NodeProcessor(NodeDAO nodeDAO, TagDAO tagDAO) {
        this.nodeDAO = nodeDAO;
        this.tagDAO = tagDAO;
    }

    @Override
    public void insertNode(Node node) {
        List<Tag> tags = new ArrayList<>();
        node.getTag().forEach(tag -> {
            tags.add(new Tag(node.getId(), tag.getK(), tag.getV()));
        });
        try {
            nodeDAO.insertNode(node);
            for(Tag tag : tags) {
                tagDAO.insertTag(tag);
            }
        } catch (SQLException e) {
            logger.error("Error while inserting node.", e);
        }
    }

    @Override
    public void flush() {

    }
}
