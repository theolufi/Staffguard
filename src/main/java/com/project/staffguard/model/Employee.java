package com.project.staffguard.model;

import com.project.staffguard.config.SalaryEncryptor;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String department;

    @Convert(converter = SalaryEncryptor.class)
    @Column(nullable = false)
    private String salary; // Encrypted value
}