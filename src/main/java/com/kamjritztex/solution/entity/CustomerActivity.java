package com.kamjritztex.solution.entity;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kamjritztex.solution.exception.CustomException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "customerActivityMaster")
@Getter
@Setter
@Component
public class CustomerActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int activityId;
    @Column(nullable = false)
    private String activityType;
    @ManyToOne
    @JoinColumn(name = "custId")
    private CustomerDet customer;
    transient List<String> activityTypeList = Arrays.asList("created","purchase","ticketRaise","settlement");
    public void setActivityType(String activityType){
        if(!activityTypeList.contains(activityType)){
            throw new CustomException("C001","Activity type is not valid ");
        }
        this.activityType=activityType;
    }
    public Boolean processed=false;


    
}
