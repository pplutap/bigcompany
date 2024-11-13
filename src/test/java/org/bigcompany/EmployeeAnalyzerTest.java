package org.bigcompany;

import org.bigcompany.analyzer.EmployeeAnalyzer;
import org.bigcompany.analyzer.PayEntry;
import org.bigcompany.model.Employee;
import org.bigcompany.processor.EmployeeFileProcessor;
import org.bigcompany.processor.Hierarchy;
import org.bigcompany.reader.EmployeeReader;
import org.bigcompany.reader.csv.CSVEmployeeReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class EmployeeAnalyzerTest {

    private EmployeeReader fileReader = new CSVEmployeeReader();
    private EmployeeFileProcessor fileProcessor = new EmployeeFileProcessor(fileReader);
    private EmployeeAnalyzer employeeAnalyzer = new EmployeeAnalyzer(fileProcessor);

    @Test
    public void shouldCheckValidReportingLine() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.addEmployee(Employee.createEmployee(123, "Joe", "Doe", 60000, null));
        hierarchy.addEmployee(Employee.createEmployee(124, "Martin", "Chekov", 45000, 123));
        hierarchy.addEmployee(Employee.createEmployee(125, "Bob", "Ronstad", 47000, 123));
        hierarchy.addEmployee(Employee.createEmployee(300, "Alice", "Hasacat", 50000, 124));
        hierarchy.addEmployee(Employee.createEmployee(305, "Brett", "Hardleaf", 34000, 300));
        Map<Employee, Integer> tooLongReportingLines = employeeAnalyzer.getEmployeesWithTooLongReportingLines(hierarchy, 4);
        Assertions.assertEquals(0, tooLongReportingLines.size());
    }

    @Test
    public void shouldCheckTooReportingLine() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.addEmployee(Employee.createEmployee(123, "Joe", "Doe", 60000, null));
        hierarchy.addEmployee(Employee.createEmployee(124, "Martin", "Chekov", 45000, 123));
        hierarchy.addEmployee(Employee.createEmployee(125, "Bob", "Ronstad", 47000, 124));
        hierarchy.addEmployee(Employee.createEmployee(126, "Alice", "Hasacat", 50000, 125));
        hierarchy.addEmployee(Employee.createEmployee(127, "Brett", "Hardleaf", 34000, 126));
        hierarchy.addEmployee(Employee.createEmployee(128, "Brett", "Hardleaf", 34000, 127));
        Map<Employee, Integer> tooLongReportingLines = employeeAnalyzer.getEmployeesWithTooLongReportingLines(hierarchy, 4);
        Assertions.assertEquals(1, tooLongReportingLines.size());
        Assertions.assertEquals(128, tooLongReportingLines.keySet().iterator().next().getId());
    }

    @Test
    public void shouldFindUnderpaidManagers() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.addEmployee(Employee.createEmployee(123, "Joe", "Doe", 60000, null));
        hierarchy.addEmployee(Employee.createEmployee(124, "Martin", "Chekov", 45000, 123));
        hierarchy.addEmployee(Employee.createEmployee(125, "Bob", "Ronstad", 47000, 123));
        hierarchy.addEmployee(Employee.createEmployee(300, "Alice", "Hasacat", 50000, 124));
        hierarchy.addEmployee(Employee.createEmployee(305, "Brett", "Hardleaf", 34000, 300));
        List<PayEntry> underpaidManagers = employeeAnalyzer.findUnderpaidManagers(hierarchy);
        Assertions.assertEquals(1, underpaidManagers.size());
        Assertions.assertEquals(124, underpaidManagers.get(0).managerId());
        Assertions.assertEquals(45000, underpaidManagers.get(0).salary());
        Assertions.assertEquals(50000, underpaidManagers.get(0).averageSalary());
        Assertions.assertEquals(15000, underpaidManagers.get(0).difference());
    }

    @Test
    public void shouldFindOverpaidManagers() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.addEmployee(Employee.createEmployee(123, "Joe", "Doe", 60000, null));
        hierarchy.addEmployee(Employee.createEmployee(124, "Martin", "Chekov", 45000, 123));
        hierarchy.addEmployee(Employee.createEmployee(125, "Bob", "Ronstad", 47000, 123));
        hierarchy.addEmployee(Employee.createEmployee(300, "Alice", "Hasacat", 70000, 124));
        hierarchy.addEmployee(Employee.createEmployee(305, "Brett", "Hardleaf", 34000, 300));
        List<PayEntry> overpaidManagers = employeeAnalyzer.findOverpaidManagers(hierarchy);
        Assertions.assertEquals(1, overpaidManagers.size());
        Assertions.assertEquals(300, overpaidManagers.get(0).managerId());
        Assertions.assertEquals(70000, overpaidManagers.get(0).salary());
        Assertions.assertEquals(34000, overpaidManagers.get(0).averageSalary());
        Assertions.assertEquals(19000, overpaidManagers.get(0).difference());
    }
}
