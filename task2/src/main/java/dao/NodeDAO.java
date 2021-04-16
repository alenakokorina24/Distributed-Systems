package dao;

import database.DatabaseConnection;
import model.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class NodeDAO implements INodeDAO {
    private String SQL_INSERT = "INSERT INTO nodes(id, user, lon, lat " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void insertNode(Node node) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        long id = node.getId();
        String user = node.getUser();
        double lat = node.getLat();
        double lon = node.getLon();
        String sqlInsert = "INSERT INTO nodes(id, user, lon, lat) " +
                "VALUES (" + id + ", " + user + ", " + lon + ", " + lat + ");";
        statement.execute(sqlInsert);
    }

    @Override
    public void insertPreparedNode(Node node) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(preparedStatement, node);
        preparedStatement.execute();
    }

    @Override
    public void insertNodesWithBatch(List<Node> nodes) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        for (Node node : nodes) {
            prepareStatement(preparedStatement, node);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    private void prepareStatement(PreparedStatement statement, Node node) throws SQLException {
        statement.setLong(1, node.getId());
        statement.setString(2, node.getUser());
        statement.setDouble(3, node.getLon());
        statement.setDouble(4, node.getLat());
    }
}
