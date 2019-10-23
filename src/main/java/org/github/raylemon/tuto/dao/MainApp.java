package org.github.raylemon.tuto.dao;

import org.fluttercode.datafactory.impl.DataFactory;
import org.github.raylemon.tuto.dao.beans.Employee;
import org.github.raylemon.tuto.dao.beans.Language;
import org.github.raylemon.tuto.dao.beans.Society;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    private List<Society> societies = new ArrayList<>();

    public static void main(String[] args) {
        MainApp app = new MainApp();
        DataFactory factory = new DataFactory();
        /* Dummies */
        Society society1 = new Society("Dummy 1");
        Employee employee1 = new Employee();
        employee1.setFirstName("Lemon");
        employee1.setLastName("Ray");
        employee1.setBirthday(LocalDate.of(1981, 7, 22));
        employee1.addLanguage(new Language("Java"));
        employee1.addLanguage(new Language("Kotlin"));
        society1.addEmployee(employee1);
        app.societies.add(society1);
        /* end Dummies */

        app.show();

        System.out.println("*******************Ajouter une société************************");
        Society society = new Society(factory.getBusinessName());
        app.societies.add(society);

        System.out.println("*******************Ajouter un employé************************");
        Employee employee = new Employee();
        employee.setFirstName(factory.getFirstName());
        employee.setLastName(factory.getLastName());
        employee.setBirthday(factory.getBirthDate());
        employee.addLanguage(new Language("Python"));
        employee.addLanguage(new Language("C#"));
        society.addEmployee(employee);


        app.show();
    }

    private void show() {
        System.out.println("*******************Affichage des sociétés*********************");
        societies.forEach(System.out::println);
    }

}
