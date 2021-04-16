package dao;

import model.Tag;

import java.sql.SQLException;
import java.util.List;

public interface ITagDAO {
    void insertTag(Tag tag) throws SQLException;
    void insertPreparedTag(Tag tag) throws SQLException;
    void insertTagsWithBatch(List<Tag> tags) throws SQLException;
}