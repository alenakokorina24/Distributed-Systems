package processor;

import dao.NodeDAO;
import dao.TagDAO;
import model.Node;
import model.Tag;
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
        node.getTags().forEach(tag -> {
            tags.add(new Tag(node.getId(), tag.getKey(), tag.getValue()));
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
