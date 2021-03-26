package com.andflow.cekongkir;

public class notifModels {
    String judul, isi;
    int notif_no_id;

    public notifModels(){

    }

    public notifModels(String judul, String isi){
        this.judul = judul;
        this.isi = isi;
    }

    public int getNotif_no_id(){
        return notif_no_id;
    }

    public void setNotif_no_id(int no_id){
        this.notif_no_id = notif_no_id;
    }

    public String getJudul(){ return judul; }

    public void setJudul(String judul){
        this.judul = judul;
    }

    public String getIsi(){ return isi; }

    public void setIsi(String isi){
        this.isi = isi;
    }

}

