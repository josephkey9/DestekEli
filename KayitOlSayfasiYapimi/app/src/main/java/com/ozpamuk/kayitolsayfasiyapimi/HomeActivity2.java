package com.ozpamuk.kayitolsayfasiyapimi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity2 extends AppCompatActivity {
    private GridView mGridView;
    private String[] ilaclar={"Metformin", "Amlodipin", "Atorvastatin", "Omeprazol", "Levotiroksin", "Furosemid",
            "Warfarin", "Donepezil", "Paracetamol", "Alendronat","Çıkış"};

    private int[] resimler = {R.drawable.metformin,R.drawable.amlodipin,R.drawable.atorvastatin,R.drawable.omeprazol,
            R.drawable.levotiroksin,R.drawable.furosemid,R.drawable.warfarin,R.drawable.donepezil,
            R.drawable.paracetamol,R.drawable.alendronat,R.drawable.logouticon};

    private String[] kullanımAmacları={"Tip 2 diyabet tedavisinde kan şekerini kontrol etmek.",
            "Yüksek tansiyon ve angina (göğüs ağrısı) tedavisi.",
            "Kolesterolü düşürmek ve kalp hastalığı riskini azaltmak.",
            "Mide asidiyle ilgili sorunlarda, örneğin reflü ve ülser tedavisinde kullanılır.",
            "Tiroid hormon eksikliğinde (hipotiroidi).",
            "Vücutta biriken fazla sıvının atılması için (diüretik).",
            "Kan pıhtılaşmasını önlemek.",
            "Alzheimer hastalığının semptomlarını hafifletmek.",
            "Ağrı kesici ve ateş düşürücü.",
            "Osteoporoz (kemik erimesi) tedavisinde kullanılır."};

    private String[] talimatlar={"Günde 2-3 kez, yemeklerle birlikte alınır.",
            "Günde bir kez, doktorun önerdiği dozda alınır.",
            "Günde bir kez, genellikle akşamları alınır.",
            "Genellikle günde bir kez, aç karnına alınır.",
            "Sabahları aç karnına, genellikle günde bir kez alınır.",
            "Doktorun önerdiği dozda, genellikle sabahları alınır.",
            "Günde bir kez, aynı saatte alınır.",
            "Genellikle günde bir kez, yatmadan önce alınır.",
            "Gerekli olduğunda, 4-6 saatte bir alınır; maksimum doz aşılmamalıdır.",
            "Genellikle haftada bir kez, sabah aç karnına alınır."};

    private ilacAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        mGridView=(GridView)findViewById(R.id.activiy_home2_gridView);
        adapter=new ilacAdapter(ilaclar,resimler,kullanımAmacları,talimatlar,this);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0 && i < ilaclar.length) {
                    // "Geç" öğesine tıklandığında HomeActivity3'e geçiş yapılacak
                    if (ilaclar[i].equals("Çıkış")) {
                        Intent intent = new Intent(HomeActivity2.this, HomeActivity3.class);
                        startActivity(intent);
                    } else {
                        // Diğer ilaçlar için Toast mesajları gösteriliyor
                        Toast.makeText(getApplicationContext(), kullanımAmacları[i], Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), talimatlar[i], Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}