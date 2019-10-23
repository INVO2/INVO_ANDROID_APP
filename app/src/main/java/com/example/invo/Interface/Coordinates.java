package com.example.invo.Interface;

import com.example.invo.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Coordinates  {
     Map<Integer, LatLng> coordinatesMap = new HashMap<Integer, LatLng>();
     List<LatLng> pls = new ArrayList<>();


     default void setCoordinates(){
         pls.add(new LatLng(53.919455, 27.592188));//1
         pls.add(new LatLng(53.918463, 27.589635));//2
         pls.add(new LatLng(53.916721, 27.585291));//3
         pls.add(new LatLng(53.914105, 27.581271));//4
         pls.add(new LatLng(53.914343, 27.580823));//5


     }
}
