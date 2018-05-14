package com.example.cooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.cooking.R;
import com.example.cooking.models.Recipe;
import com.example.cooking.models.RecipeChecker;

import java.util.List;

public class ChosenRecipeAdapter extends BaseAdapter {
    private Context context;
    private List<RecipeChecker> recipeCheckers;
    private LayoutInflater inflater;

    public ChosenRecipeAdapter(Context context, List<RecipeChecker> recipeCheckers) {
        this.context = context;
        this.recipeCheckers = recipeCheckers;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recipeCheckers.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeCheckers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateList(List<RecipeChecker> recipeCheckers) {
        this.recipeCheckers = recipeCheckers;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.row_spl_recipe, null);

        TextView textViewRecipeName = convertView.findViewById(R.id.text_view_recipe_name);
        CheckBox checkBoxChooseRecipe = convertView.findViewById(R.id.checkbox_choose_recipe);

        RecipeChecker recipeChecker = recipeCheckers.get(position);

        textViewRecipeName.setText(recipeChecker.toString());
        if (recipeChecker.isCheck()) {
            checkBoxChooseRecipe.setChecked(true);
        } else {
            checkBoxChooseRecipe.setChecked(false);
        }

        return convertView;
    }
}
