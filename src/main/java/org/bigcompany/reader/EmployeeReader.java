package org.bigcompany.reader;

import org.bigcompany.Result;

import java.util.List;

public interface EmployeeReader {

    Result<List<EmployeeFileRow>> readEmployees(String filePath);
}
