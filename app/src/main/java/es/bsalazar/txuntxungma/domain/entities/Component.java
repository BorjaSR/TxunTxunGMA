package es.bsalazar.txuntxungma.domain.entities;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

public class Component {

    private String id;
    private String name;

    public Component(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Component(String name) {
        this.name = name;
    }

    public Component() {
    }

    public Component(String id, DocumentSnapshot document) {
        Component component = document.toObject(this.getClass());
        this.id = id;
        this.name = component.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Object> getMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        return map;
    }
}
