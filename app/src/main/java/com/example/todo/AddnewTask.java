package com.example.todo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Objects;

import com.example.todo.model.DatabaseHelper;
import com.example.todo.model.ToDomodel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddnewTask extends BottomSheetDialogFragment {
    public static final String tag = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTasksaveButton;
    private DatabaseHelper db;
    public static AddnewTask newInstance(){
        return new AddnewTask();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL , R.style.DialogStyle);
    }
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.newtasktext);
        newTasksaveButton = getView().findViewById(R.id.newtaskbutton);

        boolean isUpdate =false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            if (task != null && task.length() > 0){
                newTaskText.setText(task);
                newTasksaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.colorPrimaryDark));
            }

        }

        db= new DatabaseHelper(getActivity());
        db.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTasksaveButton.setEnabled(false);
                    newTasksaveButton.setTextColor(Color.GRAY);
                }else {

                    newTasksaveButton.setEnabled(true);
                    newTasksaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.colorPrimaryDark));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTasksaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }else{
                    ToDomodel task = new ToDomodel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
