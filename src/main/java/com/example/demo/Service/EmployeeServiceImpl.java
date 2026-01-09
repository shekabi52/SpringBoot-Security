package com.example.demo.Service;

import com.example.demo.Entity.Employee;
import com.example.demo.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
public class EmployeeServiceImpl {
    @Autowired
    public EmployeeRepository employeeRepository;

    @Transactional(propagation = Propagation.REQUIRED , isolation = Isolation.DEFAULT)
    public List<Employee> getAllEmployees() {
        TransactionSynchronizationManager.setCurrentTransactionName("Own");
        System.out.println(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel() + " " + TransactionSynchronizationManager.getCurrentTransactionName());
        return employeeRepository.findAll();
    }

    @Transactional
    public void save(Employee employee) {
        employeeRepository.save(employee);

        System.out.println("Record " + employeeRepository.getById(5));
        try {
            dummyMethod();
        } catch (Exception e) {
            System.out.println("Exception Caught");
        }
    }

    public void dummyMethod()  throws Exception {
        throw new RuntimeException("Exception");
    }
}
