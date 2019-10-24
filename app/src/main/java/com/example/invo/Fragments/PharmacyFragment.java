package com.example.invo.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.invo.MainActivity;
import com.example.invo.Parsing.Retrofit.Interface.RecyclerInterface;
import com.example.invo.Parsing.Retrofit.ModelRecycler;
import com.example.invo.Parsing.Retrofit.RetrofitAdapter;
import com.example.invo.R;
import com.example.invo.Interface.latLong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class PharmacyFragment extends Fragment {

    public latLong mDataPasser;
    private ArrayList<String> scrImg,lat,longi,address,name;
    public ArrayList<ModelRecycler> modelRecyclerArrayList;
    MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RetrofitAdapter retrofitAdapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View rootView = inflater.inflate(R.layout.fragment_pharmacy, container, false);
        mainActivity = new MainActivity();
        retrofitAdapter = new RetrofitAdapter(this.getContext(),modelRecyclerArrayList);
        recyclerView = rootView.findViewById(R.id.recycler);
        fetchJSON();

        return rootView;
    }


    public void fetchJSON(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);

        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeRecycler(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void writeRecycler(String response){

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);

            lat = new ArrayList<>();
            longi= new ArrayList<>();
            scrImg=new ArrayList<>();
            name=new ArrayList<>();
            address=new ArrayList<>();

            modelRecyclerArrayList = new ArrayList<>();
            JSONArray dataArray  = obj.getJSONArray("items");

            for (int i = 0; i < dataArray.length(); i++) {

                ModelRecycler modelRecycler = new ModelRecycler();
                JSONObject dataobj = dataArray.getJSONObject(i);

                modelRecycler.setPhone(dataobj.getString("phone"));
                modelRecycler.setSite(dataobj.getString("detailHref"));
                modelRecycler.setImgURL(dataobj.getString("scrImg"));
                modelRecycler.setName(dataobj.getString("name"));
                modelRecycler.setCountry(dataobj.getString("address"));
                modelRecycler.setLatti(dataobj.getString("lat"));
                modelRecycler.setLongi(dataobj.getString("long"));

                scrImg.add(dataobj.getString("lat"));
                name.add(dataobj.getString("lat"));
                address.add(dataobj.getString("lat"));
                lat.add(dataobj.getString("lat"));
                longi.add(dataobj.getString("long"));

                modelRecyclerArrayList.add(modelRecycler);

            }

//            Toast.makeText(getActivity(),mainActivity.phLatti.get(1),Toast.LENGTH_SHORT).show();




            mDataPasser.seeee(lat,longi,modelRecyclerArrayList);


            retrofitAdapter = new RetrofitAdapter(getActivity(),modelRecyclerArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(retrofitAdapter);
            retrofitAdapter.setOnItemClickListener((RetrofitAdapter.OnItemClickListener) getActivity());




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getLat() {
        return lat;
    }

    public ArrayList<String> getLongi() {
        return longi;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        mDataPasser = (latLong) a;

    }


}