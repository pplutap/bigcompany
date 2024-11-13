package org.bigcompany.processor;

import org.bigcompany.Result;
import org.bigcompany.model.Employee;
import org.bigcompany.reader.EmployeeFileRow;
import org.bigcompany.reader.EmployeeReader;

import java.util.List;

public class EmployeeFileProcessor {

    private final EmployeeReader fileReader;

    public EmployeeFileProcessor(EmployeeReader fileReader) {
        this.fileReader = fileReader;
    }

    public Result<Hierarchy> fetchEmployees(String filePath) {
        Result<List<EmployeeFileRow>> readingFileResult = fileReader.readEmployees(filePath);
        if (!readingFileResult.isSuccess()) {
            return Result.failure(readingFileResult.getError());
        }
        return buildHierarchy(readingFileResult.getValue());
    }

    private Result<Hierarchy> buildHierarchy(List<EmployeeFileRow> rows) {
        Hierarchy hierarchy = new Hierarchy();
        for (EmployeeFileRow row : rows) {
            hierarchy.addEmployee(map(row));
        }
        return Result.success(hierarchy);
    }


    private Employee map(EmployeeFileRow row) {
        return Employee.createEmployee(row.id(), row.firstName(), row.lastName(), row.salary(), row.managerId());
    }
}
