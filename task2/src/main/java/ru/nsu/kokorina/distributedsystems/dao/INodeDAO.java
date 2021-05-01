package ru.nsu.kokorina.distributedsystems.dao;

import ru.nsu.kokorina.distributedsystems.generated.Node;

import java.sql.SQLException;
import java.util.List;

public interface INodeDAO {
    void insertNode(Node node) throws SQLException;
    void insertPreparedNode(Node node) throws SQLException;
    void insertNodesWithBatch(List<Node> nodes) throws SQLException;
}
