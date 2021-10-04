package com.example.teacherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


class studentAdapter extends FirebaseRecyclerAdapter<studentModel, studentAdapter.studentViewholder> {

    private Context context;
    //ArrayList<String> arr = new ArrayList<>();
    private TreeMap<String,Object> list = new TreeMap<>();



    public studentAdapter(@NonNull FirebaseRecyclerOptions<studentModel> options, ArrayList<String> al) {

        super(options);
        //arr = al;
    }

    public studentAdapter(FirebaseRecyclerOptions<studentModel> options, Context c) {
        super(options);
        this.context = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull studentViewholder holder, int position, @NonNull studentModel model) {

        holder.name.setText(model.getName());
        holder.sid.setText(model.getSid());


        //holder.check.setText(arr.get(position));

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if ( b ) {
                    // perform logic
                    //notify_tv.setVisibility(View.VISIBLE);
                    list.put("S"+(holder.getAdapterPosition()+1),model.getName());
                }
                else {
                    list.remove("S"+(holder.getAdapterPosition()+1));
                }
            }
        });
        passModel.setList(list);


    }

    @NonNull
    @Override
    public studentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_student, parent, false);
        return new studentAdapter.studentViewholder(view);
    }


    static class studentViewholder extends RecyclerView.ViewHolder {
        public TextView name, sid;
        View viewClass;
        CheckBox check;

        public studentViewholder(@NonNull View itemView) {
            super(itemView);
            viewClass = itemView;

            name = itemView.findViewById(R.id.name);
            sid = itemView.findViewById(R.id.sid);
            check = itemView.findViewById(R.id.attendance);
        }
    }


}