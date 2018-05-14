package com.example.cooking.models;

public class RecipeChecker {
    private Recipe recipe;
    private boolean isCheck;

    public RecipeChecker() {
    }

    public RecipeChecker(Recipe recipe, boolean check) {
        this.recipe = recipe;
        this.isCheck = check;
    }

    public Recipe getRecipe() {

        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        this.isCheck = check;
    }

    @Override
    public String toString() {
        return recipe.toString();
    }
}
