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
import com.example.cooking.models.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {


    public GuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    ListView listViewGuide;
    Listener listener;
    List<String> guides;
    ArrayAdapter<String> guideAdapter;

    @Override
    public void onResume() {
        super.onResume();
        listener.setDataListViewGuide();
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        listViewGuide = view.findViewById(R.id.list_view_guide);
        guides = new ArrayList<>();
        guideAdapter = new ArrayAdapter<>(getContext(), android.R.layout.activity_list_item, guides);
        listViewGuide.setAdapter(guideAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    public void setDataForListViewGuide(Recipe recipe) {
        guides = recipe.getGuideSteps();
        guideAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, guides);
        listViewGuide.setAdapter(guideAdapter);
    }

    public interface Listener {
        void setDataListViewGuide();
    }
}
