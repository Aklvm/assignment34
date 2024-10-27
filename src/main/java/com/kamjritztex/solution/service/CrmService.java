package com.kamjritztex.solution.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamjritztex.solution.Dto.ProductDto;
import com.kamjritztex.solution.Dto.Stage;

import com.kamjritztex.solution.entity.CustomerActivity;
import com.kamjritztex.solution.entity.CustomerDet;
import com.kamjritztex.solution.entity.ProductEntity;
import com.kamjritztex.solution.exception.CustomException;
import com.kamjritztex.solution.repository.CustomerActivityRepo;
import com.kamjritztex.solution.repository.CustomerDetRepo;
import com.kamjritztex.solution.repository.ProductRepo;

@Service
public class CrmService {
    @Autowired
    CustomerDetRepo customerDetRepo;
    @Autowired
    CustomerActivityRepo customerActivityRepo;
    @Autowired
    CustomerActivity customerActivity;
    @Autowired
    ProductEntity productEntity;
    @Autowired
    ProductRepo productRepo;

    /**
     * Creates a new activity for a specified customer.
     *
     * This method checks if a customer exists with the given customer ID.
     * If the customer is found, it creates a new {@link CustomerActivity}
     * with the specified activity type and associates it with the customer.
     * If the customer ID is not valid, it throws a {@link CustomException}.
     *
     * @param custId       the ID of the customer for whom the activity is to be
     *                     created
     * @param activityType the type of activity to log for the customer
     * @throws CustomException if the given customer ID is not valid
     */

    public void createActitvity(int custId, String activityType) {
        Optional<CustomerDet> custDet = customerDetRepo.findById(custId);
        if (!custDet.isPresent()) {
            throw new CustomException("C002", "Given customer Id is not valid");
        }
        customerActivity.setActivityType(activityType);
        customerActivity.setCustomer(custDet.get());
        customerActivityRepo.save(customerActivity);

    }

    /**
     * Updates the current stage of a specified customer.
     *
     * This method checks if a customer exists with the given customer ID.
     * If the customer is found, it updates their current stage to the specified
     * value.
     * If the customer ID is not valid, it throws a {@link CustomException}.
     *
     * @param custId the ID of the customer whose stage is to be updated
     * @param stage  the new stage to set for the customer, which should match a
     *               value in the {@link Stage} enum
     * @throws CustomException          if the given customer ID is not valid
     * @throws IllegalArgumentException if the provided stage is not a valid value
     *                                  in the {@link Stage} enum
     */

    public void updateStage(int custId, String stage) {
        Optional<CustomerDet> custDet = customerDetRepo.findById(custId);
        if (!custDet.isPresent()) {
            throw new CustomException("C002", "Given customer Id is not valid");
        }
        custDet.get().setCurrentStage(Stage.valueOf(stage).name());
        customerDetRepo.save(custDet.get());

    }

    /**
     * Creates a new product using the provided product details.
     *
     * This method populates a {@link ProductEntity} with the details from the
     * provided {@link ProductDto} and saves it to the product repository.
     *
     * @param productDto the {@link ProductDto} object containing the details of the
     *                   product to be created
     * @throws IllegalArgumentException if the product ID is invalid or if any
     *                                  required fields are missing
     */

    public void createProduct(ProductDto productDto) {
        productEntity.setAvailable(productDto.isAvailable());
        productEntity.setId(productDto.getId());
        productEntity.setBestSeller(productDto.isBestSeller());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setNoOfPurchase(productDto.getNoOfPurchase());
        productEntity.setPrice(productEntity.getPrice());
        productEntity.setRating(productDto.getRating());
        productEntity.setTittle(productDto.getTittle());
        productRepo.save(productEntity);
    }

    /**
     * Retrieves product recommendations based on the current stage of the specified
     * customer.
     *
     * This method checks the current stage of the customer identified by the given
     * customer ID.
     * Based on the customer's stage, it returns a list of recommended products:
     * <ul>
     * <li>If the customer stage is {@link Stage#NEW}, it returns the top-rated
     * products.</li>
     * <li>If the customer stage is {@link Stage#ACTIVE}, further analysis is needed
     * to provide recommendations.</li>
     * <li>If the customer stage is {@link Stage#ATRISK}, it returns the
     * best-selling products.</li>
     * <li>For any other stage, it returns all available products.</li>
     * </ul>
     *
     * @param custId the ID of the customer for whom product recommendations are to
     *               be retrieved
     * @return a list of {@link ProductEntity} objects representing the recommended
     *         products
     * @throws CustomException if the given customer ID is not valid
     */

    public List<ProductEntity> getProductRecommendation(int custId) {
        Optional<CustomerDet> custDet = customerDetRepo.findById(custId);
        if (!custDet.isPresent()) {
            throw new CustomException("C002", "Given customer Id is not valid");
        }
        if (custDet.get().getCurrentStage().equalsIgnoreCase("new")) {
            return productRepo.findTopByOrderByRatingDescNoOfPurchaseDesc();
        } else if (custDet.get().getCurrentStage().equalsIgnoreCase("active")) {
            // get all activity analyse all purchase
            // get product id and fetch related productId from mapping;
            // get product detail
            return null;
        } else if (custDet.get().getCurrentStage().equalsIgnoreCase(("atRisk"))) {
            return productRepo.findBestSellerProduct();
        }
        return productRepo.findAll();

    }

}
