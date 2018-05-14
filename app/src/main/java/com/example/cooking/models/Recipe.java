package com.example.cooking.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private String name;
    private String image;
    private String type;
    private int ration;
    private List<Material> materials;
    private List<String> guideSteps;
    private int note;
    private String author;

    public Recipe() {
    }

    public Recipe(String name, String image, String type, int ration, List<Material> materials, List<String> guideSteps, int note, String author) {
        this.name = name;
        this.image = image;
        this.type = type;
        this.ration = ration;
        this.materials = materials;
        this.guideSteps = guideSteps;
        this.note = note;
        this.author = author;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRation() {
        return ration;
    }

    public void setRation(int ration) {
        this.ration = ration;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public List<String> getGuideSteps() {
        return guideSteps;
    }

    public void setGuideSteps(List<String> guideSteps) {
        this.guideSteps = guideSteps;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
