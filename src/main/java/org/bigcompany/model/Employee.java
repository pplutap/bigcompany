package org.bigcompany.model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private final Integer managerId;

    private Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public static Employee createEmployee(int id, String firstName, String lastName, double salary, Integer managerId) {
        return new Employee(id, firstName, lastName, salary, managerId);
    }

    public int getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public double getSalary() { return salary; }

    public Integer getManagerId() { return managerId; }
}

