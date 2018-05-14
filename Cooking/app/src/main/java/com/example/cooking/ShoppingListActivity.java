package com.example.cooking;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cooking.adapters.ChosenRecipeAdapter;
import com.example.cooking.models.Recipe;
import com.example.cooking.models.RecipeChecker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listViewRecipe;
    List<RecipeChecker> recipeCheckers;
    ChosenRecipeAdapter chosenRecipeAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<String> recipeKeys = new ArrayList<>();

    String userID;

    Button buttonShoppingList;

    List<RecipeChecker> recipeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listViewRecipe = findViewById(R.id.list_view_recipe);
        recipeCheckers = new ArrayList<>();
        chosenRecipeAdapter = new ChosenRecipeAdapter(this, recipeCheckers);
        listViewRecipe.setAdapter(chosenRecipeAdapter);
        buttonShoppingList = findViewById(R.id.button_shopping_list);
        recipeChecked = new ArrayList<>();

        listViewRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeChecker recipeChecker = recipeCheckers.get(position);
                if (recipeChecker.isCheck()) {
                    recipeChecker.setCheck(false);

                    if (recipeChecked.contains(recipeChecker)) {
                        recipeChecked.remove(recipeChecker);
                    }
                } else {
                    recipeChecker.setCheck(true);
                    recipeChecked.add(recipeChecker);
                }
                recipeCheckers.set(position, recipeChecker);
                chosenRecipeAdapter.updateList(recipeCheckers);
            }
        });

        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeChecked.size() == 0) {
                    Toast.makeText(ShoppingListActivity.this, "Bạn chưa chọn công thức", Toast.LENGTH_SHORT).show();
                } else {
                    List<Recipe> recipes = new ArrayList<>();
                    for (RecipeChecker checker: recipeChecked) {
                        recipes.add(checker.getRecipe());
                    }
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-y-h-m-s");
                    String dateStr = null;
                    try {
                        dateStr = simpleDateFormat.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (Recipe recipe: recipes) {
                        reference.child("user").child(auth.getCurrentUser().getUid()).child("shopping-list").child(dateStr).push().setValue(recipe);
                    }
                    Intent intent = new Intent(ShoppingListActivity.this, SPLResultActivity.class);
                    intent.putExtra("date", dateStr);
                    startActivity(intent);
                }
            }
        });

        userID = auth.getCurrentUser().getUid();

        reference.child("user").child(userID).child("note").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reference.child("user").child(userID).child("note").removeEventListener(this);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String recipeKey = snapshot.getValue(String.class);
                    recipeKeys.add(recipeKey);
                }
                findRecipe(recipeKeys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void findRecipe(final List<String> recipeKeys) {
        reference.child("cooking-recipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe recipe = null;
                for (String key : recipeKeys) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals(key)) {
                            recipe = snapshot.getValue(Recipe.class);
                            break;
                        }
                    }
                    if (recipe != null) {
                        recipeCheckers.add(new RecipeChecker(recipe, false));
                        chosenRecipeAdapter.notifyDataSetChanged();
                    }
                }

                reference.child("cooking-recipe").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
