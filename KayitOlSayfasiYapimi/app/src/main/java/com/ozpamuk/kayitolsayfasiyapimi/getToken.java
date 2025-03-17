package com.ozpamuk.kayitolsayfasiyapimi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class getToken extends AppCompatActivity {
    private Button buttonToken;
    private TextView txtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_token);

        buttonToken = findViewById(R.id.buttonGetToken);
        txtToken = findViewById(R.id.textView15);

        buttonToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFCMToken();
            }
        });
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "FCM Token alınamadı!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Token başarıyla alındı
                    String token = task.getResult();
                    txtToken.setText(token);
                    System.out.println("Tokeniniz "+ token);
                });
    }
}
