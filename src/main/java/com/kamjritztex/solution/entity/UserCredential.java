package com.kamjritztex.solution.entity;

import io.micrometer.common.lang.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "kuser_credential") 
@Component
public class UserCredential implements UserDetails{
   @NonNull @Id @Column(updatable=false, unique = true) 
  private String userId;
  private String password;
  private String role;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime modifiedAt;
  private boolean accountNonExpired =true;
  private  boolean accountNonLocked=true;
  private boolean enabled=true;
  private boolean credentialExpired=false;
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth =new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(role));
        return auth;
       

  }
  @Override
  public String getUsername() {
       return this.userId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.accountNonExpired;
 }

 public boolean isAccountNonLocked() {
   
    return accountNonLocked;
 }

 public boolean isCredentialsNonExpired() {
    return credentialExpired;
 }

 public boolean isEnabled() {
    return enabled;
 }


    
}
