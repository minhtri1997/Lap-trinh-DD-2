package com.example.cooking.models;

public class MaterialSPL {
    private String quantity;
    private String name;

    public MaterialSPL() {
    }

    public MaterialSPL(String quantity, String name) {

        this.quantity = quantity;
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.quantity + " " + this.name;
    }
}
