package com.example.cooking.models;

import java.io.Serializable;

public class Material implements Serializable {
    private String name;
    private String number;
    private String unit;

    public Material() {
    }

    public Material(String name, String number, String unit) {
        this.name = name;
        this.number = number;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
