package com.example.formadmission;

import java.util.List;

public class Student {
    private int studentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String street;
    private String barangay;
    private String city;
    private String province;
    private String zipCode;
    private String birthMonth;
    private String birthDay;
    private String birthYear;
    private List<String> coursePreferences;  // Store course preferences (e.g., Psychology, Biotechnology, etc.)

    // Constructor to initialize the student
    public Student(int studentId, String firstName, String middleName, String lastName, String gender,
                   String email, String phoneNumber, String street, String barangay, String city,
                   String province, String zipCode, String birthMonth, String birthDay, String birthYear,
                   List<String> coursePreferences) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.barangay = barangay;
        this.city = city;
        this.province = province;
        this.zipCode = zipCode;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.birthYear = birthYear;
        this.coursePreferences = coursePreferences;
    }

    // Getters and Setters for each field
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public List<String> getCoursePreferences() {
        return coursePreferences;
    }

    public void setCoursePreferences(List<String> coursePreferences) {
        this.coursePreferences = coursePreferences;
    }

    // Method to return the full name (you can modify it based on how you want to display)
    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName();  // Customize what to display in the list (for ListView)
    }
}
