package com.tericcabrel.authorization.dtos;

import com.tericcabrel.authorization.constraints.FieldMatch;

import javax.validation.constraints.*;

import com.tericcabrel.authorization.models.Coordinates;
import com.tericcabrel.authorization.models.Role;

import java.util.HashSet;
import java.util.Set;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
public class UserDto {
    @NotBlank(message = "The first name is required")
    private String firstName;

    @NotBlank(message = "The last name is required")
    private String lastName;

    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;

    @Size(min = 6, message = "Must be at least 6 characters")
    private String password;

    @NotBlank(message = "The timezone is required")
    private String timezone;

    @NotBlank(message = "This field is required")
    private String confirmPassword;

    private String gender;

    private String avatar;

    private boolean enabled;

    private boolean confirmed;

    private Coordinates coordinates;

    private Set<Role> roles;

    public UserDto() {
        roles = new HashSet<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getTimezone() {
        return timezone;
    }

    public UserDto setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserDto setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public UserDto setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public UserDto setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserDto setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public UserDto setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public UserDto setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public UserDto setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }
}