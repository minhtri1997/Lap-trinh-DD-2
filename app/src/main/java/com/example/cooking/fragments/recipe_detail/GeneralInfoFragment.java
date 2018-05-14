package com.example.cooking.fragments.recipe_detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cooking.R;
import com.example.cooking.models.Recipe;
import com.squareup.picasso.Picasso;

public class GeneralInfoFragment extends Fragment {

    public GeneralInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_info2, container, false);
    }

    ImageView imageViewInfo;
    TextView textViewAuthor;
    TextView textViewType;
    TextView textViewRation;
    TextView textViewNote;

    Listener listener;

    @Override
    public void onResume() {
        super.onResume();
        listener.setInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        imageViewInfo = view.findViewById(R.id.image_view_info);
        textViewAuthor = view.findViewById(R.id.text_view_author);
        textViewNote = view.findViewById(R.id.text_view_note);
        textViewRation = view.findViewById(R.id.text_view_ration);
        textViewType = view.findViewById(R.id.text_view_type);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    public void setDataForView(Recipe recipe) {
        Picasso.get().load(recipe.getImage()).into(imageViewInfo);
        textViewAuthor.setText("Tác giả            : " + recipe.getAuthor());
        textViewType.setText("Loại                 : " + recipe.getType());
        textViewRation.setText("Khẩu phần      : " + String.valueOf(recipe.getRation()));
        textViewNote.setText("Lượt ghi nhớ  : " + String.valueOf(recipe.getNote()));
    }

    public interface Listener {
        void setInfo();
    }
}
