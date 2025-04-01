package com.example.formadmission;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Connection connect;
    private Button move;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        move = findViewById(R.id.mover);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, submission.class);
                startActivity(intent);
            }
        });
    }

    public void GetTextFromSQL(View v) {
        new GetDataTask().execute();
    }

    private class GetDataTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> studentList = new ArrayList<>();
            ConnectionHelper db = new ConnectionHelper();
            try {
                connect = db.connect_to_db("FormAdmission", "postgres", "leerajenn");
                if (connect != null) {
                    String query = "SELECT first_name FROM studentname;";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) { // Fetch all student_ids
                        studentList.add(rs.getString(1));
                    }
                    rs.close();
                    stmt.close();
                    connect.close();  // Closing the connection after use
                }
            } catch (Exception e) {
                Log.e("DB Error", e.getMessage());
            }
            return studentList;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (!result.isEmpty()) {
                TextView tx1 = findViewById(R.id.textView1);
                TextView tx2 = findViewById(R.id.textView2);

                tx1.setText(result.get(0));  // First student ID
                if (result.size() > 1) {
                    tx2.setText(result.get(1));  // Second student ID (if exists)
                } else {
                    tx2.setText("N/A");
                }
                Toast.makeText(MainActivity.this, "Data Retrieved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to Retrieve Data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
