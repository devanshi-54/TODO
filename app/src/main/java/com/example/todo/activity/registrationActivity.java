package com.example.todo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.R;
import com.example.todo.databinding.ActivityRegistrationBinding;
import com.example.todo.model.DatabaseHelper;

public class registrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.tvlogin.setOnClickListener(v -> {
            Intent intent =new Intent(registrationActivity.this, loginActivity.class);
            startActivity(intent);
            finish();
        });
        binding.submit.setOnClickListener(v -> {
            String name = binding.etname.getText().toString().trim();
            String email = binding.etemail.getText().toString().trim();
            String cno = binding.etcno.getText().toString().trim();
            String password = binding.etpassword.getText().toString().trim();
            String cpassword = binding.etcpassword.getText().toString().trim();


            if(name.isEmpty() || email.isEmpty() || cno.isEmpty() || password.isEmpty() || cpassword.isEmpty()){
                Toast.makeText(this,"please fill all the fields",Toast.LENGTH_SHORT).show();
                return;
            }

            if(cno.length() < 9){
                Toast.makeText(this,"enter correct contectnumber ",Toast.LENGTH_SHORT).show();
                return;
            }


            if (!password.equals(cpassword)){
                Toast.makeText(this,"password and confirm password does not match",Toast.LENGTH_SHORT).show();
                return;
            }


            DatabaseHelper db = new DatabaseHelper(this);
            boolean success = db.registerUser( name , email , cno , password );

            if(success){
                Toast.makeText(this,"Registration successfully",Toast.LENGTH_SHORT).show();


                SharedPreferences sharedPreferences = getSharedPreferences("user_R_session" , MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name );
                editor.putString("cno", cno );
                editor.putString("email", email );
                editor.putString("password", password );
                editor.apply();


                Intent intent = new Intent(registrationActivity.this, loginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Email already exists",Toast.LENGTH_SHORT).show();
                return;
            }

            binding.etname.setText("");
            binding.etemail.setText("");
            binding.etcno.setText("");
            binding.etpassword.setText("");
            binding.etcpassword.setText("");

        });
    }
}