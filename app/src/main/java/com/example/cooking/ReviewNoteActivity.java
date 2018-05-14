package com.example.cooking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ReviewNoteActivity extends AppCompatActivity {

    ListView listViewNote;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        final String uid = auth.getCurrentUser().getUid();

        final List<Recipe> recipes = new ArrayList<>();
        final ArrayAdapter<Recipe> recipeArrayAdapter =
                new ArrayAdapter<>(
                        ReviewNoteActivity.this,
                        android.R.layout.simple_list_item_1,
                        recipes);
        listViewNote.setAdapter(recipeArrayAdapter);

        database.child("user").child(uid).child("note").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> keys = new ArrayList<>();
                final List<String> keyRecipes = new ArrayList<>();

                database.child("user").child(uid).child("note").removeEventListener(this);

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    final String keyNote = snapshot.getValue(String.class);
                    keyRecipes.add(keyNote);
                    String key = snapshot.getKey();
                    keys.add(key);
                    findNote(keyNote);

                    listViewNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ReviewNoteActivity.this, RecipeActivity.class);
                            intent.putExtra("key", keyNote);
                            intent.putExtra("recipe", recipes.get(position));
                            startActivity(intent);
                        }
                    });
                }
                listViewNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewNoteActivity.this);
                        builder.setMessage("Bạn có đồng ý xóa không");
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final List<String> keyDelete = new ArrayList<>();
                                final List<String> keyRecipes = new ArrayList<>();
                                final List<Recipe> recipes1 = new ArrayList<>();
                                database.child("user").child(uid).child("note").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final DataSnapshot data = dataSnapshot;

                                        database.child("user").child(uid).child("note").removeEventListener(this);

                                        database.child("cooking-recipe").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                database.child("user").child(uid).child("note").removeEventListener(this);
                                                database.child("cooking-recipe").removeEventListener(this);

                                                for (DataSnapshot snapshot: data.getChildren()) {
                                                    for (DataSnapshot snapshot1: dataSnapshot.getChildren()) {
                                                        String keyNoteRecipe = snapshot.getValue(String.class);
                                                        String keyRecipe = snapshot1.getKey();
                                                        if (keyRecipe.equals(keyNoteRecipe)) {
                                                            Recipe recipe = snapshot1.getValue(Recipe.class);

                                                            String author = recipe.getAuthor();
                                                            String userEmail = auth.getCurrentUser().getEmail();

                                                            if (!author.equals(userEmail)) {
                                                                keyDelete.add(snapshot.getKey());
                                                                keyRecipes.add(snapshot1.getKey());
                                                                recipes1.add(snapshot1.getValue(Recipe.class));
                                                            }
                                                        }
                                                    }
                                                }
                                                database.child("user").child(uid).child("note").removeEventListener(this);
                                                database.child("cooking-recipe").removeEventListener(this);

                                                String keyToDelete = keyDelete.get(position);
                                                database.child("user").child(uid).child("note").child(keyToDelete).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        Toast.makeText(ReviewNoteActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                                                        String keyRecipeUpdate = keyRecipes.get(position);

                                                        Recipe recipe = recipes.get(position);
                                                        int note = recipe.getNote();
                                                        if (note > 0) {
                                                            note = note - 1;
                                                        } else {
                                                            note = 0;
                                                        }

                                                        recipes.remove(position);
                                                        recipeArrayAdapter.notifyDataSetChanged();

                                                        database.child("cooking-recipe").child(keyRecipeUpdate).child("note").setValue(note);
                                                    }
                                                });
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
                        builder.show();

                        return true;
                    }
                });

                database.child("user").child(uid).child("note").removeEventListener(this);
                database.child("cooking-recipe").removeEventListener(this);
            }

            private void findNote(final String keyNote) {
                database.child("cooking-recipe").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userEmail = auth.getCurrentUser().getEmail();

                        database.child("cooking-recipe").removeEventListener(this);

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            if (key.equals(keyNote)) {
                                Recipe recipe = snapshot.getValue(Recipe.class);
                                String author = recipe.getAuthor();
                                if (!author.equals(userEmail)) {
                                    recipes.add(recipe);
                                }
                            }
                        }
                        recipeArrayAdapter.notifyDataSetChanged();
                        database.child("cooking-recipe").removeEventListener(this);
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
        listViewNote = findViewById(R.id.list_view_note);
    }
}
