package com.example.todo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.databinding.ActivitySplashscreenBinding;
import com.example.todo.model.DatabaseHelper;

public class SplashscreenActivity extends AppCompatActivity {
    ActivitySplashscreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivitySplashscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        DatabaseHelper db = new DatabaseHelper(this);

        new Handler().postDelayed(() ->{
            SharedPreferences sharedPreference = getSharedPreferences("user_R_session" , MODE_PRIVATE);
            String name = sharedPreference.getString("name" , null);
            String cno = sharedPreference.getString("cno" , null);
            String emails = sharedPreference.getString("email" , null);
            String passwords = sharedPreference.getString("password" , null);
            if(name != null && cno != null && emails != null && passwords !=null){
                SharedPreferences sharedPreferences = getSharedPreferences("user_L_session" , MODE_PRIVATE);
                String username = sharedPreferences.getString("email" , null);
                String password = sharedPreferences.getString("password" , null);
                if(username != null && password !=null){
                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }else{
                    Intent intent2 = new Intent(this, loginActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }else{
                Intent intent = new Intent(this, registrationActivity.class);
                startActivity(intent);
                finish();
            }

        },2000);
    }
}