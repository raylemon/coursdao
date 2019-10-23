package org.github.raylemon.tuto.dao.dao.h2;

import org.github.raylemon.tuto.dao.beans.Language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.esotericsoftware.minlog.Log.info;
import static com.esotericsoftware.minlog.Log.warn;


public class H2LanguageDAO extends H2Dao<Language> {
    @Override
    public Language save(Language object) {
        String sql = "insert into LANGUAGES (NAME) values (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, object.getName());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) object.setId(rs.getLong(1));
        } catch (SQLException e) {
            warn("Error when saving language", e);
        }
        info("Storing " + object.toString());
        return object;
    }

    @Override
    public Language find(long id) {
        String sql = "select * from LANGUAGES where LANG_ID = ?";
        Language language = null;
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.first()) {
                language = new Language(id, rs.getString("NAME"));
                info("Finding " + language);
            }
        } catch (SQLException e) {
            warn("Error when finding language id " + id, e);
        }
        if (language != null) return language;
        else return new Language("");
    }

    @Override
    public Language update(Language object) {
        String sql = "update LANGUAGES set NAME = '?' where LANG_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setString(1, object.getName());
            statement.setLong(2, object.getId());

            statement.executeUpdate();
            object = this.find(object.getId());
        } catch (SQLException e) {
            warn("Error when updating " + object, e);
        }
        info("Successfuly updated " + object);
        return object;
    }

    @Override
    public void delete(Language object) {
        var sql = "delete from LANGUAGES where LANG_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setLong(1, object.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            warn("Error when deleting " + object, e);
        }
        info("Successfully removed " + object);
    }
}
