package org.bigcompany.processor;

import org.bigcompany.Result;
import org.bigcompany.reader.EmployeeReader;
import org.bigcompany.reader.csv.CSVEmployeeReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeFileProcessorTest {

    private EmployeeReader reader = new CSVEmployeeReader();
    private EmployeeFileProcessor fileProcessor;

    @Test
    void shouldFetchEmployees() {
        fileProcessor = new EmployeeFileProcessor(reader);
        Result<Hierarchy> result = fileProcessor.fetchEmployees("src/main/resources/employees.csv");
        assertTrue(result.isSuccess());
        assertEquals(5, result.getValue().getEmployees().size());
    }
}
