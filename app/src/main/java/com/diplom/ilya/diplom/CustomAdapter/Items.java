package com.diplom.ilya.diplom.CustomAdapter;

import java.util.Objects;

/**
 * Created by user on 13.04.17.
 */
public class Items {
    private String name;
    private String result;

    public Items(String n, String r) {
        name = n;
        result = r;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return name;
    }
}

