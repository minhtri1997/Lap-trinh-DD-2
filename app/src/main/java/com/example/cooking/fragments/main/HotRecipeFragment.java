package com.example.cooking.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cooking.R;
import com.example.cooking.RecipeActivity;
import com.example.cooking.adapters.RecipeAdapter;
import com.example.cooking.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HotRecipeFragment extends Fragment {

    ListView listViewHotRecipe;
    List<Recipe> recipe;
    RecipeAdapter recipeAdapter;

    List<String> keys;

    public HotRecipeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_recipe, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();

        listViewHotRecipe = view.findViewById(R.id.list_view_hot_recipe);

        keys = new ArrayList<>();

        recipe = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getContext(), recipe);

        listViewHotRecipe.setAdapter(recipeAdapter);
        listViewHotRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("key", keys.get(position));
                intent.putExtra("recipe", recipe.get(position));

                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("cooking-recipe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keys.add(dataSnapshot.getKey());
                recipe.add(dataSnapshot.getValue(Recipe.class));
                sortHotRecipe(recipe);
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sortHotRecipe(List<Recipe> recipes) {
        for (int i = 0; i < recipes.size() - 1; i++) {
            for (int j = i + 1; j < recipes.size(); j++) {
                if (recipes.get(i).getNote() < recipes.get(j).getNote()) {
                    Collections.swap(recipe, i, j);
                    Collections.swap(keys, i, j);
                }
            }
        }
    }
}
