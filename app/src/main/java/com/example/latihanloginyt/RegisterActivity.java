package com.example.latihanloginyt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.latihanloginyt.api.ApiClient;
import com.example.latihanloginyt.api.ApiInterface;
import com.example.latihanloginyt.databinding.ActivityRegisterBinding;
import com.example.latihanloginyt.model.login.Login;
import com.example.latihanloginyt.model.register.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    String usernameRegister, passwordRegister, nameRegister;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> {
            usernameRegister = binding.etUsernameRegister.getText().toString().trim();
            passwordRegister = binding.etPasswordRegister.getText().toString().trim();
            nameRegister = binding.etNameRegister.getText().toString().trim();
            if(usernameRegister.isEmpty() || nameRegister.isEmpty() || passwordRegister.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Please fill in all fields completely", Toast.LENGTH_SHORT).show();
            } else {
                register(usernameRegister, passwordRegister, nameRegister);
            }
        });

        binding.tvAlreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void register(String username, String password, String name) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Register> registerCall = apiInterface.registerResponse(username,password, name);
        registerCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();*/
                } else {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}