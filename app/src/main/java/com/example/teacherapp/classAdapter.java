package com.example.teacherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


class classAdapter extends FirebaseRecyclerAdapter<classModel, classAdapter.classViewholder> {

    public classAdapter(@NonNull FirebaseRecyclerOptions<classModel> options) {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull classViewholder holder, int position, @NonNull classModel model) {

        holder.name.setText(model.getName());
        //holder.name.setText("testingBn");

        holder.description.setText(model.getDescription());
        //holder.description.setText("Hello World!");

        //holder.batch.setText(model.getBatch());
        //holder.time.setText(model.getTime());
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public classViewholder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new classAdapter.classViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class classViewholder extends RecyclerView.ViewHolder {
        TextView name, description, batch,time;
        public classViewholder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.desc);
            //batch = itemView.findViewById(R.id.batchRow);
            //time = itemView.findViewById(R.id.timeRow);
        }
    }
}