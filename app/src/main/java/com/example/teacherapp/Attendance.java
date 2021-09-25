package com.example.teacherapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Attendance extends AppCompatActivity {

    private DatabaseReference reference;


    private RecyclerView recyclerView;
    studentAdapter adapter;
    //TreeMap<String, Object> arr;
    private DatabaseReference referenceAttendance;
    private classModel model;
    private String [] date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //getting relevant model class
        model = passModel.getModel();
        reference = passModel.getReference();


        if (getSupportActionBar() != null) {     // calling the action bar
            ActionBar actionBar = getSupportActionBar();


            // Customize the back button
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.purple_500));
            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setTitle(model.getName());

            // showing the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Toast.makeText(this, "Loading " + model.getName(), Toast.LENGTH_SHORT).show();

        String pid = passModel.getModel().getId();
        assert pid != null;

        reference = reference.child(pid).child("StudentList");

        //creating reference for attendance

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String onlineUserID = null;
        if (mUser != null) {
            onlineUserID = mUser.getUid();
        } else {
            Toast.makeText(this, "Error Attendance authentication", Toast.LENGTH_SHORT).show();
            return;
        }
        referenceAttendance = FirebaseDatabase.getInstance().getReference().child("attendance").child(onlineUserID);


        //adding current date
        EditText EditDate = findViewById(R.id.attendanceDate);
        EditDate.setText(DateFormat.getDateInstance().format(new Date()));

        fetchStudent();

        storeAttendance();


    }


    //back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addStudent:
                addStudent();
                return true;
            case R.id.analyze:
                analyseAttendance();
                startActivity(new Intent(this, LineGraph.class));
                return true;
        }

        startActivity(new Intent(this, classView.class));
        return super.onOptionsItemSelected(item);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_attendance_student, menu);
        return true;
    }


    private void addStudent() {

        ProgressDialog saveloader = new ProgressDialog(this);

        AlertDialog.Builder myDialog = new AlertDialog.Builder(Attendance.this);
        LayoutInflater inflater = LayoutInflater.from(Attendance.this);
        View myView = inflater.inflate(R.layout.add_student, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        dialog.show();


        EditText name = myView.findViewById(R.id.studentName);
        EditText sid = myView.findViewById(R.id.studentId);
        Button save = myView.findViewById(R.id.saveClass);
        Button cancel = myView.findViewById(R.id.addClass);

        //cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        //saveButton

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sName = name.getText().toString().trim();
                String sSid = sid.getText().toString().trim();
                String id = reference.push().getKey();


                if (TextUtils.isEmpty(sSid)) {
                    int x = (int) (Math.random() * 1000);
                    sSid = "SID" + x;
                }

                if (TextUtils.isEmpty(sName)) {
                    name.setError("Student name is required");
                    return;
                } else {
                    saveloader.setMessage("Saving the new student");
                    saveloader.setCanceledOnTouchOutside(false);
                    saveloader.show();

                    studentModel smodel = new studentModel(sName, sSid, id);
                    reference.child(id).setValue(smodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Attendance.this, "Class Saved Successfully!", Toast.LENGTH_SHORT).show();
                                saveloader.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(Attendance.this, "Failed : " + error, Toast.LENGTH_SHORT).show();
                                saveloader.dismiss();
                            }
                        }
                    });

                }

                dialog.dismiss();
            }
        });


    }


    private void fetchStudent() {
        recyclerView = findViewById(R.id.recyclerClass);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<studentModel> options = new FirebaseRecyclerOptions.Builder<studentModel>()
                .setQuery(reference, studentModel.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new studentAdapter(options, this);
        // Connecting Adapter class with the Recycler view
        recyclerView.setAdapter(adapter);
    }


    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void storeAttendance() {

        Button submit = findViewById(R.id.submitAttendance);
        ProgressDialog saveloader = new ProgressDialog(this);


        final Calendar myCalendar = Calendar.getInstance();

        EditText edittext = (EditText) findViewById(R.id.attendanceDate);
        DatePickerDialog.OnDateSetListener dateCal = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                edittext.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));

                date = DateFormat.getDateInstance().format(myCalendar.getTime()).split("\\s|,");

            }

        };
        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Attendance.this, dateCal, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Attendance.this, "Submitting", Toast.LENGTH_SHORT).show();

                //Toast.makeText(Attendance.this, passModel.getList().toString(), Toast.LENGTH_SHORT).show();

                Map<String, Object> mapValues = passModel.getList();
                ;
                if (!mapValues.isEmpty()) {

                    mapValues.put("Count", mapValues.size());



                    if(date == null){
                        date = DateFormat.getDateInstance().format(new Date()).split("\\s|,");
                    }
                    mapValues.put("Date", date[0]+" "+date[1]); //adding current date to database


                    //creating node according to year and month

                    DatabaseReference newReferenceAttendance = referenceAttendance.child(model.getName()).child(date[3]).child(date[0]);
                    String id = newReferenceAttendance.push().getKey();


                    newReferenceAttendance.child(id).updateChildren(mapValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Attendance.this, "Attendance marked Successfully!", Toast.LENGTH_SHORT).show();
                                saveloader.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(Attendance.this, "Failed : " + error, Toast.LENGTH_SHORT).show();
                                saveloader.dismiss();
                            }
                            startActivity(new Intent(Attendance.this, classView.class));
                        }
                    });

                } else {
                    Toast.makeText(Attendance.this, "No attendance marked", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Attendance.this, date[3], Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private void analyseAttendance() {

        String className = passModel.getModel().getName();
        if(date == null){
            date = DateFormat.getDateInstance().format(new Date()).split("\\s|,");
        }
        DatabaseReference ref = referenceAttendance.child(className).child(date[3]);

        //Queue <String> months = new LinkedList <String>(Arrays.asList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"));  //"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"
        ArrayList <String> months = new ArrayList<>(Arrays.asList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"));


        for(int i=0; i<12; i++){
            //ref = ref.;
            /*FirebaseRecyclerOptions<attendanceModel> options = new FirebaseRecyclerOptions.Builder<attendanceModel>()
                    .setQuery(ref, attendanceModel.class)
                    .build(); */
            // Connecting object of required Adapter class to
            // the Adapter class itself

            ref.child(months.get(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        if(String.valueOf(task.getResult().getValue()) != null){
                            //Toast.makeText(Attendance.this, String.valueOf(task.getResult().getValue()), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });


            //attendanceAdapter adapter = new attendanceAdapter(this);
            // Connecting Adapter class with the Recycler view
            //recyclerView.setAdapter(adapter);
     }

        //Toast.makeText(Attendance.this, months.toString(), Toast.LENGTH_SHORT).show();
    }

}