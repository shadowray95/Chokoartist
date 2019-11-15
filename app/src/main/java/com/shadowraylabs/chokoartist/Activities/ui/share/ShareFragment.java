package com.shadowraylabs.chokoartist.Activities.ui.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shadowraylabs.chokoartist.Activities.AdminMainActivity;
import com.shadowraylabs.chokoartist.Model.Users;
import com.shadowraylabs.chokoartist.Constants.AppConstants;
import com.shadowraylabs.chokoartist.R;

import org.w3c.dom.Text;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;

    TextView promotionText;
    TextView currentReferrals;
    Button shareBtn;

    Users userObj;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        promotionText = (TextView) root.findViewById(R.id.share_promotion_text);
        currentReferrals = (TextView) root.findViewById(R.id.share_current_referrals);
        shareBtn = (Button) root.findViewById(R.id.share_button);
        userObj = ((AdminMainActivity)this.getActivity()).getUserObject();

        promotionText.setText("Earn " + AppConstants.promotionPoints + " choko points for every 5 referrals");
        currentReferrals.setText("Current Referrals: "+ userObj.getNoOfReferrals());
        currentReferrals.append("\n Total Store Credits: "+ userObj.getStoreCredits());

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey There! Checkout this app I found on Playstore which offers to buy freshly handmade chocolates for cheap prices!!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ChokoArtist - Buy Freshly Handmade Chocolates Online!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        return root;
    }
}