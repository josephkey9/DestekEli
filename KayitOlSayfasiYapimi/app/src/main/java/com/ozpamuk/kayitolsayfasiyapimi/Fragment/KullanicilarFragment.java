package com.ozpamuk.kayitolsayfasiyapimi.Fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ozpamuk.kayitolsayfasiyapimi.Adapter.KullaniciAdapter;
import com.ozpamuk.kayitolsayfasiyapimi.R;
import com.ozpamuk.kayitolsayfasiyapimi.model.Kullanici;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class KullanicilarFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private View v;
    private KullaniciAdapter mAdapter;
    private FirebaseUser mUser;
    private Query mQuery;
    private FirebaseFirestore mFirestore;
    private ArrayList<Kullanici> mKullaniciList;
    private Kullanici mKullanici;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_kullanicilar, container, false);

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        mFirestore=FirebaseFirestore.getInstance();
        mKullaniciList=new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.kullanicilar_fragment_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL,false));


        mQuery=mFirestore.collection("Kullanıcılar");
        mQuery.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(v.getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value!=null){
                    mKullaniciList.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        mKullanici= snapshot.toObject(Kullanici.class);

                        assert mKullanici != null;
                        if(!mKullanici.getKullaniciId().equals(mUser.getUid()))
                            mKullaniciList.add(mKullanici);
                    }
                    mRecyclerView.addItemDecoration(new LinearDecoration(20,mKullaniciList.size()));
                    mAdapter= new KullaniciAdapter(mKullaniciList,v.getContext(),mUser.getUid());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });

        return v;
    }

    class LinearDecoration extends RecyclerView.ItemDecoration{
    private int bosluk;
    private int veriSayisi;

    public LinearDecoration(int bosluk, int veriSayisi) {
        this.bosluk = bosluk;
        this.veriSayisi = veriSayisi;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos= parent.getChildAdapterPosition(view);
        if (pos!=(veriSayisi-1)){
            outRect.bottom=bosluk;
        }
    }
    }

}