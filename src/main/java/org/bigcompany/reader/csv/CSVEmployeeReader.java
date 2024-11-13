package org.bigcompany.reader.csv;

import org.bigcompany.Result;
import org.bigcompany.reader.EmployeeFileRow;
import org.bigcompany.reader.EmployeeReader;
import org.bigcompany.reader.exception.InvalidStructureException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVEmployeeReader implements EmployeeReader {

    private static final String DELIMITER = ",";
    private static final int MIN_TOKENS = 4;
    private static final int MAX_TOKENS = 5;
    private static final int ID_POSITION = 0;
    private static final int FIRST_NAME_POSITION = 1;
    private static final int LAST_NAME_POSITION = 2;
    private static final int SALARY_POSITION = 3;
    private static final int MANAGER_ID_POSITION = 4;

    @Override
    public Result<List<EmployeeFileRow>> readEmployees(String filePath) {
        List<EmployeeFileRow> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            // Skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(DELIMITER);
                validate(tokens);
                int id = Integer.parseInt(tokens[ID_POSITION]);
                String firstName = tokens[FIRST_NAME_POSITION];
                String lastName = tokens[LAST_NAME_POSITION];
                int salary = Integer.parseInt(tokens[SALARY_POSITION]);
                Integer managerId = tokens.length > MANAGER_ID_POSITION && !tokens[MANAGER_ID_POSITION].isEmpty() ? Integer.parseInt(tokens[MANAGER_ID_POSITION]) : null;
                employees.add(new EmployeeFileRow(id, firstName, lastName, salary, managerId));
            }
        } catch (FileNotFoundException e) {
            return Result.failure("File not exists");
        } catch (InvalidStructureException e) {
            return Result.failure("File structure is invalid");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure(e.getMessage());
        }
        return Result.success(employees);
    }

    private void validate(String[] tokens) {
        if (tokens.length < MIN_TOKENS || tokens.length > MAX_TOKENS) {
            throw new InvalidStructureException("Line: " + Arrays.toString(tokens) + " is invalid");
        }
    }
}
