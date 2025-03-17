package com.ozpamuk.kayitolsayfasiyapimi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private TextView txthata,txtdenemekullanici,txtdenemesifre;
    private EditText editText2Kullanci,editText2Sifre;
    private Button btnGirisYap,btnKayitGiris;
    private String gelenKAdi,gelenSifre,kayitliKullanici,kayitliSifre;
    private String kAdi,kSifre;
    private CheckBox checkBoxHatirla;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        txthata=(TextView)findViewById(R.id.txtview2);
        /*txtdenemekullanici=(TextView)findViewById(R.id.txtviewdeneme1);
        txtdenemesifre=(TextView)findViewById(R.id.txtviewdeneme2);
        txtdenemekullanici.setText(gelenKAdi);
        txtdenemesifre.setText(gelenSifre);*/
        editText2Kullanci=(EditText) findViewById(R.id.editText2KullaniciAdi);
        editText2Sifre=(EditText) findViewById(R.id.editTxt2NumberPassword);
        btnKayitGiris=(Button)findViewById(R.id.btnKayitGiris);
        checkBoxHatirla=(CheckBox)findViewById(R.id.checkboxhatirla);

        Intent gelenintent=getIntent();
        gelenKAdi=gelenintent.getStringExtra("KullaniciAdi");
        gelenSifre=gelenintent.getStringExtra("Şifre");
        mAuth=FirebaseAuth.getInstance();
        /*if (checkBoxHatirla.isChecked()) {
            Intent intent = new Intent(this,HomeActivity3.class);
            startActivity(intent);
            finish();
            return; }*/
       /*kayitliKullanici=preferences.getString("KullaniciAdi2","");
        kayitliSifre=preferences.getString("Şifre2","");*/
        //boolean beniHatirla = preferences.getBoolean("beniHatirla",false);

        /*System.out.println("gelen "+gelenKAdi);
        System.out.println("gelens "+gelenSifre);*/

        preferences=getSharedPreferences("com.ozpamuk.kayitolsayfasiyapimi", Context.MODE_PRIVATE);
        editor=preferences.edit();
        if (preferences.getBoolean("beniHatirla", false)) {
            Intent intent = new Intent(this, HomeActivity3.class);
            startActivity(intent);
            finish();
        }



        btnKayitGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void btnGirisYap(View v){
    kAdi=editText2Kullanci.getText().toString();
    kSifre=editText2Sifre.getText().toString();

        if (!TextUtils.isEmpty(kAdi)) {
            if (!TextUtils.isEmpty(kSifre)) {
                mAuth.signInWithEmailAndPassword(kAdi, kSifre)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                if (checkBoxHatirla.isChecked()){
                                  editor.putBoolean("beniHatirla",true);
                                  editor.apply();}

                                Intent intent = new Intent(HomeActivity.this, HomeActivity3.class);
                                startActivity(intent);
                                finish();
                            } else {
                                txthata.setText("Giriş başarısız: " + task.getException().getMessage());
                            }
                        });
            } else {
                txthata.setText("Şifre boş bırakılamaz.");
            }
        } else {
            txthata.setText("Kullanıcı adı boş bırakılamaz.");
        }
    }

}