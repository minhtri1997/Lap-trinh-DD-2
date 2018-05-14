package com.example.cooking.fragments.spl_result;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cooking.R;
import com.example.cooking.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    String dateStr;
    List<Recipe> recipes;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ListView listViewRecipeName;

    List<String> recipeNames;

    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        listViewRecipeName = view.findViewById(R.id.list_view_recipe_name);

        Intent intent = getActivity().getIntent();
        dateStr = intent.getStringExtra("date");

        recipes = new ArrayList<>();
        recipeNames = new ArrayList<>();

        reference.child("user").child(auth.getCurrentUser().getUid()).child("shopping-list").child(dateStr).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    recipes.add(snapshot.getValue(Recipe.class));
                }

                for (int i = 0; i < recipes.size(); i++) {
                    recipeNames.add(recipes.get(i).getName());
                }

                ArrayAdapter<String> recipeNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, recipeNames);

                listViewRecipeName.setAdapter(recipeNameAdapter);

                reference.child("user").child(auth.getCurrentUser().getUid()).child("shopping-list").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
