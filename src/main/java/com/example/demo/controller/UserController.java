package com.example.demo.controller;

import com.example.demo.Entity.Employee;
import com.example.demo.Service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    public EmployeeServiceImpl employeeServiceImpl;

    @GetMapping("/api/v1/welcome")
    public String getWelcomeNote() {
        return "Hello User! Welcome to the Employee Management System";
    }

    @GetMapping("/api/v1/getEmployees")
    public Object getUsers(){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return employeeServiceImpl.getAllEmployees();
    }

    @PostMapping("/api/v1/insertEmployee")
    public void insertEmployee(@RequestBody Employee employee){
        employeeServiceImpl.save(employee);
    }

//    @GetMapping("/api/v1/getEmployees")
//    @Cacheable(value="Invoice")
//    public List<Employee> getEmployee() throws InterruptedException {
//        Thread.sleep(3000);
//        return List.of(new Employee("Abishek", "a@gmail.com"),
//                new Employee("Bharathwaj", "b@gmail.com"),
//                new Employee("Ganu", "g@gmail.com"));
//    }
}
