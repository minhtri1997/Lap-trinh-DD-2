package com.example.cooking;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cooking.fragments.recipe_detail.GeneralInfoFragment;
import com.example.cooking.fragments.recipe_detail.GuideFragment;
import com.example.cooking.fragments.recipe_detail.MaterialFragment;
import com.example.cooking.models.Recipe;
import com.example.cooking.models.UserNote;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity
        implements GeneralInfoFragment.Listener, MaterialFragment.Listener, GuideFragment.Listener {

    Toolbar toolbar;
    String key;
    Recipe recipe;

    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<String> values;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    String uid = auth.getCurrentUser().getUid().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            key = intent.getStringExtra("key");
            recipe = (Recipe) intent.getSerializableExtra("recipe");
        }

        getSupportActionBar().setTitle(recipe.getName());

        SectionsPagerAdapter pagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        values = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        myRef.child("user").child(uid).child("note").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(String.class);
                    if (value.equals(key)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    addNote();
                } else {
                    Toast.makeText(RecipeActivity.this, "Bạn đã ghi nhớ công thức này rồi", Toast.LENGTH_SHORT).show();
                }
                myRef.child("user").child(uid).child("note").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RecipeActivity.this, "Ghi nhớ thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNote() {
        UserNote note = new UserNote();
        note.setUserID(uid);
        note.setKeyNote(key);
        myRef.child("user").child(uid).child("note").push().setValue(key, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    increaseNote();
                    Toast.makeText(RecipeActivity.this, "Ghi nhớ thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecipeActivity.this, "Ghi nhớ thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void increaseNote() {
        myRef.child("cooking-recipe").child(key).child("note").setValue(recipe.getNote() + 1);
    }

    @Override
    public void setInfo() {
        FragmentManager manager = getSupportFragmentManager();
        GeneralInfoFragment fragment = (GeneralInfoFragment) manager.findFragmentByTag("android:switcher:" + R.id.pager + ":" + 0);
        fragment.setDataForView(recipe);
    }

    @Override
    public void setDataForListView() {
        FragmentManager manager = getSupportFragmentManager();
        MaterialFragment fragment = (MaterialFragment) manager.findFragmentByTag("android:switcher:" + R.id.pager + ":" + 1);
        fragment.setDataForListView(recipe);
    }

    @Override
    public void setDataListViewGuide() {
        FragmentManager manager = getSupportFragmentManager();
        GuideFragment fragment = (GuideFragment) manager.findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);
        fragment.setDataForListViewGuide(recipe);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GeneralInfoFragment();
                case 1:
                    return new MaterialFragment();
                case 2:
                    return new GuideFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Thông tin chung";
                case 1:
                    return "Nguyên liệu";
                case 2:
                    return "Hướng dẫn";
            }
            return null;
        }
    }
}
