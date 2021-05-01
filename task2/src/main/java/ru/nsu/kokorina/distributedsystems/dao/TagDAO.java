package ru.nsu.kokorina.distributedsystems.dao;

import ru.nsu.kokorina.distributedsystems.database.DatabaseConnection;
import ru.nsu.kokorina.distributedsystems.model.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TagDAO implements ITagDAO {

    private final String SQL_INSERT = "INSERT INTO tags(node_id, key, value) " +
            "VALUES (?, ?, ?)";

    @Override
    public void insertTag(Tag tag) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        String key = tag.getTag().getK().replace("'","");
        String value = tag.getTag().getV().replace("'","");
        String sqlInsert = "INSERT INTO tags(node_id, key, value) " +
                "VALUES (" + tag.getNodeId() + ", '" + key + "', '" + value + "')";
        statement.execute(sqlInsert);
    }

    @Override
    public void insertPreparedTag(Tag tag) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(statement, tag);
        statement.execute();
    }

    @Override
    public void insertTagsWithBatch(List<Tag> tags) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        for (Tag tag : tags) {
            prepareStatement(statement, tag);
            statement.addBatch();
        }
        statement.executeBatch();
    }

    private void prepareStatement(PreparedStatement statement, Tag tag) throws SQLException {
        statement.setLong(1, tag.getNodeId());
        statement.setString(2, tag.getTag().getK());
        statement.setString(3, tag.getTag().getV());
    }
}
