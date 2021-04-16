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

public class PreparedNodeProcessor implements INodeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PreparedNodeProcessor.class);

    private final NodeDAO nodeDAO;
    private final TagDAO tagDAO;

    public PreparedNodeProcessor(NodeDAO nodeDAO, TagDAO tagDAO) {
        this.nodeDAO = nodeDAO;
        this.tagDAO = tagDAO;
    }

    @Override
    public void insertNode(Node node) {
        List<Tag> tags = new ArrayList<>();
        node.getTags().forEach(tag -> {
            tags.add(new Tag(node.getId(), tag.getKey(), tag.getValue()));
        });
        try {
            nodeDAO.insertPreparedNode(node);
            for (Tag tag : tags) {
                tagDAO.insertPreparedTag(tag);
            }
        } catch (SQLException e) {
            logger.error("Error while inserting prepared node.", e);
        }
    }

    @Override
    public void flush() {

    }
}
