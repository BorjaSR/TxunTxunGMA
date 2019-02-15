package es.bsalazar.txuntxungma.domain.entities;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

public class Rate {

    private String id;
    private String description;
    private double amount;

    public Rate(String id, DocumentSnapshot document) {
        Rate rate = document.toObject(this.getClass());
        this.id = id;
        this.description = rate.getDescription();
        this.amount = rate.getAmount();
    }

    public Rate() {
    }

    public Rate(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public HashMap<String, Object> getMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("description", this.description);
        map.put("amount", this.amount);
        return map;
    }
}
