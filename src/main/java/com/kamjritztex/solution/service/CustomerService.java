package com.kamjritztex.solution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamjritztex.solution.Dto.CustomerDetail;
import com.kamjritztex.solution.Dto.Stage;
import com.kamjritztex.solution.entity.CustomerActivity;
import com.kamjritztex.solution.entity.CustomerDet;
import com.kamjritztex.solution.repository.CustomerDetRepo;

@Service
public class CustomerService {
      
    @Autowired
    CustomerDetRepo customerDetRepo;
  
    
    public void createCustomer(CustomerDetail customerDetails){
       CustomerDet customerDet = fromDtoToEntity(customerDetails);
       customerDetRepo.save(customerDet);
       customerDetails.setCustId(customerDet.getCustid());

    }

    private CustomerDet fromDtoToEntity(CustomerDetail customerDetails) {
        CustomerDet customerDet = new CustomerDet();
        customerDet.setCustid(customerDetails.getCustId());
        customerDet.setEmailId(customerDetails.getEmail());
        customerDet.setCurrentStage(Stage.NEW.name());
        customerDetails.setCurrentStage(Stage.NEW);
        customerDet.setFirstName(customerDetails.getFirstName());
        customerDet.setLastName(customerDetails.getLastName());
        customerDet.setGender(customerDetails.getGender());
        customerDet.setDateOfBirth(customerDetails.getDateOfBirth());
        customerDet.setSegment(customerDetails.getSegment());
        customerDet.setCustomerActivity(fromDtoToEntity());
        return customerDet;
    }
    private com.kamjritztex.solution.entity.CustomerActivity fromDtoToEntity(){
        com.kamjritztex.solution.entity.CustomerActivity custActivity=new CustomerActivity();
         custActivity.setActivityType("created");
          return custActivity;
    }
}
