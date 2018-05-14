package com.example.cooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cooking.R;
import com.example.cooking.models.Recipe;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Recipe> recipes;

    private FirebaseStorage storage;
    StorageReference storageRef;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ImageView imageViewRecipe;

    Recipe recipe;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.row_recipe, null);

        imageViewRecipe = convertView.findViewById(R.id.image_view_recipe);
        TextView textViewRecipeName = convertView.findViewById(R.id.text_view_recipe_name);
        TextView textViewAuthor = convertView.findViewById(R.id.text_view_author);
        TextView textViewNote = convertView.findViewById(R.id.text_view_note);

        recipe = recipes.get(position);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        setImageForImageView();

        textViewRecipeName.setText(recipe.getName());
        textViewAuthor.setText("Tác giả: " + recipe.getAuthor());
        textViewNote.setText(String.valueOf("Lượt ghi nhớ: " + recipe.getNote()));

        return convertView;
    }

    private void setImageForImageView() {
        Picasso.get().load(recipe.getImage()).into(imageViewRecipe);
    }
}
