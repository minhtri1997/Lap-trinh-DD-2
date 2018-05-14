package com.example.cooking.fragments.create_recipe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cooking.R;

import java.util.ArrayList;
import java.util.List;

public class AddGuideFragment extends Fragment {

    FloatingActionButton buttonAddGuide;
    Dialog dialogAddGuide;
    ListView listViewGuide;

    ArrayAdapter<String> guideAdapter;
    List<String> guides;

    Button buttonBack;

    public interface Listener {
        void onAddGuideFragPause();

        void onAddGuideFragResume();
    }

    Listener listener;

    public AddGuideFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_guide, container, false);

        buttonAddGuide = view.findViewById(R.id.button_add_guide);
        buttonAddGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddGuide();
            }
        });

        guides = new ArrayList<>();
        guideAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, guides);

        listViewGuide = view.findViewById(R.id.list_view_guide);
        listViewGuide.setAdapter(guideAdapter);

        listViewGuide.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delete(position);
                return true;
            }
        });

        buttonBack = view.findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void delete(final int positon) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Xóa");
        dialog.setMessage("Bạn có đồng ý xóa không");

        dialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guides.remove(positon);
                guideAdapter.notifyDataSetChanged();
            }
        });

        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (Listener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onAddGuideFragResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        listener.onAddGuideFragPause();
    }

    public void getListGuide(List<String> guides) {
        this.guides = guides;
        guideAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, this.guides);
        listViewGuide.setAdapter(guideAdapter);
    }

    public List<String> sendListGuide() {
        return this.guides;
    }

    private void showDialogAddGuide() {
        dialogAddGuide = new Dialog(getContext());
        dialogAddGuide.setTitle("Thêm hướng dẫn");
        dialogAddGuide.setContentView(R.layout.dialog_add_guide);

        Button buttonCancel = dialogAddGuide.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddGuide.cancel();
            }
        });

        Button buttonAddGuide = dialogAddGuide.findViewById(R.id.button_add_guide);
        buttonAddGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editGuide = dialogAddGuide.findViewById(R.id.edit_guide);
                if (!editGuide.getText().toString().equals("")) {
                    String guide = editGuide.getText().toString();
                    guides.add(guide);
                    guideAdapter.notifyDataSetChanged();
                    dialogAddGuide.dismiss();
                } else {
                    Toast.makeText(getContext(), "Bạn chưa nhập thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogAddGuide.show();
    }
}
