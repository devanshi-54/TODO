package com.example.todo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.databinding.ActivityLoginBinding;
import com.example.todo.model.DatabaseHelper;

public class loginActivity extends AppCompatActivity {


        ActivityLoginBinding binding;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            binding=ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            binding.tvragister.setOnClickListener(v -> {
                Intent intent =new Intent(this, registrationActivity.class);
                startActivity(intent);
                finish();
            });



            binding.submit.setOnClickListener(v -> {
                String emails = binding.etemail.getText().toString().trim() ;
                String passwords = binding.etpassword.getText().toString().trim() ;


                if(emails.isEmpty() || passwords.isEmpty()){
                    Toast.makeText(this,"please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }


                DatabaseHelper db = new DatabaseHelper(this);
                boolean success = db.loginUser(emails,passwords);

                if(success){
                    Toast.makeText(this,"login successfully",Toast.LENGTH_SHORT).show();


                    SharedPreferences sharedPreferences = getSharedPreferences("user_L_session" , MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", emails );
                    editor.putString("password", passwords );
                    editor.apply();


                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(this,"invalid content",Toast.LENGTH_SHORT).show();
                    return;
                }


                binding.etemail.setText("");
                binding.etpassword.setText("");
            });

        }
}