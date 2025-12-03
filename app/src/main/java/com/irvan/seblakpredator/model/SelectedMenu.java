package com.irvan.seblakpredator.model;

import com.irvan.seblakpredator.SecondTransaction;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectedMenu implements Serializable {
    private String nama;
    private String level;
    private String kuah;
    private String telur;
    private String kencur;
    private int hargaLevel;
    private int hargaKuah;
    private int hargaTelur;
    private int hargaKencur;
    private ArrayList<SecondTransaction.SelectedTopping> selectedToppings;

    public SelectedMenu(String nama, String level, String kuah, String telur, String kencur,
                        int hargaLevel, int hargaKuah, int hargaTelur, int hargaKencur,
                        ArrayList<SecondTransaction.SelectedTopping> selectedToppings) {
        this.nama = nama;
        this.level = level;
        this.kuah = kuah;
        this.telur = telur;
        this.kencur = kencur;
        this.hargaLevel = hargaLevel;
        this.hargaKuah = hargaKuah;
        this.hargaTelur = hargaTelur;
        this.hargaKencur = hargaKencur;
        this.selectedToppings = selectedToppings != null ? selectedToppings : new ArrayList<>();
    }
    public void setNama(String nama) { this.nama = nama; }
    public void setLevel(String level) { this.level = level; }
    public void setKuah(String kuah) { this.kuah = kuah; }
    public void setTelur(String telur) { this.telur = telur; }
    public void setKencur(String kencur) { this.kencur = kencur; }
    public void setHargaLevel(int hargaLevel) { this.hargaLevel = hargaLevel; }
    public void setHargaKuah(int hargaKuah) { this.hargaKuah = hargaKuah; }
    public void setHargaTelur(int hargaTelur) { this.hargaTelur = hargaTelur; }
    public void setHargaKencur(int hargaKencur) { this.hargaKencur = hargaKencur; }
    public void setSelectedToppings(ArrayList<SecondTransaction.SelectedTopping> selectedToppings) {
        this.selectedToppings = selectedToppings != null ? selectedToppings : new ArrayList<>();
    }

    public String getNama() { return nama; }
    public String getLevel() { return level; }
    public String getKuah() { return kuah; }
    public String getTelur() { return telur; }
    public String getKencur() { return kencur; }
    public int getHargaLevel() { return hargaLevel; }
    public int getHargaKuah() { return hargaKuah; }
    public int getHargaTelur() { return hargaTelur; }
    public int getHargaKencur() { return hargaKencur; }
    public ArrayList<SecondTransaction.SelectedTopping> getSelectedToppings() { return selectedToppings; }
}
