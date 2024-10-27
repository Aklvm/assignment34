package com.kamjritztex.solution.Dto;


import java.time.LocalDateTime;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerDetail {

    @Nullable
    private int custId;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String email;
    @Nullable
    private String phone;
    @Nullable
    private String segment;
    @Nullable
    private String gender;
    @Nullable
    private LocalDateTime dateOfBirth;
    @Nullable
    private Enum<Stage> currentStage;
  

    

}