package com.ozpamuk.kayitolsayfasiyapimi.model;

public class MesajIstek {
    private String kanalId;
    private String kullaniciId;

    public MesajIstek(String kanalId, String kullaniciId) {
        this.kanalId = kanalId;
        this.kullaniciId = kullaniciId;
    }

    public MesajIstek() {
    }

    public String getKanalId() {
        return kanalId;
    }

    public void setKanalId(String kanalId) {
        this.kanalId = kanalId;
    }

    public String getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciId = kullaniciId;
    }
}
