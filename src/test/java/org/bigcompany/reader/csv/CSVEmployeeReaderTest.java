package org.bigcompany.reader.csv;

import org.bigcompany.Result;
import org.bigcompany.reader.EmployeeFileRow;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVEmployeeReaderTest {

    private CSVEmployeeReader reader = new CSVEmployeeReader();

    @Test
    void shouldReadRowsFromCSVFile() {
        Result<List<EmployeeFileRow>> result = reader.readEmployees("src/main/resources/employees.csv");
        assertTrue(result.isSuccess());
        assertEquals(5, result.getValue().size());
        assertTrue(result.getValue().containsAll(
                List.of(
                        new EmployeeFileRow(123, "Joe", "Doe", 60000, null),
                        new EmployeeFileRow(124, "Martin", "Chekov", 45000, 123),
                        new EmployeeFileRow(125, "Bob", "Ronstad", 47000, 123),
                        new EmployeeFileRow(300, "Alice", "Hasacat", 50000, 124),
                        new EmployeeFileRow(305, "Brett", "Hardleaf", 34000, 300)
                )
        ));
    }

    @Test
    void shouldReturnFailureIfFileNotExists() {
        Result<List<EmployeeFileRow>> result = reader.readEmployees("src/main/resources/bad_name.csv");
        assertFalse(result.isSuccess());
        assertEquals("File not exists", result.getError());
    }

    @Test
    void shouldReturnFailureIfLineInCSVIsTooShort() {
        Result<List<EmployeeFileRow>> result = reader.readEmployees("src/main/resources/not_enough_tokens_in_line.csv");
        assertFalse(result.isSuccess());
        assertEquals("File structure is invalid", result.getError());
    }

    @Test
    void shouldReturnFailureIfLineInCSVIsTooLong() {
        Result<List<EmployeeFileRow>> result = reader.readEmployees("src/main/resources/too_many_tokens_in_line.csv");
        assertFalse(result.isSuccess());
        assertEquals("File structure is invalid", result.getError());
    }
}
