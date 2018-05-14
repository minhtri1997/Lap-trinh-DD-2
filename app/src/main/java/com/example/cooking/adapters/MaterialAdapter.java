package com.example.cooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cooking.R;
import com.example.cooking.models.Material;

import java.util.List;

public class MaterialAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Material> materials;

    public MaterialAdapter(Context context, List<Material> materials) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.materials = materials;
    }

    @Override
    public int getCount() {
        return materials.size();
    }

    @Override
    public Object getItem(int position) {
        return materials.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.row_material, null);

        TextView textName = convertView.findViewById(R.id.text_name);
        TextView textNumber = convertView.findViewById(R.id.text_number);
        TextView textUnit = convertView.findViewById(R.id.text_unit);

        Material material = materials.get(position);

        textName.setText(material.getName());
        textNumber.setText(material.getNumber());
        textUnit.setText(material.getUnit());

        return convertView;
    }
}
