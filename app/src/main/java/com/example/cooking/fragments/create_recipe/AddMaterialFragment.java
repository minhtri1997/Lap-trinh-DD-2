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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cooking.R;
import com.example.cooking.adapters.MaterialAdapter;
import com.example.cooking.models.Material;

import java.util.ArrayList;
import java.util.List;

public class AddMaterialFragment extends Fragment {

    FloatingActionButton buttonAddMaterial;
    ListView listViewMaterial;

    List<Material> materials;
    MaterialAdapter materialAdapter;
    Button buttonNextInMaterialFrag, buttonBack;

    public interface Listener {
        void onAddMaterialFragmentPause();

        void onAddMaterialFragmentResume();

        void clickButtonNextInAddMaterialFrag();
    }

    Listener listener;

    public AddMaterialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_material, container, false);

        buttonAddMaterial = view.findViewById(R.id.button_add_material);
        buttonAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddMaterial();
            }
        });

        materials = new ArrayList<>();
        materialAdapter = new MaterialAdapter(getContext(), materials);

        listViewMaterial = view.findViewById(R.id.list_material);
        listViewMaterial.setAdapter(materialAdapter);

        listViewMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        buttonNextInMaterialFrag = view.findViewById(R.id.button_next_in_add_material_frag);
        buttonNextInMaterialFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickButtonNextInAddMaterialFrag();
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
                materials.remove(positon);
                materialAdapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        listener.onAddMaterialFragmentResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        listener.onAddMaterialFragmentPause();
    }

    public void getListMaterial(List<Material> materials) {
        this.materials = materials;
        materialAdapter = new MaterialAdapter(getContext(), this.materials);
        listViewMaterial.setAdapter(materialAdapter);
    }

    public List<Material> sendListMaterial() {
        return materials;
    }

    private void showDialogAddMaterial() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_material);

        dialog.setTitle("Nhập nguyên liệu");

        Button buttonCancel = dialog.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Button buttonAddMaterial = dialog.findViewById(R.id.button_add_material);
        buttonAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Material material = new Material();

                EditText editTextMaterialName = dialog.findViewById(R.id.edit_text_material_name);
                EditText editTextNumberMaterial = dialog.findViewById(R.id.edit_text_number_material);
                EditText editTextUnit = dialog.findViewById(R.id.edit_unit);

                if (!editTextMaterialName.getText().toString().equals("") &&
                        !editTextNumberMaterial.getText().toString().equals("") &&
                        !editTextUnit.getText().toString().equals("")) {

                    material.setName(editTextMaterialName.getText().toString());
                    material.setNumber(editTextNumberMaterial.getText().toString());
                    material.setUnit(editTextUnit.getText().toString());

                    materials.add(material);
                    materialAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Bạn chưa nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
