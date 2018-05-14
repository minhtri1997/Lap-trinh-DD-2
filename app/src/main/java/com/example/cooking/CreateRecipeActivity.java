package com.example.cooking;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cooking.fragments.create_recipe.AddGuideFragment;
import com.example.cooking.fragments.create_recipe.AddMaterialFragment;
import com.example.cooking.fragments.create_recipe.GeneralInfoFragment;
import com.example.cooking.models.Recipe;
import com.example.cooking.models.Material;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity
        implements GeneralInfoFragment.Listener, AddMaterialFragment.Listener, AddGuideFragment.Listener {

    static final int REQUEST_CODE = 1;

    Toolbar toolbar;
    FrameLayout frameLayoutContent;

    Uri fullPhotoUri;
    String recipeName;
    int ration;
    String recipeType;
    List<Material> materials;
    List<String> guides;
    int note;

    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageRef;

    Recipe recipe;

    StorageReference riversRef;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        materials = new ArrayList<>();
        guides = new ArrayList<>();
        recipeName = "";
        recipeType = "";
        note = 0;

        storage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef = storage.getReference();

        frameLayoutContent = findViewById(R.id.frame_layout_content);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        GeneralInfoFragment generalInfoFragment = new GeneralInfoFragment();

        transaction.replace(R.id.frame_layout_content, generalInfoFragment, "general_info_frag");
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cooking_recipe, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_done:
                if (getRecipe()) {
                    saveRecipe();
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveRecipe() {
        databaseReference.child("cooking-recipe").push().setValue(recipe, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    String key = databaseReference.getKey();
                    saveImage(key);
                    saveUserKey(key);
                } else {
                    Toast.makeText(CreateRecipeActivity.this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserKey(String key) {
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("user").child(uid).child("note").push().setValue(key);
    }

    private void saveImage(String key) {
        Uri file = fullPhotoUri;

        riversRef = storageRef.child("images/" + key + ".jpg");

        final Image image = new Image();
        image.setKey(key);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        image.setUri(downloadUrl.toString());
                        updateImageNode(image);
                        endActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(CreateRecipeActivity.this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void endActivity() {
        this.finish();
    }

    private void updateImageNode(Image image) {
        databaseReference.child("cooking-recipe").child(image.getKey()).child("image").setValue(image.getUri());
    }

    private class Image {
        private String key;
        private String Uri;

        public Image() {
        }

        public Image(String key, String uri) {
            this.key = key;
            Uri = uri;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUri() {
            return Uri;
        }

        public void setUri(String uri) {
            Uri = uri;
        }
    }

    private boolean getRecipe() {
        boolean ok = true;

        GeneralInfoFragment fragment =
                (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag("general_info_frag");

        if (!fragment.sendRecipeName().equals(null)) {
            recipeName = fragment.sendRecipeName();
        } else {
            ok = false;
        }

        if (fragment.sendRation() != 0) {
            ration = fragment.sendRation();
        } else {
            ok = false;
        }

        if (!fragment.sendRecipeType().equals(null)) {
            recipeType = fragment.sendRecipeType();
        } else {
            ok = false;
        }

        recipe = new Recipe();

        if (fullPhotoUri != null) {
            recipe.setImage(fullPhotoUri.toString());
        } else {
            ok = false;
        }

        if (materials.size() == 0) {
            ok = false;
        }

        if (guides.size() == 0) {
            ok = false;
        }

        if (ok) {
            recipe.setName(recipeName);
            recipe.setRation(ration);
            recipe.setType(recipeType);
            recipe.setMaterials(materials);
            recipe.setGuideSteps(guides);
            recipe.setAuthor(auth.getCurrentUser().getEmail().toString());
            recipe.setNote(note);
        } else {
            Toast.makeText(this, "Bạn chưa nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }

        return ok;
    }

    @Override
    public void clickButtonChooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void clickButtonNextInGeneralInfoFrag() {
        GeneralInfoFragment fragment =
                (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag("general_info_frag");

        recipeName = fragment.sendRecipeName();
        ration = fragment.sendRation();
        recipeType = fragment.sendRecipeType();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_content, new AddMaterialFragment(), "add_material_frag");
        transaction.addToBackStack("add_material_frag");
        transaction.commit();
    }

    @Override
    public void onGeneralInfoFragPause() {
        GeneralInfoFragment fragment =
                (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag("general_info_frag");
        recipeName = fragment.sendRecipeName();
        ration = fragment.sendRation();
        recipeType = fragment.sendRecipeType();
    }

    @Override
    public void onGeneralInfoFragResume() {
        GeneralInfoFragment fragment =
                (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag("general_info_frag");

        fragment.setInfo(recipeName, ration, recipeType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            fullPhotoUri = data.getData();

            GeneralInfoFragment fragment =
                    (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag("general_info_frag");
            fragment.setImageInfo(fullPhotoUri);
        }
    }

    @Override
    public void onAddMaterialFragmentPause() {
        FragmentManager manager = getSupportFragmentManager();
        AddMaterialFragment fragment = (AddMaterialFragment) manager.findFragmentByTag("add_material_frag");
        materials = fragment.sendListMaterial();
    }

    @Override
    public void onAddMaterialFragmentResume() {
        FragmentManager manager = getSupportFragmentManager();
        AddMaterialFragment fragment = (AddMaterialFragment) manager.findFragmentByTag("add_material_frag");
        fragment.getListMaterial(materials);
    }

    @Override
    public void clickButtonNextInAddMaterialFrag() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout_content, new AddGuideFragment(), "add_guide_frag");
        transaction.addToBackStack("add_guide_frag");
        transaction.commit();
    }

    @Override
    public void onAddGuideFragPause() {
        FragmentManager manager = getSupportFragmentManager();
        AddGuideFragment fragment = (AddGuideFragment) manager.findFragmentByTag("add_guide_frag");

        this.guides = fragment.sendListGuide();
    }

    @Override
    public void onAddGuideFragResume() {
        FragmentManager manager = getSupportFragmentManager();
        AddGuideFragment fragment = (AddGuideFragment) manager.findFragmentByTag("add_guide_frag");

        fragment.getListGuide(this.guides);
    }
}
