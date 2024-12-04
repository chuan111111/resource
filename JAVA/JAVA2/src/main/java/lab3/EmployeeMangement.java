package lab3;

import java.util.ArrayList;
import java.util.List;

class EmployeeMangement {
    private List<Employee> employees = new ArrayList<>();


    public void addEmployee(String name, int age) {
        for (Employee emp : employees) {
            if (emp.getName().equals(name)) {
                return; // Employee already exists, don't add a new one
            }
        }
        employees.add(new Employee(name, age));
    }


    public void deleteEmployee(String name) {
        employees.removeIf(employee -> employee.getName().equals(name));
    }



    public void printAllEmployees() {
        employees.stream()
                .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                .forEach(System.out::println);
    }
}