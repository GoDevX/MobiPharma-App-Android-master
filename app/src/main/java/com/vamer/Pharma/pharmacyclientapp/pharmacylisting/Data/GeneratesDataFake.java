package com.vamer.Pharma.pharmacyclientapp.pharmacylisting.Data;


import com.vamer.Pharma.pharmacyclientapp.R;

import java.util.ArrayList;

/**
 * Created by Rasoul Miri on 8/23/17.
 */

public class GeneratesDataFake {

    private ArrayList<DataObject> dataObjects = new ArrayList<>();

    public GeneratesDataFake() {
    }

    private ArrayList<String> titles = new ArrayList<String>() {
        {
            add("seif");
            add("ezaby");
            add("ezaby");
            add("ezaby");
            add("elnahdy");

        }
    };
    private ArrayList<String> descriptions = new ArrayList<String>() {
        {
            add("Description");
            add("Description");
            add("Description");
            add("Description");
            add("Description");

        }
    };

    private ArrayList<Integer> photos = new ArrayList<Integer>() {
        {
            add(R.drawable.seif);
            add(R.drawable.elezaby);
            add(R.drawable.elezaby);
            add(R.drawable.elezaby);
            add(R.drawable.elnahdy);

        }
    };


    private ArrayList<String> prices = new ArrayList<String>() {
        {
            add("4");
            add("2");
            add("4");
            add("2");
            add("4");
        }
    };

    public ArrayList<DataObject> generateDataFake() {
        for (int i = 0; i < 5; i++) {
            DataObject dataObject = new DataObject();
            dataObject.setId(i);
            dataObject.setTitle(titles.get(i));
            dataObject.setDescription(descriptions.get(i));
            dataObject.setPhoto(photos.get(i));
            dataObject.setPrice(prices.get(i));
            dataObject.setNew(true);
            dataObjects.add(dataObject);
        }
        return dataObjects;
    }
}
