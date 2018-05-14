package com.example.cooking.fragments.create_recipe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cooking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GeneralInfoFragment extends Fragment {

    public interface Listener {
        void clickButtonChooseImage();

        void clickButtonNextInGeneralInfoFrag();

        void onGeneralInfoFragPause();

        void onGeneralInfoFragResume();
    }

    Listener listener;

    Uri imageUri;

    ImageView imageViewInfo;
    Button buttonChooseImage;
    Spinner spinnerType;
    EditText editTextRecipeName;
    EditText editTextRation;
    Button buttonNextInGeneralFrag;

    List<String> types;

    public GeneralInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general_info, container, false);

        buttonChooseImage = view.findViewById(R.id.button_choose_image);
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickButtonChooseImage();
            }
        });

        imageViewInfo = view.findViewById(R.id.image_view_info);

        spinnerType = view.findViewById(R.id.spinner_type);
        initSpinnerType();

        editTextRecipeName = view.findViewById(R.id.edit_text_recipe_name);
        editTextRation = view.findViewById(R.id.edit_text_ration);

        buttonNextInGeneralFrag = view.findViewById(R.id.button_next_in_general_frag);
        buttonNextInGeneralFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickButtonNextInGeneralInfoFrag();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        listener.onGeneralInfoFragPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (imageUri != null) {
            imageViewInfo.setImageURI(imageUri);
        }
        listener.onGeneralInfoFragResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    public int sendRation() {
        String strRation = editTextRation.getText().toString();
        if (!strRation.equals("")) {
            return Integer.parseInt(strRation);
        }
        return 0;
    }

    public String sendRecipeType() {
        String recipeType = spinnerType.getSelectedItem().toString();
        if (!recipeType.equals("")) {
            return recipeType;
        }
        return "";
    }

    public String sendRecipeName() {
        String recipeName = editTextRecipeName.getText().toString();
        if (!recipeName.equals("")) {
            return recipeName;
        }
        return "";
    }

    public void setInfo(String recipeName, int ration, String recipeType) {
        editTextRecipeName.setText(recipeName);
        if (ration != 0) {
            editTextRation.setText(String.valueOf(ration));
        }
        for (int i = 0; i < types.size(); i++) {
            if (recipeType.equals(types.get(i))) {
                spinnerType.setSelection(i);
                break;
            }
        }
    }

    public void setImageInfo(Uri uri) {
        Picasso.get()
                .load(uri)
                .resize(400, 300)
                .centerCrop()
                .into(imageViewInfo);
//        imageViewInfo.setImageURI(uri);
        imageUri = uri;
    }

    private void initSpinnerType() {
        types = new ArrayList<>();
        types.add("Món khai vị");
        types.add("Món chính");
        types.add("Món tráng miệng");
        types.add("Thức uống");
        types.add("Bánh ngọt");
        types.add("Món chay");
        types.add("Ăn vặt");
        types.add("Khác");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, types);

        spinnerType.setAdapter(typeAdapter);
    }
}
