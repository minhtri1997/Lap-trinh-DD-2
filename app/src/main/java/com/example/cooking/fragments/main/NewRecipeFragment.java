package com.example.cooking.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cooking.RecipeActivity;
import com.example.cooking.R;
import com.example.cooking.adapters.RecipeAdapter;
import com.example.cooking.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewRecipeFragment extends Fragment {

    public NewRecipeFragment() {}

    ListView listViewNewRecipe;
    List<Recipe> recipes;
    RecipeAdapter recipeAdapter;

    List<String> keys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_recipe, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();

        listViewNewRecipe = view.findViewById(R.id.list_view_new_recipe);

        recipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getContext(), recipes);

        listViewNewRecipe.setAdapter(recipeAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        keys = new ArrayList<>();

        myRef.child("cooking-recipe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keys.add(0, dataSnapshot.getKey());

                recipes.add(0, dataSnapshot.getValue(Recipe.class));
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

        listViewNewRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("key", keys.get(position));
                intent.putExtra("recipe", recipes.get(position));

                startActivity(intent);
            }
        });
    }
}
