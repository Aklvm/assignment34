package com.kamjritztex.solution.entity;

import java.time.LocalDateTime;

import com.kamjritztex.solution.Dto.Stage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "custMaster")

public class CustomerDet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int custid;
    private String firstName;
    private String lastName;
    private String emailId;
    private String Segment;
    private String gender;
    private LocalDateTime dateOfBirth;
    @Column(nullable = false,updatable= true)
    private  String currentStage;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private  CustomerActivity customerActivity;
}
