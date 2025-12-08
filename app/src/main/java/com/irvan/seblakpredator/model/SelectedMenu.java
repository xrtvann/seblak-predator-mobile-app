package com.irvan.seblakpredator.model;

import com.irvan.seblakpredator.SecondTransaction;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectedMenu implements Serializable {
    private String name;
    private String phone;
    private String level;
    private String kuah;
    private String telur;
    private String kencur;
    private int hargaLevel;
    private int hargaKuah;
    private int hargaTelur;
    private int hargaKencur;
    private ArrayList<SecondTransaction.SelectedTopping> selectedToppings;

    public SelectedMenu(String name, String phone,String level, String kuah, String telur, String kencur,
                        int hargaLevel, int hargaKuah, int hargaTelur, int hargaKencur,
                        ArrayList<SecondTransaction.SelectedTopping> selectedToppings) {
        this.name = name;
        this.phone = phone;
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
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone){this.phone = phone;}
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

    public String getName() { return name; }
    public String getPhone(){return phone;}
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
