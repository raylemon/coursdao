package org.github.raylemon.tuto.dao.dao.h2;

import org.github.raylemon.tuto.dao.beans.Employee;
import org.github.raylemon.tuto.dao.beans.Language;

import java.sql.Date;
import java.sql.*;
import java.util.*;

import static com.esotericsoftware.minlog.Log.*;


public class H2EmployeeDAO extends H2Dao<Employee> implements CollectionDAO<Employee> {

    @Override
    public Employee save(Employee object) {
        String sql = "insert into EMPLOYEES (FIRSTNAME, LASTNAME, BIRTHDAY,FK_SOCIETY) VALUES ( ?,?,?,? )";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, object.getFirstName());
            statement.setString(2, object.getLastName());
            statement.setDate(3, Date.valueOf(object.getBirthday()));
            statement.setLong(4, object.getSociety().getId());
            debug(LOG_CAT, "sql>" + statement.toString());

            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.first()) object.setId(rs.getLong(1));
            debug(LOG_CAT, object + "has now id " + object.getId());

            object.getLanguages().forEach(language -> putLanguage(language, object.getId()));

        } catch (SQLException e) {
            error(LOG_CAT, "SQL error when creating " + object, e);
        }

        return object;
    }

    private void putLanguage(Language language, long id) {
        String sql = "insert into J_EMP_LANG (FK_EMPLOYEE, FK_LANGUAGE) VALUES ( ?,? )";
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            if (language.getId() == -1)
                language = new H2LanguageDAO().save(language);
            statement.setLong(1, language.getId());
            statement.setLong(2, id);
            info(LOG_CAT, "sql> " + statement);

            statement.execute();

        } catch (SQLException e) {
            error(LOG_CAT, "sql error when saving " + language + " into junction table", e);
        }
        info(LOG_CAT, language + " attached to emp id " + id);
    }

    @Override
    public Employee find(long id) {
        String sql = "select * from EMPLOYEES where EMP_ID = ?";
        Employee employee = new Employee();
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setLong(1, id);
            info(LOG_CAT, "sql> " + statement);

            ResultSet rs = statement.executeQuery();
            if (rs.first()) {
                employee.setId(id);
                employee.setFirstName(rs.getString("FIRSTNAME"));
                employee.setLastName(rs.getString("LASTNAME"));
                employee.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
                employee.setLanguages(getLanguages(id));
            }
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when finding Employee id " + id, e);
        }
        return employee;
    }

    private Set<Language> getLanguages(long id) {
        String sql = "select * from J_EMP_LANG where FK_EMPLOYEE =?";
        Set<Language> languages = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setLong(1, id);
            info(LOG_CAT, "sql> " + statement);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                languages.add(new H2LanguageDAO().find(rs.getLong("FK_LANGUAGE")));
            }
            info(LOG_CAT, "finding " + languages.size() + " languages");
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when retrieving languages from junction table", e);
        }
        return languages;
    }

    @Override
    public Employee update(Employee object) {
        String sql = "update EMPLOYEES set FIRSTNAME = ?, LASTNAME = ?,BIRTHDAY = ?,FK_SOCIETY = ? where EMP_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setString(1, object.getFirstName());
            statement.setString(2, object.getLastName());
            statement.setDate(3, Date.valueOf(object.getBirthday()));
            statement.setLong(4, object.getSociety().getId());
            statement.setLong(5, object.getId());
            info(LOG_CAT, "sql> " + statement);

            statement.executeUpdate();
            object.getLanguages().forEach(new H2LanguageDAO()::update);
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when updating " + object);
        }
        info(LOG_CAT, "Successfully updated " + object);
        return object;
    }

    @Override
    public void delete(Employee object) {
        String sql1 = "delete from EMPLOYEES where EMP_ID = ?";
        String sql2 = "delete from J_EMP_LANG where FK_EMPLOYEE = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql1);
             PreparedStatement statement2 = connection.prepareStatement(sql2)) {

            statement.setLong(1, object.getId());
            info(LOG_CAT, "sql> " + statement);

            statement.execute();

            statement2.setLong(1, object.getId());
            info(LOG_CAT, "sql> " + statement2);

            statement2.execute();
        } catch (SQLException e) {
            error(LOG_CAT, "sql error when deleting " + object, e);
        }
        info(LOG_CAT, "Successfully removed " + object);
    }


    @Override
    public Collection<Employee> findAll() {
        String sql = "select EMP_ID from EMPLOYEES";
        List<Employee> employees = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            info(LOG_CAT, "sql> " + sql);

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                employees.add(find(rs.getLong(1)));
            }

        } catch (SQLException e) {
            error(LOG_CAT, "sql error when retrieving Employees", e);
        }
        info(LOG_CAT, "finding " + employees.size() + " employees");
        return employees;
    }

    @Override
    public void saveAll(Collection<Employee> collection) {
        collection.forEach(this::save);
    }
}
