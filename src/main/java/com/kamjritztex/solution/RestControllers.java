package com.kamjritztex.solution;

import com.kamjritztex.solution.Dto.Message;
import com.kamjritztex.solution.Dto.ProductDto;

import com.kamjritztex.solution.entity.UserCredential;
import com.kamjritztex.solution.exception.CustomException;
import com.kamjritztex.solution.repository.UserRepo;
import com.kamjritztex.solution.security.JwtService;
import com.kamjritztex.solution.service.CrmService;
import com.kamjritztex.solution.service.CustomerService;
import com.kamjritztex.solution.service.Userservice;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kamjritztex.solution.Dto.Credential;
import com.kamjritztex.solution.Dto.CustomerDetail;

@RestController
public class RestControllers {
    @Autowired
    UserCredential userCredential;
    @Autowired
    Message message;
    @Autowired
    UserRepo userRepo;
    @Autowired
    JwtService jwtService;
    @Autowired
    Userservice userservice;
    @Autowired
    CustomerService customerService;
    @Autowired
    CrmService crmService;

    /**
     * Creates a new customer based on the provided customer details.
     *
     * This method handles HTTP POST requests to the "/user/createCustomer"
     * endpoint.
     * It expects a JSON payload representing a {@link CustomerDetail} object in the
     * request body.
     * Upon successful creation of the customer, it returns a response containing a
     * success message.
     *
     * @param custdet the {@link CustomerDetail} object containing customer
     *                information
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         result of the operation
     *         and HTTP status 200 (OK) if successful.
     * @throws Exception if an error occurs during customer creation
     */

    @PostMapping("user/createCustomer")
    public ResponseEntity<Message> createCustomer(@RequestBody CustomerDetail custdet) {
        customerService.createCustomer(custdet);
        message.setMessage(custdet);
        message.setResult("customerCreated Successfully");
        return ResponseEntity.ok(message);
    }

    /**
     * Creates a new user with the specified credentials and assigns the role of
     * "ROLE_USER".
     *
     * This method handles HTTP POST requests to the "/admin/createUser" endpoint.
     * It expects a JSON payload representing a {@link Credential} object in the
     * request body.
     * Upon successful creation of the user, it returns a response containing a
     * success message.
     *
     * @param credential the {@link Credential} object containing the user's login
     *                   information
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         result of the operation
     *         and HTTP status 200 (OK) if successful.
     * @throws Exception if an error occurs during user creation
     */

    @PostMapping("admin/createUser")
    public ResponseEntity<Message> createUser(@RequestBody Credential credential) {

        message.setMessage(userservice.createRole(credential, "ROLE_USER"));
        message.setResult("user created Successfully");
        return ResponseEntity.ok(message);

    }

    /**
     * Creates a new admin user with the specified credentials and assigns the role
     * of "ROLE_ADMIN".
     *
     * This method handles HTTP POST requests to the "/superAdmin/createAdmin"
     * endpoint.
     * It expects a JSON payload representing a {@link Credential} object in the
     * request body.
     * Upon successful creation of the admin user, it returns a response containing
     * a success message.
     *
     * @param credential the {@link Credential} object containing the admin's
     *                   authentication information
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         result of the operation
     *         and an HTTP status of 200 (OK) if successful.
     * @throws Exception if an error occurs during admin creation or role assignment
     */

    @PostMapping("superAdmin/createAdmin")
    public ResponseEntity<Message> createAdmin(@RequestBody Credential credential) {
        message.setMessage(userservice.createRole(credential, "ROLE_ADMIN"));
        message.setResult("admin created Successfully");
        return ResponseEntity.ok(message);
    }

    // @PostMapping("createSuperAdmin")
    // public ResponseEntity<Message> createSuperAdmin(@RequestBody Credential
    // credential) {
    // message.setMessage(userservice.createRole(credential, "ROLE_SUPERADMIN"));
    // message.setResult("superAdmin created Successfully");
    // return ResponseEntity.ok(message);
    // }

    /**
     * Logs in a user via email and validates their credentials.
     *
     * This method handles HTTP POST requests to the "/email/login" endpoint.
     * It expects a JSON payload representing a {@link Credential} object in the
     * request body.
     * Upon successful login, it generates a JWT token and returns it in the
     * response.
     *
     * @param user the {@link Credential} object containing the user's email and
     *             password
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         result of the operation
     *         and an HTTP status of 200 (OK) if successful.
     * @throws CustomException if the email is not found or the password is invalid
     */

    @PostMapping("email/login")
    public ResponseEntity<Message> loginViaMail(@RequestBody Credential user) {
        Optional<UserCredential> userDetail = userservice.getRole(user.getEmailId());
        if (!userDetail.isPresent()) {
            throw new CustomException("C004", "given id not exist singup first");
        }
        if (!userDetail.get().getPassword().equals(user.getPassword())) {
            throw new CustomException("C005", "Password is not valid");
        }
        String jwtToken = jwtService.generateToken(userDetail.get());
        message.setResult("sucess");
        message.setMessage(jwtToken);
        return ResponseEntity.ok(message);
    }

    /**
     * Updates the stage of a customer identified by the given customer ID.
     *
     * This method handles HTTP POST requests to the
     * "/user/updateStage/{custId}/{stage}" endpoint.
     * It expects the customer ID and stage to be provided as path variables.
     * Upon successful update, it returns a response containing a success message.
     *
     * @param custId the ID of the customer whose stage is to be updated
     * @param stage  the new stage to be set for the customer
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         result of the operation
     *         and an HTTP status of 200 (OK) if successful.
     * @throws Exception if an error occurs during the update process
     */

    @PostMapping("/user/updateStage/{custId}/{stage}")
    public ResponseEntity<Message> updateStage(@PathVariable int custId, @PathVariable String stage) {
        crmService.updateStage(custId, stage);
        message.setMessage("customer stage updated to " + stage);
        message.setResult("success");
        return ResponseEntity.ok(message);
    }

    /**
     * Retrieves product recommendations for a specified customer.
     *
     * This method handles HTTP GET requests to the
     * "/user/productRecommendation/{custId}" endpoint.
     * It expects the customer ID to be provided as a path variable.
     * Upon successful retrieval, it returns a response containing the recommended
     * products.
     *
     * @param custId the ID of the customer for whom to fetch product
     *               recommendations
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         recommended products
     *         and an HTTP status of 200 (OK) if successful.
     * @throws Exception if an error occurs during the retrieval process
     */

    @GetMapping("/user/productRecommendation/{custId}")
    public ResponseEntity<Message> getProductRecommendation(@PathVariable int custId) {
        message.setMessage(crmService.getProductRecommendation(custId));
        message.setResult("Recommended Product fetch succesfully");
        return ResponseEntity.ok(message);

    }

    /**
     * Creates a new activity for a specified customer.
     *
     * This method handles HTTP POST requests to the
     * "/user/createActivity/{custId}/{activityType}" endpoint.
     * It expects the customer ID and activity type to be provided as path
     * variables.
     * Upon successful creation of the activity, it returns a response indicating
     * success.
     *
     * @param custId       the ID of the customer for whom the activity is being
     *                     created
     * @param activityType the type of activity to be logged for the customer
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         result of the operation
     *         and an HTTP status of 200 (OK) if successful.
     * @throws Exception if an error occurs during the activity creation process
     */

    @PostMapping("/user/createActitvity/{custId}/{activityType}")
    public ResponseEntity<Message> createActivity(@PathVariable int custId, @PathVariable String activityType) {
        crmService.createActitvity(custId, activityType);
        message.setMessage("customer activity log ");
        message.setResult("success");
        return ResponseEntity.ok(message);

    }

    /**
     * Creates a new product using the provided product details.
     *
     * This method handles HTTP POST requests to the "/admin/createProduct"
     * endpoint.
     * It expects a JSON payload representing a {@link ProductDto} object in the
     * request body.
     * Upon successful creation of the product, it returns a response containing a
     * success message.
     *
     * @param productDto the {@link ProductDto} object containing the details of the
     *                   product to be created
     * @return a {@link ResponseEntity} containing a {@link Message} object with the
     *         result of the operation
     *         and an HTTP status of 200 (OK) if successful.
     * @throws Exception if an error occurs during product creation
     */

    @PostMapping("/admin/createProduct")
    public ResponseEntity<Message> createProduct(@RequestBody ProductDto productDto) {
        crmService.createProduct(productDto);
        message.setMessage(productDto);
        message.setResult("successfully created product");
        return ResponseEntity.ok(message);

    }

}
