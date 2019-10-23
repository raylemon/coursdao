package org.github.raylemon.tuto.dao.beans;

import java.util.ArrayList;
import java.util.List;

public class Society {
    private long id = -1;
    private String name;
    private List<Employee> employees = new ArrayList<>();

    public Society(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public boolean addEmployee(Employee employee) {
        return employees.add(employee);
    }

    public boolean removeEmployee(Employee employee) {
        return employees.remove(employee);
    }

    public boolean containsEmployee(Employee employee) {
        return employees.contains(employee);
    }

    @Override
    public String toString() {
        return id + ":" + name;
    }
}
