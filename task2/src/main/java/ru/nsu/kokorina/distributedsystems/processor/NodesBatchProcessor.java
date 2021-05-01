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

public class NodesBatchProcessor implements INodeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NodesBatchProcessor.class);

    private static final int THRESH = 1024;

    private List<Node> nodes = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private final NodeDAO nodeDAO;
    private final TagDAO tagDAO;

    public NodesBatchProcessor(NodeDAO nodeDAO, TagDAO tagDAO) {
        this.nodeDAO = nodeDAO;
        this.tagDAO = tagDAO;
    }

    @Override
    public void insertNode(Node node) {
        nodes.add(node);
        node.getTag().forEach(tag -> {
            tags.add(new Tag(node.getId(), tag.getK(), tag.getV()));
        });

        if (nodes.size() + tags.size() > THRESH) {
            flush();
        }
    }

    public void flush() {
        try {
            nodeDAO.insertNodesWithBatch(nodes);
            tagDAO.insertTagsWithBatch(tags);
        } catch (SQLException e) {
            logger.error("Error while flushing.", e);
        }
        nodes.clear();
        tags.clear();
    }
}
