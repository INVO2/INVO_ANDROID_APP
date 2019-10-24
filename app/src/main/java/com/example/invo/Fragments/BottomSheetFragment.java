package com.example.invo.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.invo.Interface.ButtonPressInterface;
import com.example.invo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment  {

    private TextView name,address,site,time,phone;
    private ArrayList<String> lat;
    private ButtonPressInterface mDataPasser;
    ImageView img;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
//        // Inflate the layout for this fragment
        name = rootView.findViewById(R.id.name);
        address = rootView.findViewById(R.id.address);
        site = rootView.findViewById(R.id.site);
        img = rootView.findViewById(R.id.img);
        phone = rootView.findViewById(R.id.phone);
        Bundle bundle = getArguments();
        assert bundle != null;

        Picasso.get().load(bundle.getString("img")).error(R.drawable.error_pharmacy).fit().centerCrop().into(img);
        phone.setText(bundle.getString("phone"));
        name.setText(bundle.getString("name"));
        address.setText(bundle.getString("address"));
        site.setText(bundle.getString("site"));




        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataPasser.buttonClick();
            }
        });




        return rootView;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        mDataPasser = (ButtonPressInterface) a;

    }

}