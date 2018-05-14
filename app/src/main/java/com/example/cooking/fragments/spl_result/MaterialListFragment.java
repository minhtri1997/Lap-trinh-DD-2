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
import com.example.cooking.adapters.MaterialAdapter;
import com.example.cooking.models.Material;
import com.example.cooking.models.MaterialSPL;
import com.example.cooking.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialListFragment extends Fragment {

    ListView listViewMaterial;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    List<Recipe> recipes;

    public MaterialListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_material_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        listViewMaterial = view.findViewById(R.id.list_view_material);

        recipes = new ArrayList<>();

        Intent intent = getActivity().getIntent();
        String dateStr = intent.getStringExtra("date");

        reference.child("user").child(auth.getCurrentUser().getUid()).child("shopping-list").child(dateStr).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    recipes.add(snapshot.getValue(Recipe.class));
                }

                List<Material> materials = new ArrayList<>();
                for (Recipe recipe : recipes) {
                    for (Material material : recipe.getMaterials()) {
                        materials.add(material);
                    }
                }

                List<MaterialSPL> materialSPLS = new ArrayList<>();

                List<Integer> deleteIndex = new ArrayList<>();

                for (int i = 0; i < materials.size(); i++) {

                    if (deleteIndex.contains(i)) {
                        continue;
                    }

                    for (int j = i + 1; j < materials.size(); j++) {

                        if (deleteIndex.contains(j)) {
                            continue;
                        }

                        if (materials.get(i).getName().equals(materials.get(j).getName())) {
                            if (materials.get(i).getUnit().equals(materials.get(j).getUnit())) {
                                if (isNumber(materials.get(i).getNumber()) && isNumber(materials.get(j).getNumber())) {
                                    Double q = Double.parseDouble(materials.get(i).getNumber()) + Double.parseDouble(materials.get(j).getNumber());
                                    materials.get(i).setNumber(String.valueOf(q));
//                                    materials.remove(j);
                                    deleteIndex.add(j);
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < materials.size(); i++) {

                    if (deleteIndex.contains(i)) {
                        continue;
                    }

                    MaterialSPL materialSPL = new MaterialSPL();
                    materialSPL.setName(materials.get(i).getName());
                    String quantity = materials.get(i).getNumber() + " " + materials.get(i).getUnit();

                    for (int j = i + 1; j < materials.size(); j++) {

                        if (deleteIndex.contains(j)) {
                            continue;
                        }

                        if (materials.get(i).getName().equals(materials.get(j).getName())) {
                            quantity = quantity + " + " + materials.get(j).getNumber() + " " + materials.get(j).getUnit();
                            deleteIndex.add(j);
                        }
                    }

                    materialSPL.setQuantity(quantity);
                    materialSPLS.add(materialSPL);
                }

                ArrayAdapter<MaterialSPL> materialAdapter = new ArrayAdapter<MaterialSPL>(getContext(), android.R.layout.simple_list_item_1, materialSPLS);

                listViewMaterial.setAdapter(materialAdapter);

                reference.child("user").child(auth.getCurrentUser().getUid()).child("shopping-list").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isNumber(String data) {
        try {
            Double.parseDouble(data);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }
}
