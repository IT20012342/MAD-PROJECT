package com.example.teacherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class ToDoListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ProgressDialog saveloader;

    private DatabaseReference reference;
    private String onlineUserID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        recyclerView = findViewById(R.id.todorecycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        saveloader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            onlineUserID = mUser.getUid();
        }
        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);


        floatingActionButton = findViewById(R.id.todoaddfab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.activity_addtodo, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        dialog.show();

        EditText task = myView.findViewById(R.id.todolist_task);
        EditText description = myView.findViewById(R.id.todolist_description);
        Button save = myView.findViewById(R.id.todolist_savebtn);
        Button cancel = myView.findViewById(R.id.todolist_cancelbtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTask = task.getText().toString().trim();
                String nDescription = description.getText().toString().trim();
                String id = reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                if(TextUtils.isEmpty(nTask)) {
                    task.setError("Task is required");
                    return;
                }

                if(TextUtils.isEmpty(nDescription)) {
                    description.setError("Task is required");
                    return;
                }

                else{
                    saveloader.setMessage("Saving the task");
                    saveloader.setCanceledOnTouchOutside(false);
                    saveloader.show();

                    ToDoListModel todomodel = new ToDoListModel(nTask, nDescription, id, date);
                    reference.child(id).setValue(todomodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ToDoListActivity.this, "Task Saved Successfully!", Toast.LENGTH_SHORT).show();
                                saveloader.dismiss();
                            }
                            else {
                                String error = task.getException().toString();
                                Toast.makeText(ToDoListActivity.this, "Failed : " + error, Toast.LENGTH_SHORT).show();
                                saveloader.dismiss();
                            }
                        }
                    });

                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}