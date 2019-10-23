package com.example.invo.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.invo.Adapter.CustomGridViewActivity;
import com.example.invo.R;


public class ListFragment extends Fragment  {

    BottomNavigationView bottomNavigationView;
    Button list_bt ;
    GridView androidGridView;

    String[] gridViewString = {
            "Pharmacy","Hospital","Include", "Shop", "Sanatoriums","Taxi"

    } ;
    int[] gridViewImageId = {
            R.drawable.pharmacy, R.drawable.hospital, R.drawable.walker,R.drawable.groceries,R.drawable.sunset,R.drawable.taxi,

    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_list, container, false);

       bottomNavigationView= (BottomNavigationView) container.findViewById(R.id.main_nov);


        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(getActivity(), gridViewString, gridViewImageId);
        androidGridView=(GridView)v.findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Toast.makeText(getActivity(), "GridView Item: " + gridViewString[+i]+"i = "+i, Toast.LENGTH_LONG).show();
                if(i==0) {
                    Intent intent = new Intent(getActivity(), PharmacyFragment.class);


                    PharmacyFragment pharmacy = new PharmacyFragment();

                    getActivity().getFragmentManager().beginTransaction().replace(
                            R.id.fragment_conteiner, pharmacy, "findThisFragment"
                    ).addToBackStack(null).commit();

                }
                if(i==1) {
                    Intent intent = new Intent(getActivity(), PharmacyFragment.class);


                    PharmacyFragment pharmacy = new PharmacyFragment();

                    getActivity().getFragmentManager().beginTransaction().replace(
                            R.id.fragment_conteiner, pharmacy, "findThisFragment"
                    ).addToBackStack(null).commit();

                }
                if(i==2) {
                    Intent intent = new Intent(getActivity(), PharmacyFragment.class);


                    PharmacyFragment pharmacy = new PharmacyFragment();

                    getActivity().getFragmentManager().beginTransaction().replace(
                            R.id.fragment_conteiner, pharmacy, "findThisFragment"
                    ).addToBackStack(null).commit();

                }
            }
        });

       return v;

    }


}