package org.github.raylemon.tuto.dao;

import org.fluttercode.datafactory.impl.DataFactory;
import org.github.raylemon.tuto.dao.beans.Employee;
import org.github.raylemon.tuto.dao.beans.Language;
import org.github.raylemon.tuto.dao.beans.Society;
import org.github.raylemon.tuto.dao.dao.h2.H2SocietyDAO;

public class MainApp {
    private H2SocietyDAO societyDAO = new H2SocietyDAO();

    public static void main(String[] args) {
        MainApp app = new MainApp();
        DataFactory factory = new DataFactory();

        app.show();

        System.out.println("*******************Ajouter une société************************");
        Society society = new Society(factory.getBusinessName());
        System.out.println(society);

        System.out.println("*******************Ajouter un employé************************");
        Employee employee = new Employee();
        employee.setFirstName(factory.getFirstName());
        employee.setLastName(factory.getLastName());
        employee.setBirthday(factory.getBirthDate());
        employee.addLanguage(new Language("Python"));
        employee.addLanguage(new Language("C#"));
        employee.setSociety(society);
        society.addEmployee(employee);

        app.societyDAO.save(society);

        app.show();
    }

    private void show() {
        System.out.println("*******************Affichage des sociétés*********************");
        societyDAO.findAll().forEach(System.out::println);
    }

}
