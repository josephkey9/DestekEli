package com.ozpamuk.kayitolsayfasiyapimi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ilacAdapter extends ArrayAdapter<String> {

    private String[] ilaclar;
    private int[] resimler;
    private String[] kullanımAmaclari;
    private String[] talimatlar;
    private Context context;
    private ImageView ilacResim;
    private TextView ilacIsim;


    public ilacAdapter(String[] ilaclar,int[] resimler,String[] kullanımAmaclari,String[] talimatlar,Context context){
        super(context,R.layout.ilac_item,ilaclar);
        this.ilaclar=ilaclar;
        this.kullanımAmaclari=kullanımAmaclari;
        this.resimler=resimler;
        this.talimatlar=talimatlar;
        this.context=context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(R.layout.ilac_item,null);
        if (v!=null){
         ilacIsim=v.findViewById(R.id.ilac_item_textViewIsim);
         ilacResim=v.findViewById(R.id.ilac_item_imageViewResim);

         ilacIsim.setText(ilaclar[position]);
         ilacResim.setBackgroundResource(resimler[position]);
        }
        return v ;
    }
}
