package com.example.demo.EmployeeDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoginRequest {
    private String username;
    private String password;
}
