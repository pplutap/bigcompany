package org.bigcompany.processor;

import org.bigcompany.model.Employee;

import java.util.*;

public class Hierarchy {
    private Employee ceo;
    private Map<Integer, Employee> employees = new HashMap<>();
    private Map<Integer, List<Integer>> managerIdToSubordinates = new HashMap<>();

    public void addEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
        if (employee.getManagerId() == null) {
            ceo = employee;
            return;
        }
        managerIdToSubordinates
                .computeIfAbsent(employee.getManagerId(), k -> new ArrayList<>())
                .add(employee.getId());
    }

    public List<Employee> getEmployees() {
        return employees.values().stream().toList();
    }

    public Employee getEmployee(Integer id) {
        return employees.get(id);
    }

    public Set<Integer> getManagerIds() {
        return managerIdToSubordinates.keySet();
    }

    public List<Double> getSubordinatesSalaries(Integer managerId) {
        return managerIdToSubordinates.get(managerId)
                .stream()
                .map(employeeId -> employees.get(employeeId).getSalary())
                .toList();
    }

    public int getNumberOfManagersBetweenEmployeeAndCEO(Integer employeeId) {
        int count = 0;
        Employee employee = employees.get(employeeId);
        Set<Integer> visited = new HashSet<>();
        while (employee != null && employee.getManagerId() != null) {
            if (!visited.add(employee.getId())) {
                System.out.println("Cycle detected for employee ID: " + employee.getId());
                break;
            }
            employee = employees.get(employee.getManagerId());
            if (employee != null) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

}
