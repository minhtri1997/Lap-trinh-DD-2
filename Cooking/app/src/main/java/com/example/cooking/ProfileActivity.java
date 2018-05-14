package com.example.cooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.example.cooking.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView
            textViewEmail,
            textViewNumberRecipe,
            textViewNumberShoppingList,
            textViewReviewRecipe,
            textViewReviewShoppingList,
            textViewNumberNote,
            textViewReviewNote;
    FirebaseAuth
            auth = FirebaseAuth.getInstance();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        String uid = auth.getCurrentUser().getUid();

        textViewEmail.setText(auth.getCurrentUser().getEmail());

        textViewReviewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ReviewRecipeActivity.class);
                startActivity(intent);
            }
        });
        textViewReviewShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ReviewShoppingListActivity.class);
                startActivity(intent);
            }
        });
        textViewReviewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ReviewNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        textViewEmail = findViewById(R.id.text_view_email);
        textViewNumberRecipe = findViewById(R.id.text_view_number_recipe);
        textViewNumberShoppingList = findViewById(R.id.text_view_number_shopping_list);
        textViewReviewRecipe = findViewById(R.id.text_view_review_recipe);
        textViewReviewShoppingList = findViewById(R.id.text_view_review_shopping_list);
        textViewNumberNote = findViewById(R.id.text_view_number_note);
        textViewReviewNote = findViewById(R.id.text_view_review_note);
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
}
