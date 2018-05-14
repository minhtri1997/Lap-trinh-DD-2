package com.example.cooking.fragments.recipe_detail;


import android.content.Context;
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
import com.example.cooking.models.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialFragment extends Fragment {


    public MaterialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_material, container, false);
    }

    ListView listViewMaterial;
    List<Material> materials;
    MaterialAdapter materialAdapter;
    Listener listener;

    @Override
    public void onResume() {
        super.onResume();
        listener.setDataForListView();
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        listViewMaterial = view.findViewById(R.id.list_view_material);
        materials = new ArrayList<>();
        materialAdapter = new MaterialAdapter(getContext(), materials);
        listViewMaterial.setAdapter(materialAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    public void setDataForListView(Recipe recipe) {
        materials = recipe.getMaterials();
        materialAdapter = new MaterialAdapter(getContext(), materials);
        listViewMaterial.setAdapter(materialAdapter);
    }

    public interface Listener {
        void setDataForListView();
    }
}
