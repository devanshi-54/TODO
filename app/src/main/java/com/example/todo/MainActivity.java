package com.example.todo;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todo.adapter.ToDoAdapter;
import com.example.todo.databinding.ActivityMainBinding;
import com.example.todo.model.DatabaseHelper;
import com.example.todo.model.ToDomodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    ActivityMainBinding binding;

    private ToDoAdapter taskAdapter;

    private List<ToDomodel> taskList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        db.openDatabase();


        taskList = new ArrayList<>();

        taskAdapter = new ToDoAdapter(db,this);
        taskAdapter.setTasks(taskList);

        taskList= db.getALLTask();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);

        binding.taskrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.taskrecyclerview.setAdapter(taskAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(binding.taskrecyclerview);



        binding.fab.setOnClickListener(v -> {
            AddnewTask.newInstance().show(getSupportFragmentManager(), AddnewTask.tag);
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList= db.getALLTask();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}