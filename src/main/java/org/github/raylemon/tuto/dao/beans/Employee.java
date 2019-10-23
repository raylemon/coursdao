package org.github.raylemon.tuto.dao.beans;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Employee {
    private long id = -1;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Set<Language> languages = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public boolean addLanguage(Language language) {
        return languages.add(language);
    }

    public boolean containsLanguage(Language language) {
        return languages.contains(language);
    }

    public boolean removeLanguage(Language language) {
        return languages.remove(language);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(id).append(":")
                .append(firstName).append(" ")
                .append(lastName)
                .append("\n++++++++++++++++++++Langages connus ++++++++++++++++++++\n");
        languages.forEach(language -> builder.append(language).append("\n"));
        return builder.toString();

    }
}
