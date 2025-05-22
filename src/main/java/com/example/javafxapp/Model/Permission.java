package com.example.javafxapp.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Permission {

    private int permission_id ;

    private String permission_name ;
    private boolean deleted = false ;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);



    public Permission(String permission_name, boolean deleted) {
        this.permission_name = permission_name;
        this.deleted = deleted;
    }

    public Permission(int permission_id, String permission_name, boolean deleted) {
        this.permission_id = permission_id;
        this.permission_name = permission_name;
        this.deleted = deleted;
    }

    public int getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(int permission_id) {
        this.permission_id = permission_id;
    }

    public String getPermission_name() {
        return permission_name;
    }

    public void setPermission_name(String permission_name) {
        this.permission_name = permission_name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
