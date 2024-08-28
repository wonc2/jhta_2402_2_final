package org.example.jhta_2402_2_final.model.dto;

import lombok.Data;

import java.time.LocalDate;


//테스트 용
@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private LocalDate hireDate;
}

