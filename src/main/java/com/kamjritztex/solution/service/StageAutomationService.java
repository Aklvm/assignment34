package com.kamjritztex.solution.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamjritztex.solution.Dto.Stage;
import com.kamjritztex.solution.entity.CustomerActivity;
import com.kamjritztex.solution.entity.CustomerDet;
import com.kamjritztex.solution.exception.CustomException;
import com.kamjritztex.solution.repository.CustomerActivityRepo;
import com.kamjritztex.solution.repository.CustomerDetRepo;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class StageAutomationService {
    @Autowired
    CustomerActivity customerActivity;
    @Autowired
    CustomerActivityRepo customerActivityRepo;
    @Autowired
    CustomerDetRepo detRepo;

    /**
     * Starts a background scheduler that periodically processes customer
     * activities.
     *
     * This method creates a new thread that runs continuously in a loop. Every
     * minute,
     * it retrieves unprocessed customer activities from the repository and updates
     * the
     * customer's stage based on the type of activity.
     * 
     * If any exceptions occur during processing, a {@link CustomException} is
     * thrown.
     */

    @PostConstruct
    private void startScheduler() {
        Thread scheduler = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 60);
                        List<CustomerActivity> customerAct = customerActivityRepo.findByProcessed(false);
                        processCustomerActivityToUpdateStage(customerAct);

                    } catch (Exception e) {
                        throw new CustomException(e);
                    }
                }
            }

            /**
             * Processes a list of customer activities to update their stages based on the
             * activity type.
             *
             * This method evaluates each {@link CustomerActivity} in the provided list and
             * updates the
             * customer's stage according to the type of activity.
             * The following mappings are used:
             * <ul>
             * <li>If the activity type is "purchase", the customer's stage is updated to
             * "ACTIVE".</li>
             * <li>If the activity type is "ticketRaise", the customer's stage is updated to
             * "ATRISK".</li>
             * <li>If the activity type is "Settlement", the customer's stage is updated to
             * "CHURNED".</li>
             * </ul>
             * 
             * @param customerAct a list of {@link CustomerActivity} objects to be processed
             */
          
            private void processCustomerActivityToUpdateStage(List<CustomerActivity> customerAct) {
                customerAct.forEach(custAct -> {
                    String activityType = custAct.getActivityType().toLowerCase();
                    
                    if (activityType.equals("purchase")) {
                        updateCustStage(custAct, "ACTIVE");
                    } else if (activityType.equals("ticketraise")) {
                        updateCustStage(custAct, "ATRISK");
                    } else if (activityType.equals("settlement")) {
                        updateCustStage(custAct, "CHURNED");
                    } else if (activityType.equals("created")) {
                        custAct.setProcessed(true);
                        customerActivityRepo.save(custAct); 
                    }
                });
            }

            /**
             * Updates the stage of a customer associated with a given customer activity.
             *
             * This method sets the current stage of the customer to the specified type and
             * marks the
             * associated customer activity as processed. It updates the customer record in
             * the database
             * and saves the modified activity status.
             *
             * @param custAct the {@link CustomerActivity} object associated with the
             *                customer
             * @param type    the new stage to set for the customer, which should match a
             *                value in the {@link Stage} enum
             */

             @Transactional
            private void updateCustStage(CustomerActivity custAct, String type) {
                custAct.setProcessed(true);
                customerActivityRepo.save(custAct);
                CustomerDet det = custAct.getCustomer();
                det.setCurrentStage(type);
                detRepo.save(det);
                
            }
        });
        scheduler.start();
    }

}
