package com.example.cooking;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cooking.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewRecipeActivity extends AppCompatActivity {

    ListView listViewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();

        reference.child("cooking-recipe").addValueEventListener(new ValueEventListener() {
            List<Recipe> recipes = new ArrayList<>();
            List<String> keys = new ArrayList<>();
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();

                    Recipe recipe = snapshot.getValue(Recipe.class);
                    String author = recipe.getAuthor();

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String userEmail = auth.getCurrentUser().getEmail();

                    if (author.equals(userEmail)) {
                        recipes.add(recipe);
                        keys.add(key);
                    }
                }
                reference.child("cooking-recipe").removeEventListener(this);
                final ArrayAdapter<Recipe> recipeArrayAdapter =
                        new ArrayAdapter<>(ReviewRecipeActivity.this,
                                android.R.layout.simple_list_item_1,
                                recipes);

                listViewRecipe.setAdapter(recipeArrayAdapter);

                listViewRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent =
                                new Intent(ReviewRecipeActivity.this,
                                        RecipeActivity.class);
                        String key = keys.get(position);
                        Recipe recipe = recipes.get(position);
                        intent.putExtra("key", key);
                        intent.putExtra("recipe", recipe);
                        startActivity(intent);
                    }
                });

                listViewRecipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        showMenu(position);

                        return true;
                    }

                    private void showMenu(final int position) {
                        final Dialog dialog = new Dialog(ReviewRecipeActivity.this);
                        dialog.setTitle("Menu");
                        dialog.setContentView(R.layout.dialog_menu);

                        Button buttonDelete = dialog.findViewById(R.id.button_delete);
                        buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteRecipe(position);
                                dialog.dismiss();
                            }

                            private void deleteRecipe(final int position) {
                                final String key = keys.get(position);
                                reference.child("cooking-recipe").child(key).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        deleteRecipeNote(key);
                                        recipes.remove(position);
                                        recipeArrayAdapter.notifyDataSetChanged();
                                        Toast.makeText(ReviewRecipeActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    }

                                    private void deleteRecipeNote(final String key) {
                                        reference.child("user").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                    String uid = snapshot.getKey();
                                                    deleteNote(uid, key);
                                                }
                                                reference.child("user").removeEventListener(this);
                                            }

                                            private void deleteNote(final String uid, final String key) {
                                                reference.child("user").child(uid).child("note").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                            String keyRecipe = snapshot.getValue(String.class);
                                                            if (keyRecipe.equals(key)) {
                                                                String keyRemove = snapshot.getKey();
                                                                delete(keyRemove);
                                                            }
                                                        }
                                                        reference.child("user").child(uid).child("note").removeEventListener(this);
                                                    }

                                                    private void delete(String keyRemove) {
                                                        reference.child("user").child(uid).child("note").child(keyRemove).removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                            }
                        });

                        Button buttonEdit = dialog.findViewById(R.id.button_edit);
                        buttonEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ReviewRecipeActivity.this, EditRecipeActivity.class);

                                intent.putExtra("key", keys.get(position));
                                intent.putExtra("recipe", recipes.get(position));

                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                reference.child("cooking-recipe").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_review_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();

        reference.child("cooking-recipe").addValueEventListener(new ValueEventListener() {
            List<Recipe> recipes = new ArrayList<>();
            List<String> keys = new ArrayList<>();
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();

                    Recipe recipe = snapshot.getValue(Recipe.class);
                    String author = recipe.getAuthor();

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String userEmail = auth.getCurrentUser().getEmail();

                    if (author.equals(userEmail)) {
                        recipes.add(recipe);
                        keys.add(key);
                    }
                }
                reference.child("cooking-recipe").removeEventListener(this);
                final ArrayAdapter<Recipe> recipeArrayAdapter =
                        new ArrayAdapter<>(ReviewRecipeActivity.this,
                                android.R.layout.simple_list_item_1,
                                recipes);

                listViewRecipe.setAdapter(recipeArrayAdapter);

                listViewRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent =
                                new Intent(ReviewRecipeActivity.this,
                                        RecipeActivity.class);
                        String key = keys.get(position);
                        Recipe recipe = recipes.get(position);
                        intent.putExtra("key", key);
                        intent.putExtra("recipe", recipe);
                        startActivity(intent);
                    }
                });

                listViewRecipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        showMenu(position);

                        return true;
                    }

                    private void showMenu(final int position) {
                        final Dialog dialog = new Dialog(ReviewRecipeActivity.this);
                        dialog.setTitle("Menu");
                        dialog.setContentView(R.layout.dialog_menu);

                        Button buttonDelete = dialog.findViewById(R.id.button_delete);
                        buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteRecipe(position);
                                dialog.dismiss();
                            }

                            private void deleteRecipe(final int position) {
                                final String key = keys.get(position);
                                reference.child("cooking-recipe").child(key).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        deleteRecipeNote(key);
                                        recipes.remove(position);
                                        recipeArrayAdapter.notifyDataSetChanged();
                                        Toast.makeText(ReviewRecipeActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    }

                                    private void deleteRecipeNote(final String key) {
                                        reference.child("user").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                    String uid = snapshot.getKey();
                                                    deleteNote(uid, key);
                                                }
                                                reference.child("user").removeEventListener(this);
                                            }

                                            private void deleteNote(final String uid, final String key) {
                                                reference.child("user").child(uid).child("note").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                            String keyRecipe = snapshot.getValue(String.class);
                                                            if (keyRecipe.equals(key)) {
                                                                String keyRemove = snapshot.getKey();
                                                                delete(keyRemove);
                                                            }
                                                        }
                                                        reference.child("user").child(uid).child("note").removeEventListener(this);
                                                    }

                                                    private void delete(String keyRemove) {
                                                        reference.child("user").child(uid).child("note").child(keyRemove).removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                            }
                        });

                        Button buttonEdit = dialog.findViewById(R.id.button_edit);
                        buttonEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ReviewRecipeActivity.this, EditRecipeActivity.class);

                                intent.putExtra("key", keys.get(position));
                                intent.putExtra("recipe", recipes.get(position));

                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                reference.child("cooking-recipe").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        listViewRecipe = findViewById(R.id.list_view_recipe);
    }
}
