package com.ozpamuk.kayitolsayfasiyapimi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ozpamuk.kayitolsayfasiyapimi.model.Kullanici;

public class MainActivity extends AppCompatActivity {
    private EditText editTextAd,editTextSoyad,editTextSehir,editTextYas,editTextKullanici,editTextSifre;
    private CheckBox checkBox1,checkBox2,checkBox3;
    private RadioButton radioBtnE,radioBtnK;
    private String editAd,editSoyad,editSehir,editYas,editKullanıcıAdi,editSifre;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    private Kullanici mKullanici;
    private FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, HomeActivity.class);
        //startActivity(intent);

        //Intent intent = new Intent(this, getToken.class);
        //startActivity(intent);



        editTextAd=(EditText) findViewById(R.id.editTxtAd);
        editTextSoyad=(EditText) findViewById(R.id.editTxtSoyad);
        editTextSehir=(EditText) findViewById(R.id.editTxtSehir);
        editTextYas=(EditText) findViewById(R.id.editTxtYas);
        editTextKullanici=(EditText) findViewById(R.id.editTxtKullaniciAdi);
        editTextSifre=(EditText) findViewById(R.id.editTxtNumberPassword);

        /*checkBox1=(CheckBox) findViewById(R.id.checkBoxVarolanlar1);
        checkBox2=(CheckBox) findViewById(R.id.checkBoxVarolanlar2);
        checkBox3 =(CheckBox) findViewById(R.id.checkBoxVarolanlar3);

        radioBtnE=(RadioButton) findViewById(R.id.radioBtnE);
        radioBtnK=(RadioButton) findViewById(R.id.radioBtnK);*/

        mAuth= FirebaseAuth.getInstance();
        mFireStore= FirebaseFirestore.getInstance();

    }
    public void BtnKayitOl(View v){
        editAd=editTextAd.getText().toString();
        editSoyad=editTextSoyad.getText().toString();
        editSehir=editTextSehir.getText().toString();
        editYas=editTextYas.getText().toString();
        editKullanıcıAdi=editTextKullanici.getText().toString();
        editSifre=editTextSifre.getText().toString();

        if (!TextUtils.isEmpty(editAd)&&!TextUtils.isEmpty(editSoyad)&&!TextUtils.isEmpty(editSehir)&&!TextUtils.isEmpty(editYas)){
            System.out.println("--------BİLGİLERİNİZ---------");
            System.out.println("Adınız: "+editAd);
            System.out.println("Soyadınız: "+editSoyad);
            System.out.println("Şehriniz: "+editSehir);
            System.out.println("Yaşınız: "+editYas);
            System.out.println("Kullanıcı Adınız: "+editKullanıcıAdi);
            System.out.println("Şiferniz: "+editSifre);

            //System.out.println("-----------VAROLAN KRONİK HASTALIKLARINIZ-------------");

           /* if (checkBox1.isChecked())
                System.out.println(checkBox1.getText().toString());
            if (checkBox2.isChecked())
                System.out.println(checkBox2.getText().toString());
            if (checkBox3.isChecked())
                System.out.println(checkBox3.getText().toString());

            if (radioBtnE.isChecked())
                System.out.println("Cinsiyetiniz: "+radioBtnE.getText().toString());
            else
                System.out.println("Cinsiyetiniz: "+radioBtnK.getText().toString());*/
            mAuth.createUserWithEmailAndPassword(editKullanıcıAdi,editSifre)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mUser = mAuth.getCurrentUser();
                                if (mUser != null) {
                                    mKullanici = new Kullanici(editAd, editSoyad, editYas, editSehir, editKullanıcıAdi, mUser.getUid(),"default");

                                    mFireStore.collection("Kullanıcılar").document(mUser.getUid())
                                            .set(mKullanici)
                                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(MainActivity.this, "Kaydınız başarıyla gerçekleştirildi", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                        intent.putExtra("KullaniciAdi", editKullanıcıAdi);
                                                        intent.putExtra("Şifre", editSifre);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Firestore kaydı başarısız: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(MainActivity.this, "Firebase Kullanıcı nesnesi null döndü!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Kayıt başarısız: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });




        }
        else System.out.println("Bilgiler Boş Bırakılamaz ! ");
    }
}