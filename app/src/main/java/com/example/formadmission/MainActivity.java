package com.example.formadmission;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView studentListView;
    List<Student> studentList = new ArrayList<>(); // Changed to hold Student objects
    ArrayAdapter<Student> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentListView = findViewById(R.id.studentListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        studentListView.setAdapter(adapter);

        new GetDataTask().execute();

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (studentList == null || studentList.isEmpty()) {
                    Log.e("onItemClick", "Error: studentList is null or empty!");
                    return;
                }

                Student student = studentList.get(position);
                if (student == null) {
                    Log.e("onItemClick", "Error: Student object at position " + position + " is null");
                    return;
                }

                int studentId = student.getStudentId();  // Now it accesses the Student object
                Log.d("onItemClick", "Selected Student ID: " + studentId);

                Intent intent = new Intent(MainActivity.this, SubmissionActivity.class);
                intent.putExtra("student_id", studentId);
                startActivity(intent);
            }
        });
    }



    private class GetDataTask extends AsyncTask<Void, Void, List<Student>> {
        @Override
        protected List<Student> doInBackground(Void... voids) {
            List<Student> students = new ArrayList<>();
            ConnectionHelper db = new ConnectionHelper();
            try {
                Connection connect = db.connect_to_db("FormAdmission", "postgres", "leerajenn");
                if (connect != null) {
                    String query = "SELECT s.student_id, sn.first_name, sn.middle_name, sn.last_name FROM students s JOIN studentname sn ON s.student_id = sn.student_id;";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        int studentId = rs.getInt("student_id");
                        String firstName = rs.getString("first_name");
                        String middleName = rs.getString("middle_name");
                        String lastName = rs.getString("last_name");

                        // Create a new Student object and add it to the list
                        Student student = new Student(studentId, firstName, middleName, lastName, null, null, null, null, null, null, null, null, null, null, null, null);
                        students.add(student);
                    }
                    rs.close();
                    stmt.close();
                    connect.close();
                } else {
                    Log.e("DB Error", "Connection failed");
                }
            } catch (Exception e) {
                Log.e("DB Error", "Error: " + e.getMessage());
            }
            return students;
        }

        @Override
        protected void onPostExecute(List<Student> result) {
            if (!result.isEmpty()) {
                studentList.clear();
                studentList.addAll(result);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "No students found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
