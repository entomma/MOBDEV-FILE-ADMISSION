package com.example.formadmission;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubmissionActivity extends AppCompatActivity {
    private EditText firstName, middleName, lastName, phoneNumber, email, street, barangay, city, province, zipCode;
    private Spinner monthSpinner, daySpinner, yearSpinner;
    private RadioGroup genderGroup;
    private CheckBox cs, ba, me, ee, ce, med, law, psy, ah, bio;
    private Button submitButton;
    ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        connectionHelper = new ConnectionHelper();

        // Initializing input fields
        firstName = findViewById(R.id.firstName);
        middleName = findViewById(R.id.middleName);
        lastName = findViewById(R.id.lastName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailAddress);
        street = findViewById(R.id.streetAddress);
        barangay = findViewById(R.id.barangay);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        zipCode = findViewById(R.id.zipCode);

        // Spinners for birth date
        monthSpinner = findViewById(R.id.birthMonth);
        daySpinner = findViewById(R.id.birthDay);
        yearSpinner = findViewById(R.id.birthYear);

        // Radio Group for gender
        genderGroup = findViewById(R.id.genderGroup);

        // Checkboxes for course preference
        cs = findViewById(R.id.course_cs);
        ba = findViewById(R.id.course_business_admin);
        me = findViewById(R.id.course_mechanical_engg);
        ee = findViewById(R.id.course_electrical_engg);
        ce = findViewById(R.id.course_civil_engg);
        med = findViewById(R.id.course_medicine);
        law = findViewById(R.id.course_law);
        psy = findViewById(R.id.course_psychology);
        ah = findViewById(R.id.course_arts_humanities);
        bio = findViewById(R.id.course_biotechnology);

        // Submit Button
        submitButton = findViewById(R.id.submitBtn);
        submitButton.setOnClickListener(v -> validateAndSubmit());
    }

    private void fetchStudentData(int studentId) {
        new Thread(() -> {
            try {
                // Establish a connection to the PostgreSQL database
                Connection conn = DriverManager.getConnection("jdbc:postgresql://your_host:5432/FormAdmission", "your_user", "your_password");

                // SQL query to retrieve student data
                String query = "SELECT s.student_id, sn.first_name, sn.middle_name, sn.last_name, sn.gender, " +
                        "sc.email, sc.phone_number, sb.date_of_births, sa.street, sa.barangay, sa.city, sa.zipcode, sa.province, " +
                        "cp.course_name " +
                        "FROM students s " +
                        "JOIN studentname sn ON s.student_id = sn.student_id " +
                        "JOIN studentcontact sc ON s.student_id = sc.student_id " +
                        "JOIN studentbirth sb ON s.student_id = sb.student_id " +
                        "JOIN studentaddress sa ON s.student_id = sa.student_id " +
                        "JOIN studentpreference cp ON s.student_id = cp.student_id " +
                        "WHERE s.student_id = ?";

                // Prepare the statement and execute the query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, studentId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Retrieve student data from the result set
                    String dbFirstName = rs.getString("first_name");
                    String dbMiddleName = rs.getString("middle_name");
                    String dbLastName = rs.getString("last_name");
                    String dbGender = rs.getString("gender");
                    String dbEmail = rs.getString("email");
                    String dbPhone = rs.getString("phone_number");
                    String dbBirthDate = rs.getString("date_of_births");
                    String dbStreet = rs.getString("street");
                    String dbBarangay = rs.getString("barangay");
                    String dbCity = rs.getString("city");
                    String dbProvince = rs.getString("province");
                    String dbZipCode = rs.getString("zipcode");

                    // Assuming course preferences are stored as a comma-separated list
                    List<String> coursePreferences = new ArrayList<>();
                    coursePreferences.add(rs.getString("course_name"));

                    // Split birth date to get individual parts (year, month, day)
                    String[] birthDateParts = dbBirthDate.split("-");
                    String birthYear = birthDateParts[0];
                    String birthMonth = birthDateParts[1];
                    String birthDay = birthDateParts[2];

                    // Run UI update on the main thread
                    runOnUiThread(() -> {
                        // Set the EditText fields with the correct values
                        firstName.setText(dbFirstName);
                        middleName.setText(dbMiddleName);
                        lastName.setText(dbLastName);
                        email.setText(dbEmail);
                        phoneNumber.setText(dbPhone);
                        street.setText(dbStreet);  // Fix: updated to use dbStreet
                        barangay.setText(dbBarangay);  // Fix: updated to use dbBarangay
                        city.setText(dbCity);  // Fix: updated to use dbCity
                        zipCode.setText(dbZipCode);  // Fix: updated to use dbZipCode
                        province.setText(dbProvince);  // Fix: updated to use dbProvince

                        // Set radio button for gender
                        RadioButton male = findViewById(R.id.radioMale);
                        RadioButton female = findViewById(R.id.radioFemale);
                        if ("Male".equalsIgnoreCase(dbGender)) {
                            male.setChecked(true);
                        } else if ("Female".equalsIgnoreCase(dbGender)) {
                            female.setChecked(true);
                        }

                        // Set the birthdate spinners
                        monthSpinner.setSelection(getIndex(monthSpinner, birthMonth));
                        daySpinner.setSelection(getIndex(daySpinner, birthDay));
                        yearSpinner.setSelection(getIndex(yearSpinner, birthYear));

                        // Set course preferences
                        setCoursePreferences(coursePreferences);
                    });
                }

                // Close resources
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();  // Handle errors appropriately
            }
        }).start();
    }

    private void setCoursePreferences(List<String> coursePreferences) {
        // Create a mapping between course names and checkbox IDs
        List<CheckBox> courseCheckBoxes = Arrays.asList(cs, ba, me, ee, ce, med, law, psy, ah, bio);
        List<String> availableCourses = Arrays.asList("Computer Science", "Business Administration", "Mechanical Engineering", "Electrical Engineering",
                "Civil Engineering", "Medicine", "Law", "Psychology", "Arts and Humanities", "Biotechnology");

        // Loop through course preferences and set the corresponding checkboxes to checked
        for (int i = 0; i < availableCourses.size(); i++) {
            if (coursePreferences.contains(availableCourses.get(i))) {
                courseCheckBoxes.get(i).setChecked(true);
            }
        }
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().trim().equalsIgnoreCase(value.trim())) {
                return i;
            }
        }
        return 0; // Default to first item if not found
    }

    private void validateAndSubmit() {
        String first = firstName.getText().toString().trim();
        String middle = middleName.getText().toString().trim();
        String last = lastName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String streetText = street.getText().toString().trim();
        String barangayText = barangay.getText().toString().trim();
        String cityText = city.getText().toString().trim();
        String provinceText = province.getText().toString().trim();
        String zipText = zipCode.getText().toString().trim();

        // Birthdate validation
        String birthMonth = monthSpinner.getSelectedItem().toString();
        String birthDay = daySpinner.getSelectedItem().toString();
        String birthYear = yearSpinner.getSelectedItem().toString();

        // Gender validation
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender.getText().toString();

        // Contact validation
        if (!phone.matches("^09\\d{9}$")) {
            Toast.makeText(this, "Phone number must start with 09 and be 11 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!emailText.matches("^.+@(gmail\\.com|yahoo\\.com)$")) {
            Toast.makeText(this, "Email must end with @gmail.com or @yahoo.com", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!zipText.matches("\\d+")) {
            Toast.makeText(this, "Zip code must be numeric", Toast.LENGTH_SHORT).show();
            return;
        }

        // Course selection
        List<CheckBox> courseBoxes = Arrays.asList(cs, ba, me, ee, ce, med, law, psy, ah, bio);
        StringBuilder selectedCourses = new StringBuilder();
        for (CheckBox checkBox : courseBoxes) {
            if (checkBox.isChecked()) {
                selectedCourses.append(checkBox.getText().toString()).append(", ");
            }
        }
        if (selectedCourses.length() == 0) {
            Toast.makeText(this, "Select at least one course", Toast.LENGTH_SHORT).show();
            return;
        }

        new SaveStudentTask().execute(first, middle, last, birthMonth, birthDay, birthYear, gender, phone, emailText, streetText, barangayText, cityText, provinceText, zipText, selectedCourses.toString());
    }

    private class SaveStudentTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Connection conn = connectionHelper.connect_to_db("FormAdmission", "postgres", "leerajenn");
                if (conn == null) return false;

                String query = "INSERT INTO studentname (first_name, middle_name, last_name) VALUES (?, ?, ?);";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, params[0]);
                stmt.setString(2, params[1]);
                stmt.setString(3, params[2]);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
                return true;
            } catch (Exception e) {
                Log.e("DB Error", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(SubmissionActivity.this, "Student Saved Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SubmissionActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(SubmissionActivity.this, "Error saving student", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
