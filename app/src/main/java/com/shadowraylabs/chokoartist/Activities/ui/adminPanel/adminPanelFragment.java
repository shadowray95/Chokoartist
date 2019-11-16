package com.shadowraylabs.chokoartist.Activities.ui.adminPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shadowraylabs.chokoartist.Activities.AddProductActivity;
import com.shadowraylabs.chokoartist.R;


public class adminPanelFragment extends Fragment {

    private adminPanelViewModel adminPanelViewModel;
    private Button addNewProduct, removeProduct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminPanelViewModel =
                ViewModelProviders.of(this).get(adminPanelViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_panel, container, false);

        addNewProduct = (Button)root.findViewById(R.id.admin_add_product);
        removeProduct = (Button) root.findViewById(R.id.admin_remove_product);

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });

        removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }
}