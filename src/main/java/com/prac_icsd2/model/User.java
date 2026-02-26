package com.prac_icsd2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wallet_users")
@Data // This provides getters and setters automatically
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fname;
    private String lname;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
}