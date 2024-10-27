package com.kamjritztex.solution.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamjritztex.solution.Dto.Credential;
import com.kamjritztex.solution.entity.UserCredential;
import com.kamjritztex.solution.exception.CustomException;
import com.kamjritztex.solution.repository.UserRepo;

@Service
public class Userservice {
    @Autowired
    UserCredential userCredential;

    @Autowired
    UserRepo userRepo;

    /**
     * Creates a new user role for the specified credentials.
     *
     * This method checks if the provided email is associated with an existing role.
     * If it is not, it throws a {@link CustomException}. Otherwise, it creates a
     * new
     * {@link UserCredential} object, sets its properties based on the provided
     * credentials,
     * and saves it to the repository.
     *
     * @param credential the {@link Credential} object containing the user's email
     *                   and password
     * @param role       the role to be assigned to the user
     * @return the created {@link UserCredential} object containing the user's
     *         details and assigned role
     * @throws CustomException if the provided email is not associated with any
     *                         existing role
     */

    public UserCredential createRole(Credential credential, String role) {
        if (getRole(credential.getEmailId()).isPresent()) {
            throw new CustomException("C003", "Given Username not associate with any role");
        }
        userCredential.setUserId(credential.getEmailId());
        userCredential.setPassword(credential.getPassword());
        userCredential.setRole(role);
        userRepo.save(userCredential);
        return userCredential;
    }

    /**
     * Retrieves the user credentials associated with the specified email ID.
     *
     * This method queries the user repository to find a {@link UserCredential}
     * based on the provided email ID. It returns an {@link Optional} that may
     * contain
     * the user credentials if found, or be empty if no user is associated with that
     * email.
     *
     * @param emailId the email ID of the user whose credentials are to be retrieved
     * @return an {@link Optional<UserCredential>} containing the user's credentials
     *         if found,
     *         or an empty Optional if no user exists with the given email ID
     */
    
    public Optional<UserCredential> getRole(String emailId) {
        Optional<UserCredential> userDetail = userRepo.findById(emailId);
        return userDetail;
    }
}
