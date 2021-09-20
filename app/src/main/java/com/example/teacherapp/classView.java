package com.example.teacherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class classView extends AppCompatActivity {


    //fireBase variables
    private DatabaseReference reference;
    private String onlineUserID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ProgressDialog saveloader;



    ListView listView;
    String mTitle[] = {"Physics", "Maths", "Software Development", "History", "International law"};
    String mDescription[] = {"Physics Description", "Maths Description", "IT Description", "sad Description", "Law Description"};

    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_class_view);


        if (getSupportActionBar() != null) {     // calling the action bar
            ActionBar actionBar = getSupportActionBar();

            // Customize the back button
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("blue"));
            actionBar.setBackgroundDrawable(colorDrawable);

            // showing the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }






        listView = findViewById(R.id.listView3);

        MyAdapter adapter = new MyAdapter(this, mTitle, mDescription);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Toast.makeText(classView.this, "Class Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(classView.this, "Class Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(classView.this, "Class Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(classView.this, "Class Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(classView.this, "Class Description", Toast.LENGTH_SHORT).show();
                }
            }
        });


        saveloader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            onlineUserID = mUser.getUid();
        }
        reference = FirebaseDatabase.getInstance().getReference().child("classes").child(onlineUserID);




        FloatingActionButton addNewClass = findViewById(R.id.addClass);

        addNewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(classView.this);
                LayoutInflater inflater = LayoutInflater.from(classView.this);
                View myView = inflater.inflate(R.layout.add_class, null);
                myDialog.setView(myView);

                AlertDialog dialog = myDialog.create();
                dialog.setCancelable(false);
                dialog.show();


                EditText name = myView.findViewById(R.id.className);
                EditText description = myView.findViewById(R.id.classDescription);
                EditText batch = myView.findViewById(R.id.batch);
                EditText time = myView.findViewById(R.id.timeInput);
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
                        String nClass = name.getText().toString().trim();
                        String nDescription = description.getText().toString().trim();
                        String nBatch = batch.getText().toString().trim();
                        String nTime = time.getText().toString().trim();
                        String id = reference.push().getKey();


                        if(TextUtils.isEmpty(nDescription)) {
                            nDescription  = "Auto Generated Description!";
                        }
                        if(TextUtils.isEmpty(nBatch)) {
                            nBatch = Double.toString(Math.random()*1000);
                        }
                        if(TextUtils.isEmpty(nTime)) {
                            nTime = "00:00";
                        }

                        if(TextUtils.isEmpty(nClass)) {
                            name.setError("Class name is required");
                            return;
                        }
                        else{
                            saveloader.setMessage("Saving the new class");
                            saveloader.setCanceledOnTouchOutside(false);
                            saveloader.show();

                            classModel cmodel = new classModel(nClass, nDescription, nBatch, nTime,id);
                            reference.child(id).setValue(cmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(classView.this, "Class Saved Successfully!", Toast.LENGTH_SHORT).show();
                                        saveloader.dismiss();
                                    }
                                    else {
                                        String error = task.getException().toString();
                                        Toast.makeText(classView.this, "Failed : " + error, Toast.LENGTH_SHORT).show();
                                        saveloader.dismiss();
                                    }
                                }
                            });

                        }

                        dialog.dismiss();
                    }
                });
            }
        });


    }


    //back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,MainActivity.class));
        return super.onOptionsItemSelected(item);

    }








    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];

        MyAdapter (Context c, String title[], String description[] /*, int imgs[] */) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            //this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            // now set our resources on views
            //images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);




            return row;
        }
    }
}
