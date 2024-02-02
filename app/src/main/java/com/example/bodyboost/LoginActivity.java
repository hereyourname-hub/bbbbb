package com.example.bodyboost;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.tv_forgot_pw);

        // Initialization FireBase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // При нажатии на кнопку вызываем метод для запроса разрешения
                requestNotificationPermission();
            }
        });

        TextView tvSwitchToRegister = findViewById(R.id.tv_havent_account);
        tvSwitchToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToRegister();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    private void requestNotificationPermission() {
        // Проверяем, есть ли у нас уже разрешение на отправку уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            boolean areNotificationsEnabled = notificationManager.areNotificationsEnabled();
            if (!areNotificationsEnabled) {
                // Если разрешение еще не предоставлено, запрашиваем его
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
                return; // Прерываем метод, чтобы аутентификация не выполнилась
            }
        }

        // Если разрешение уже предоставлено или устройство не поддерживает запрос разрешения,
        // выполняем аутентификацию пользователя
        authenticateUser();
    }

    private void authenticateUser() {
        EditText etLoginEmail = findViewById(R.id.et_email);
        EditText etLoginPassword = findViewById(R.id.et_password);

        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}