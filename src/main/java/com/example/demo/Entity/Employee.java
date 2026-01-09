package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Employee implements Serializable {
    @Id
    public int id;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String name;
    public String email;

    public Employee() {
    }

    public Employee(int i, String name, String mail) {
        this.id = i;
        this.name = name;
        this.email = mail;
    }
}
