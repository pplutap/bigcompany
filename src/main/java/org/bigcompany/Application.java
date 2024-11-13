package org.bigcompany;

import org.bigcompany.analyzer.EmployeeAnalyzer;
import org.bigcompany.processor.EmployeeFileProcessor;
import org.bigcompany.reader.EmployeeReader;
import org.bigcompany.reader.csv.CSVEmployeeReader;

public class Application {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the path to the employees CSV file.");
            return;
        }

        EmployeeReader fileReader = new CSVEmployeeReader();
        EmployeeFileProcessor fileProcessor = new EmployeeFileProcessor(fileReader);
        EmployeeAnalyzer employeeAnalyzer = new EmployeeAnalyzer(fileProcessor);
        employeeAnalyzer.analyzeFile(args[0]);
    }
}
