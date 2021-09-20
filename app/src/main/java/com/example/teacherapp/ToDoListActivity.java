package com.example.teacherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    ImageView imageView;

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

        //Back Button Function
        imageView = findViewById(R.id.todoback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ToDoListModel> options = new FirebaseRecyclerOptions.Builder<ToDoListModel>().setQuery(reference, ToDoListModel.class).build();

        FirebaseRecyclerAdapter<ToDoListModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<ToDoListModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ToDoListModel model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDescription(model.getDescription());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todoretrieved_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View nView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nView = itemView;
        }

        public void setTask(String task) {
            TextView taskTextView = nView.findViewById(R.id.todoitemtopic);
            taskTextView.setText(task);
        }

        public void setDescription(String description) {
            TextView descTextView = nView.findViewById(R.id.todoitemdescription);
            descTextView.setText(description);
        }

        public void setDate(String date) {
            TextView dateTextView = nView.findViewById(R.id.tododate);
            dateTextView.setText(date);
        }
    }
}