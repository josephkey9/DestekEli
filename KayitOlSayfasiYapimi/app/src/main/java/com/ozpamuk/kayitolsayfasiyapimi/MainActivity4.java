package com.ozpamuk.kayitolsayfasiyapimi;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ozpamuk.kayitolsayfasiyapimi.Fragment.KullanicilarFragment;
import com.ozpamuk.kayitolsayfasiyapimi.Fragment.MesajlarFragment;
import com.ozpamuk.kayitolsayfasiyapimi.Fragment.ProfilFragment;
import com.ozpamuk.kayitolsayfasiyapimi.model.MesajIstek;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {
    private BottomNavigationView mBottomView;
    private KullanicilarFragment kullanicilarFragment;
    private MesajlarFragment mesajlarFragment;
    private ProfilFragment profilFragment;

    private Toolbar mToolbar;
    private RelativeLayout mRelaNotif;
    private TextView txtbilidirimSayisi;

    private FirebaseFirestore mFireStore;
    private Query mQuery;
    private FirebaseUser mUser;
    private MesajIstek mMesajIstek;
    private ArrayList<MesajIstek> mesajIstekList;

    private Dialog mesajIstekDialog;
    private ImageView mesajIstekKapat;
    private RecyclerView mesajIstekRecyclerView;


    private void init() {
        mFireStore=FirebaseFirestore.getInstance();
        mUser= FirebaseAuth.getInstance().getCurrentUser();

        mesajIstekList=new ArrayList<>();

        mBottomView = findViewById(R.id.main_activity4_bottomView);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mRelaNotif=(RelativeLayout)findViewById(R.id.bar_layout_RelativeNotif);
        txtbilidirimSayisi=(TextView)findViewById(R.id.txt_bildirimSayisi);

        kullanicilarFragment = new KullanicilarFragment();
        mesajlarFragment = new MesajlarFragment();
        profilFragment = new ProfilFragment();

        FragmentAyarla(kullanicilarFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        init();

        mQuery=mFireStore.collection("Mesajİstekleri").document(mUser.getUid()).collection("İstekler");
        mQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if(error != null){
                Toast.makeText(MainActivity4.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                return;
            }
            if (value!=null){
                txtbilidirimSayisi.setText(String.valueOf(value.getDocuments().size()));
                mesajIstekList.clear();

                for (DocumentSnapshot snapshot : value.getDocuments()){
                    mMesajIstek=snapshot.toObject(MesajIstek.class);
                    mesajIstekList.add(mMesajIstek);
                }
            }
            }
        });

        mRelaNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mesajIstekDialog();
            }
        });


        mBottomView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_ic_people:
                        mRelaNotif.setVisibility(View.INVISIBLE);
                        FragmentAyarla(kullanicilarFragment);
                        return true;
                    case R.id.bottom_nav_ic_message:
                        mRelaNotif.setVisibility(View.VISIBLE);
                        FragmentAyarla(mesajlarFragment);
                        return true;
                    case R.id.bottom_nav_ic_profile:
                        mRelaNotif.setVisibility(View.INVISIBLE);
                        FragmentAyarla(profilFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void FragmentAyarla(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity4_framelayout, fragment);
        transaction.commit();
    }
    private void mesajIstekDialog(){
        mesajIstekDialog=new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        mesajIstekDialog.setContentView(R.layout.custom_dialog_mesaj_istek);

        mesajIstekKapat=mesajIstekDialog.findViewById(R.id.custom_dialog_gelen_mesaj_istek_imgkapat);
        mesajIstekRecyclerView=mesajIstekDialog.findViewById(R.id.custom_dialog_gelen_mesaj_istek_recyclerView);

        mesajIstekKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mesajIstekDialog.dismiss();
            }
        });

        mesajIstekDialog.show();
    }
}
