package com.ozpamuk.kayitolsayfasiyapimi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity3 extends AppCompatActivity {

    private ImageView imghome, imginfo, imgchat, imgalert, imgbuymedicine, imglogout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private FirebaseFirestore db;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);

        imghome = findViewById(R.id.imagehome);
        imginfo = findViewById(R.id.imageinfomedicine);
        imgchat = findViewById(R.id.imagechatdoctor);
        imgalert = findViewById(R.id.imagealert);
        imgbuymedicine = findViewById(R.id.imagebuymedicine);
        imglogout = findViewById(R.id.imagelogout);

        preferences = getSharedPreferences("com.ozpamuk.kayitolsayfasiyapimi", Context.MODE_PRIVATE);
        editor = preferences.edit();
        db = FirebaseFirestore.getInstance();
        requestQueue = Volley.newRequestQueue(this);

        imginfo.setOnClickListener(v -> {
            Intent intent3 = new Intent(HomeActivity3.this, HomeActivity2.class);
            startActivity(intent3);
        });

        imgchat.setOnClickListener(v -> {
           Intent intent= new Intent(HomeActivity3.this,MainActivity4.class);
           startActivity(intent);
        });

        imglogout.setOnClickListener(v -> {
            editor.putBoolean("beniHatirla", false);
            editor.apply();
            Intent intent = new Intent(HomeActivity3.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        imgalert.setOnClickListener(v -> sendAlertNotification());
    }

    private void sendAlertNotification() {
        String userId = preferences.getString("userId", "");  // Kullanıcının ID'sini al
        if (userId.isEmpty()) {
            Toast.makeText(this, "Kullanıcı kimliği bulunamadı!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firestore'dan aile üyelerinin FCM tokenlarını al
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String familyId = document.getString("familyId"); // Kullanıcının aile ID'sini al
                        if (familyId != null) {
                            fetchFamilyTokens(familyId);
                        } else {
                            Toast.makeText(this, "Aile bilgisi bulunamadı!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Kullanıcı bilgisi alınamadı", e));
    }

    private void fetchFamilyTokens(String familyId) {
        db.collection("users")
                .whereEqualTo("familyId", familyId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String fcmToken = document.getString("fcmToken");
                        if (fcmToken != null && !fcmToken.isEmpty()) {
                            sendFCMNotification(fcmToken);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Aile üyeleri alınamadı", e));
    }

    private void sendFCMNotification(String fcmToken) {
        String url = "https://fcm.googleapis.com/fcm/send";
        String serverKey = "YOUR_SERVER_KEY"; // Buraya Firebase sunucu anahtarını ekleyin

        try {
            JSONObject json = new JSONObject();
            json.put("to", fcmToken);

            JSONObject notification = new JSONObject();
            notification.put("title", "Acil Durum!");
            notification.put("body", "Aile üyeniz yardım düğmesine bastı!");

            json.put("notification", notification);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                    response -> Log.d("FCM", "Bildirim başarıyla gönderildi"),
                    error -> Log.e("FCM", "Bildirim gönderme başarısız", error)) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + serverKey);  // Sunucu anahtarını burada gönderiyorsunuz
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}