package com.shadowraylabs.chokoartist.Activities.ui.contactUs;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shadowraylabs.chokoartist.Activities.AdminMainActivity;
import com.shadowraylabs.chokoartist.Activities.ui.cart.CartViewModel;
import com.shadowraylabs.chokoartist.Model.Users;
import com.shadowraylabs.chokoartist.R;

public class contactUsFragment extends Fragment {

    private ContactUsViewModel contactUsViewModel;

    private Button callUs, mailUs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactUsViewModel =
                ViewModelProviders.of(this).get(ContactUsViewModel.class);
        View root = inflater.inflate(R.layout.contact_us_fragment, container, false);

        callUs = (Button) root.findViewById(R.id.contact_us_call);
        mailUs = (Button) root.findViewById(R.id.contact_us_mail);
        final Users userObj = ((AdminMainActivity)this.getActivity()).getUserObject();

        callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9643063858"));
                startActivity(callIntent);
            }
        });

        mailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "sakshijaindelhi22@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Chokoartist : "+ userObj.getPhoneNo() + " | " + userObj.getName());
                    intent.putExtra(Intent.EXTRA_CC, "navneet895@gmail.com");
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(getContext(), "sakshijaindelhi22@gmail.com", Toast.LENGTH_LONG).show();
                }

            }
        });
        return root;
    }

}
