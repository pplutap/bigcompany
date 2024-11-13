package org.bigcompany.analyzer;

import org.bigcompany.Result;
import org.bigcompany.processor.EmployeeFileProcessor;
import org.bigcompany.model.Employee;
import org.bigcompany.processor.Hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeAnalyzer {

    public static final double MIN_THRESHOLD = 1.2;
    public static final double MAX_THRESHOLD = 1.5;
    public static final int MAX_ALLOWED_LEVELS = 4;
    private final EmployeeFileProcessor fileProcessor;

    public EmployeeAnalyzer(EmployeeFileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    public Result<Void> analyzeFile(String filePath) {
        Result<Hierarchy> result = fileProcessor.fetchEmployees(filePath);
        if (!result.isSuccess()) {
            return Result.failure(result.getError());
        }
        List<PayEntry> underpaidManagers = findUnderpaidManagers(result.getValue());
        for(PayEntry payEntry : underpaidManagers) {
            System.out.println("Manager with ID: " + payEntry.managerId() + " is underpaid by " + payEntry.difference());
        }

        List<PayEntry> overpaidManagers = findOverpaidManagers(result.getValue());
        for(PayEntry payEntry : overpaidManagers) {
            System.out.println("Manager with ID: " + payEntry.managerId() + " is overpaid by " + payEntry.difference());
        }

        Map<Employee, Integer> tooLongReportingLines = getEmployeesWithTooLongReportingLines(result.getValue(), MAX_ALLOWED_LEVELS);
        for(Map.Entry<Employee, Integer> entry : tooLongReportingLines.entrySet()) {
            System.out.println("Employee with ID: " + entry.getKey().getId() + " has a reporting line too long by " + entry.getValue() + " levels");
        }

        return Result.success(null);
    }

    public List<PayEntry> findUnderpaidManagers(Hierarchy hierarchy) {
        List<PayEntry> entries = new ArrayList<>();
        for (Integer managerId : hierarchy.getManagerIds()) {
            Employee manager = hierarchy.getEmployee(managerId);
            List<Double> subordinatesSalaries = hierarchy.getSubordinatesSalaries(managerId);
            double average = calculateAverage(subordinatesSalaries);
            double minSalary = average * MIN_THRESHOLD;
            if (manager.getSalary() < minSalary) {
                double difference = minSalary - manager.getSalary();
                entries.add(new PayEntry(managerId, manager.getSalary(), average, difference));
            }
        }
        return entries;
    }

    public List<PayEntry> findOverpaidManagers(Hierarchy hierarchy) {
        List<PayEntry> entries = new ArrayList<>();
        for (Integer managerId : hierarchy.getManagerIds()) {
            Employee manager = hierarchy.getEmployee(managerId);
            List<Double> subordinatesSalaries = hierarchy.getSubordinatesSalaries(managerId);
            double average = calculateAverage(subordinatesSalaries);
            double maxSalary = average * MAX_THRESHOLD;
            if (manager.getSalary() > maxSalary) {
                double difference = manager.getSalary() - maxSalary;
                entries.add(new PayEntry(managerId, manager.getSalary(), average, difference));
            }
        }
        return entries;
    }

    public Map<Employee, Integer> getEmployeesWithTooLongReportingLines(Hierarchy hierarchy, int maxAllowedLevels) {
        Map<Employee, Integer> result = new HashMap<>();
        for (Employee employee : hierarchy.getEmployees()) {
            int levels = hierarchy.getNumberOfManagersBetweenEmployeeAndCEO(employee.getId());
            if (levels > maxAllowedLevels) {
                int extraLevels = levels - maxAllowedLevels;
                result.put(employee, extraLevels);
            }
        }
        return result;
    }


    private Double calculateAverage(List<Double> numbers) {
        return numbers.stream().mapToDouble(d -> d).average().orElse(0);
    }
}

