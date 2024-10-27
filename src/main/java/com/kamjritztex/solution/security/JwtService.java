package com.kamjritztex.solution.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kamjritztex.solution.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${spring.jwt.secretKey}")
  private String secretKey;

  @Value("${Spring.jwt.encryptionKey}")
  private String encryptionKey;

  /**
   * Extracts the username from a JWT token.
   *
   * This method retrieves the subject claim from the provided JWT token,
   * which is expected to be the encrypted username. It then decrypts
   * the username and returns it.
   *
   * @param jwt the JWT token from which to extract the username
   * @return the decrypted username extracted from the token
   * @throws CustomException if there is an error during extraction or decryption
   */

  public String extractUsername(String jwt) {
    try {
      String username = extractClaim(jwt, Claims::getSubject);
      return decrypt(username);
    } catch (Exception e) {
      throw new CustomException(e);
    }
  }

  /**
   * Extracts a specific claim from a JWT token.
   *
   * This method retrieves all claims from the provided JWT token and applies
   * the specified resolver function to extract a particular claim. The
   * resolver function should define how to retrieve the desired claim
   * from the claims object.
   *
   * @param token          the JWT token from which to extract claims
   * @param claimsResolver a function that specifies how to extract the desired
   *                       claim from the {@link Claims} object
   * @param <T>            the type of the claim to be extracted
   * @return the extracted claim of type {@code T}
   */

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extracts all claims from a JWT token.
   *
   * This method parses the provided JWT token using a signing key and retrieves
   * the claims contained in the token's body. It utilizes the JWT library to
   * ensure the token is valid and properly signed.
   *
   * @param token the JWT token from which to extract claims
   * @return a {@link Claims} object containing all claims extracted from the
   *         token
   * @throws JwtException if the token is invalid or if there is an error during
   *                      parsing
   */

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Retrieves the signing key for JWT validation.
   *
   * This method decodes a Base64-encoded secret key and generates an HMAC signing
   * key
   * using the decoded byte array. This signing key is used for validating the
   * integrity
   * of JWT tokens.
   *
   * @return a {@link Key} object representing the HMAC signing key
   * @throws IllegalArgumentException if the secret key is not properly formatted
   *                                  or cannot be decoded
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Checks if a JWT token has expired.
   *
   * This method extracts the expiration date from the provided JWT token and
   * compares it
   * to the current date. It returns {@code true} if the token is expired,
   * and {@code false} otherwise.
   *
   * @param token the JWT token to check for expiration
   * @return {@code true} if the token is expired; {@code false} otherwise
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the expiration date from a JWT token.
   *
   * This method retrieves the expiration claim from the provided JWT token,
   * which indicates when the token will expire. It uses the claim extraction
   * utility to obtain this date.
   *
   * @param token the JWT token from which to extract the expiration date
   * @return a {@link Date} object representing the expiration date of the token
   * @throws JwtException if the token is invalid or if there is an error during
   *                      claim extraction
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Validates a JWT token against the provided user details.
   *
   * This method checks if the username extracted from the JWT token matches
   * the username of the provided user details and verifies that the token
   * has not expired. If both conditions are met, the token is considered valid.
   *
   * @param jwtToken    the JWT token to validate
   * @param userDetails the user details object containing the username to compare
   *                    against
   * @return {@code true} if the token is valid; {@code false} otherwise
   */

  public boolean isValidToken(String jwtToken, UserDetails userDetails) {
    final String username = extractUsername(jwtToken);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  /**
   * Generates a JWT token for the specified user details with additional claims.
   *
   * This method creates a JWT token using the provided user details and
   * any extra claims that should be included in the token. It calls the
   * {@link #buildToken(Map, UserDetails)} method to construct the token.
   *
   * @param extraClaims a map of additional claims to include in the token
   * @param userDetails the user details object representing the user for whom the
   *                    token is generated
   * @return a {@link String} representing the generated JWT token
   */

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails) {
    return buildToken(extraClaims, userDetails);
  }

  public String generateRefreshToken(
      UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails);
  }

  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails

  ) {
    try {
      String usename = encrypt(userDetails.getUsername());
      return Jwts
          .builder()
          .setSubject(usename)
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 10))
          .signWith(getSignInKey(), SignatureAlgorithm.HS512)
          .compact();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String encrypt(String data) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    byte[] encryptedData = cipher.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(encryptedData);
  }

  public String decrypt(String encryptedData) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
    return new String(decryptedData);
  }

}
