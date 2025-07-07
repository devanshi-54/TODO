package com.example.todo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.todo.AddnewTask;
import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.model.DatabaseHelper;
import com.example.todo.model.ToDomodel;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDomodel> todolist = new ArrayList<>();
    private MainActivity activity;
    private DatabaseHelper db;

    public ToDoAdapter(DatabaseHelper db, MainActivity activity) {
        this.db = db;
        this.activity = activity; // Assign activity here
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        ToDomodel model = todolist.get(position);
        holder.task.setText(model.getTask());
        holder.task.setChecked(toBoolean(model.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(model.getId(), 1);
                } else {
                    db.updateStatus(model.getId(), 0);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemview);
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDomodel> todolist) {
        this.todolist = todolist;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ToDomodel toDomodel = todolist.get(position);
        db.deleteTask(toDomodel.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDomodel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddnewTask fragment = new AddnewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddnewTask.tag);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        public ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.todoCheckbox);
        }
    }
}