package org.github.raylemon.tuto.dao.dao.h2;

import org.github.raylemon.tuto.dao.beans.Employee;
import org.github.raylemon.tuto.dao.beans.Society;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.esotericsoftware.minlog.Log.*;


public class H2SocietyDAO extends H2Dao<Society> implements CollectionDAO<Society> {
    @Override
    public Society save(Society object) {
        String sql = "insert into SOCIETIES (NAME) values ( ? )";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, object.getName());
            debug(LOG_CAT, "sql> " + statement.toString());

            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.first()) object.setId(rs.getLong(1));
            debug(LOG_CAT, object + " has now id " + object.getId());

            object.getEmployees().forEach(new H2EmployeeDAO()::save);

        } catch (SQLException e) {
            error(LOG_CAT, "SQL error when creating " + object, e);
        }
        return object;
    }

    @Override
    public Society find(long id) {
        String sql = "select * from SOCIETIES where SOC_ID = ?";
        Society society = new Society("");
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setLong(1, id);
            info(LOG_CAT, "sql> " + statement);

            ResultSet rs = statement.executeQuery();
            if (rs.first()) {
                society.setId(id);
                society.setName(rs.getString("NAME"));
                List<Employee> employees = new ArrayList<>(new H2EmployeeDAO().findAll());
                society.setEmployees(employees.parallelStream().filter(employee -> employee.getSociety().getId() == id).collect(Collectors.toList()));

                info(LOG_CAT, "finding Society " + society);
            }
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when searching Society number " + id, e);
        }
        return society;
    }

    @Override
    public Society update(Society object) {
        String sql = "update SOCIETIES set NAME = ?  where SOC_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setString(1, object.getName());
            statement.setLong(2, object.getId());
            info(LOG_CAT, "sql> " + statement);

            statement.executeUpdate();
            object.getEmployees().forEach(new H2EmployeeDAO()::update);
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when updating :" + object, e);
        }
        info(LOG_CAT, "Successfully updated Society " + object);
        return object;
    }

    @Override
    public void delete(Society object) {
        String sql = "delete from SOCIETIES where SOC_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, object.getId());
            info(LOG_CAT, "sql> " + statement);

            statement.execute();
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when delete " + object, e);
        }
        info(LOG_CAT, "Successfully removed Society " + object);
    }

    @Override
    public Collection<Society> findAll() {
        String sql = "select SOC_ID from SOCIETIES";
        List<Society> societies = new ArrayList<>();
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            info(LOG_CAT, "sql> " + sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                societies.add(find(rs.getLong(1)));
            }
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when retrieving Societies collection", e);
        }
        info(LOG_CAT, "Finding " + societies.size() + " societies");
        return societies;
    }

    @Override
    public void saveAll(Collection<Society> collection) {
        collection.forEach(this::save);
    }
}
