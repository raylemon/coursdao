package org.github.raylemon.tuto.dao.dao.h2;

import org.github.raylemon.tuto.dao.beans.Language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.esotericsoftware.minlog.Log.*;


public class H2LanguageDAO extends H2Dao<Language> {

    @Override
    public Language save(Language object) {
        String sql = "insert into LANGUAGES (NAME) values (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, object.getName());
            debug(LOG_CAT, "sql> " + statement.toString());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) object.setId(rs.getLong(1));
        } catch (SQLException e) {
            error(LOG_CAT, "SQL Error when saving language", e);
        }
        info(LOG_CAT, "Storing " + object.toString());
        return object;
    }

    @Override
    public Language find(long id) {
        String sql = "select * from LANGUAGES where LANG_ID = ?";
        Language language = null;
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setLong(1, id);
            debug(LOG_CAT, "sql> " + statement.toString());

            ResultSet rs = statement.executeQuery();
            if (rs.first()) {
                language = new Language(id, rs.getString("NAME"));
                info(LOG_CAT, "Finding " + language);
            }
        } catch (SQLException e) {
            error(LOG_CAT, "SQL error when finding language id " + id, e);
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
            debug(LOG_CAT, "sql> " + statement.toString());

            statement.executeUpdate();
            object = this.find(object.getId());
        } catch (SQLException e) {
            error(LOG_CAT, "SQL error when updating object " + object, e);
        }
        info(LOG_CAT, "Successfuly updated " + object);
        return object;
    }

    @Override
    public void delete(Language object) {
        var sql = "delete from LANGUAGES where LANG_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setLong(1, object.getId());
            debug(LOG_CAT, "sql> " + statement.toString());

            statement.executeUpdate();

        } catch (SQLException e) {
            error(LOG_CAT, "SQL error when deleting object " + object, e);
        }
        info(LOG_CAT, "Successfully removed " + object);
    }

    /*@Override
    public List<Language> findAll() {
        String sql = "select * from LANGUAGES";
        List<Language> languages = new ArrayList<>();

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            debug(LOG_CAT, "sql> " + sql);

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Language language = new Language(
                        rs.getLong("LANG_ID"),
                        rs.getString("NAME")
                );
                languages.add(language);
                debug(LOG_CAT, "found " + language);
            }
        } catch (SQLException e) {
            error(LOG_CAT, "SQL error when retrieving all objects", e);
        }
        info(LOG_CAT, "Successfully found " + languages.size() + " elements");
        return languages;
    }

    @Override
    public void saveAll(Collection<Language> collection) {
        String sql = "insert into LANGUAGES (NAME) values (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            collection.forEach(language -> {
                try {
                    statement.setString(1, language.getName());
                    statement.addBatch();
                    debug(LOG_CAT, "adding to batch > " + statement.toString());
                } catch (SQLException e) {
                    error(LOG_CAT, "SQL error when building batch", e);
                }
            });

            statement.executeBatch();
        } catch (SQLException e) {
            error(LOG_CAT, "SQL error when saving all objects", e);
        }
    }*/
}
