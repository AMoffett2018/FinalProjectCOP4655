package com.example.elliestockapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserClass {
    public String id;
    public List<String> favList;

    public UserClass() {

    }

    public UserClass(String id) {
        this.id = id;
        this.favList = new ArrayList<>();
        favList.add("APPL");
    }
}
