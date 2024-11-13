package org.bigcompany.reader;

import java.util.Objects;

public record EmployeeFileRow(int id, String firstName, String lastName, double salary, Integer managerId) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeFileRow that = (EmployeeFileRow) o;
        return id == that.id && Double.compare(salary, that.salary) == 0 && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(managerId, that.managerId);
    }

}
