package com.ozpamuk.kayitolsayfasiyapimi.model;

public class Kullanici {
    private String kullaniciIsmi, kullaniciKullaniciAd,kullaniciId,kullaniciProfil;

    public Kullanici(String kullaniciIsmi, String kullaniciSoyad, String kullaniciYas, String kullaniciSehir, String kullaniciKullaniciAd, String kullaniciId,String kullaniciProfil) {
        this.kullaniciIsmi = kullaniciIsmi;
        this.kullaniciKullaniciAd = kullaniciKullaniciAd;
        this.kullaniciId = kullaniciId;
        this.kullaniciProfil=kullaniciProfil;
    }
    public Kullanici() {

    }

    public String getKullaniciIsmi() {
        return kullaniciIsmi;
    }

    public void setKullaniciIsmi(String kullaniciIsmi) {
        this.kullaniciIsmi = kullaniciIsmi;
    }


    public String getKullaniciKullaniciAd() {
        return kullaniciKullaniciAd;
    }

    public void setKullaniciKullaniciAd(String kullaniciKullaniciAd) {
        this.kullaniciKullaniciAd = kullaniciKullaniciAd;
    }

    public String getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciProfil() {
        return kullaniciProfil;
    }

    public void setKullaniciProfil(String kullaniciProfil) {
        this.kullaniciProfil = kullaniciProfil;
    }
}
