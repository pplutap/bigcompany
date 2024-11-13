# Employee Analysis Application

This application reads employee data from a CSV file and performs the following analyses:

- Identifies managers who earn less than they should, and by how much.
- Identifies managers who earn more than they should, and by how much.
- Identifies employees who have a reporting line that is too long, and by how much.

The application uses Java SE and JUnit for tests. It is built using Maven.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Running the Tests](#running-the-tests)
- [Application Usage](#application-usage)
- [Assumptions](#assumptions)
- [Notes](#notes)
- [Troubleshooting](#troubleshooting)

## Prerequisites

- **Java Development Kit (JDK) 17 or higher**: Ensure Java is installed and the `JAVA_HOME` environment variable is set.
- **Apache Maven 3.6 or higher**: Used for building and running the application.

You can verify your installations by running:

```bash
java -version
mvn -version
```

## Building the Project
- Clone the repository or download the project files to your local machine.
- Navigate to the project directory:
```bash
cd employee_analyzer
```
- Compile the project using Maven:
```bash
mvn compile
```

## Running the Application
To run the application, use the Maven exec plugin. Depending on your shell and operating system, you might need to adjust the quotes:
- Unix/Linux/MacOS (Bash shell):
```bash
mvn compile exec:java -Dexec.args="employees.csv" 
```
- Windows Command Prompt:
```bash
mvn compile exec:java -Dexec.args="employees.csv"
```
- Windows PowerShell:
```bash
mvn compile exec:java "-Dexec.args=employees.csv"
```
Ensure that employees.csv is in the project root directory or provide the full path to the file.

## Running the Tests
To run the JUnit tests:
```bash
mvn test
```
This command will compile and execute all tests located in the src/test/java directory.

## Application Usage

### Input File Format
The application expects a CSV file with the following structure:
```csv
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
```
- Id: Integer, unique identifier for the employee.
- firstName: String, employee's first name.
- lastName: String, employee's last name.
- salary: Double, employee's salary.
- managerId: Integer, the Id of the employee's manager. Leave blank for the CEO.

### Output
The application will output the following to the console:
- Managers who earn less than they should:
  - Lists managers who earn less than 20% more than the average salary of their direct subordinates, along with the underpaid amount.
- Managers who earn more than they should:
  - Lists managers who earn more than 50% more than the average salary of their direct subordinates, along with the overpaid amount.
- Employees with reporting lines too long:
  - Lists employees who have more than 4 managers between them and the CEO, along with how many levels too long their reporting line is.

## Assumptions
- Single CEO: There must be exactly one CEO (an employee with no managerId).
- Manager Existence: If an employee references a managerId that doesn't exist, the manager is considered unknown, and the employee is treated as a top-level employee.
- Cycle Detection: The application detects cycles in the management chain to prevent infinite loops.
- Data Validation: Lines with invalid data (e.g., non-numeric IDs or salaries) are skipped with an error message.

## Notes
- Error Handling: The application uses a Result object pattern to handle errors gracefully without throwing exceptions for control flow.
- Immutability: The Employee class is designed to be immutable.
- No External Libraries: The application uses only Java SE and JUnit for tests, as per the requirements.

## Troubleshooting
- IOException: If you encounter an IOException, ensure that the file path is correct and that you have the necessary permissions to read the file.
- Parsing Errors: If there are errors parsing the CSV file, check that all numeric fields (Id, salary, managerId) are properly formatted.
- Multiple CEOs: If the application reports multiple CEOs, verify that only one employee has an empty managerId.