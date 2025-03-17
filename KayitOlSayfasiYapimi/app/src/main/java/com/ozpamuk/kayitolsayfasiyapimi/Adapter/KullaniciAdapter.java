package com.ozpamuk.kayitolsayfasiyapimi.Adapter;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ozpamuk.kayitolsayfasiyapimi.R;
import com.ozpamuk.kayitolsayfasiyapimi.model.Kullanici;
import com.ozpamuk.kayitolsayfasiyapimi.model.MesajIstek;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.kullaniciHolder> {

    private ArrayList<Kullanici> mKullaniciList;
    private Context mContext;
    private View v;
    private Kullanici mKullanici;
    private ImageView imgKapat;
    private CircleImageView imgProfil;
    private LinearLayout linearGonder;
    private EditText editMesaj;
    private String txtMesaj;
    private Dialog mesajDialog;
    private Window mesajWindow;

    private FirebaseFirestore mFireStore;
    private DocumentReference mReferences;
    private String mUID, kanalId,mesajdocumentId;
    private MesajIstek mesajIstek;
    private HashMap<String, Object> mData;

    public KullaniciAdapter(ArrayList<Kullanici> mKullaniciList, Context mContext,String mUID) {
        this.mKullaniciList = mKullaniciList;
        this.mContext = mContext;
        this.mUID=mUID;
        mFireStore=FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public kullaniciHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(mContext).inflate(R.layout.kullanici_item, parent, false);
        return new kullaniciHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull kullaniciHolder holder, int position) {
        mKullanici = mKullaniciList.get(position);
        holder.kullaniciIsmi.setText(mKullanici.getKullaniciIsmi());

        if (mKullanici.getKullaniciProfil().equals("default"))
            holder.kullaniciProfil.setImageResource(R.mipmap.ic_launcher);
        else
            Picasso.get().load(mKullanici.getKullaniciProfil()).resize(66, 66).into(holder.kullaniciProfil);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int kPos = holder.getAdapterPosition();
                if (kPos != RecyclerView.NO_POSITION){
                    mReferences=mFireStore.collection("Kullanıcılar").document(mUID).collection("Kanal").document(mKullaniciList.get(kPos).getKullaniciId());
                    mReferences.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        //mesajlasma
                                    }else
                                        mesajGonderDialog(mKullaniciList.get(kPos));
                                }
                            });
                }

            }
        });
    }

    private void mesajGonderDialog( final Kullanici kullanici) {
        mesajDialog = new Dialog(mContext);
        mesajDialog.setContentView(R.layout.custom_dialog_mesaj_gonder);

        mesajWindow = mesajDialog.getWindow();
        if (mesajWindow != null) {
            mesajWindow.setBackgroundDrawableResource(android.R.color.transparent);
            mesajWindow.setGravity(Gravity.CENTER);
            mesajWindow.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        }

        imgKapat = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_mesajiptal);
        imgProfil = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gönder_profil_imgKullanıcı);
        linearGonder = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_imgGonder);
        editMesaj = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_editMesaj);

        if (kullanici.getKullaniciProfil().equals("default"))
            imgProfil.setImageResource(R.mipmap.ic_launcher);
        else
            Picasso.get().load(kullanici.getKullaniciProfil()).resize(126, 126).into(imgProfil);

        imgKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mesajDialog.dismiss();
            }
        });

        linearGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMesaj = editMesaj.getText().toString();
                if (!TextUtils.isEmpty(txtMesaj)) {
                    kanalId= UUID.randomUUID().toString();

                    mesajIstek = new MesajIstek(kanalId,mUID);
                    mFireStore.collection("Mesajİstekleri").document(kullanici.getKullaniciId()).collection("İstekler").document(mUID)
                            .set(mesajIstek)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   //chat kısmı
                                   mesajdocumentId=UUID.randomUUID().toString();
                                   mData=new HashMap<>();
                                   mData.put("mesajicerigi",txtMesaj);
                                   mData.put("gönderen",mUID);
                                   mData.put("alici",kullanici.getKullaniciId());
                                   mData.put("mesajTipi","text");
                                   mData.put("MesajTarihi", FieldValue.serverTimestamp());
                                   mData.put("docId",mesajdocumentId);

                                   mFireStore.collection("ChatKanalları").document(kanalId).collection("Mesajlar").document(mesajdocumentId)
                                           .set(mData)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful()){
                                                  Toast.makeText(mContext,"Mesaj İsteğiniz Başarıyla İletldi.",Toast.LENGTH_SHORT).show();
                                                  if (mesajDialog.isShowing())
                                                      mesajDialog.dismiss();
                                              }else
                                                  Toast.makeText(mContext,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                               }
                                           });
                               }else
                                   Toast.makeText(mContext,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(mContext, "Mesaj Boş Bırakılamaz!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mesajDialog.show();
    }

    @Override
    public int getItemCount() {
        return mKullaniciList.size();
    }

    static class kullaniciHolder extends RecyclerView.ViewHolder {
        TextView kullaniciIsmi;
        CircleImageView kullaniciProfil;

        public kullaniciHolder(@NonNull View itemView) {
            super(itemView);
            kullaniciIsmi = itemView.findViewById(R.id.kullanici_item_txtKullaniciIsim);
            kullaniciProfil = itemView.findViewById(R.id.kullanici_item_imgprofile);
        }
    }
}
