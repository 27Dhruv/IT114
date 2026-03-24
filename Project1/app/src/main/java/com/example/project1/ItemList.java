package com.example.project1;

import java.util.ArrayList;

public class ItemList {

    private static ItemList instance;
    private ArrayList<House> list;

    private ItemList() {
        list = new ArrayList<>();
    }

    public static ItemList getInstance() {
        if (instance == null) {
            instance = new ItemList();
        }
        return instance;
    }

    public void addHouse(House h) {
        list.add(h);
    }

    public ArrayList<House> getList() {
        return list;
    }

    public void clear() {
        list.clear();
    }

    public House findByLotNumber(String lot) {
        for (House h : list) {
            if (h.getLotNumber().equalsIgnoreCase(lot)) {
                return h;
            }
        }
        return null;
    }

    public boolean removeByLotNumber(String lot) {
        House h = findByLotNumber(lot);
        if (h != null) {
            list.remove(h);
            return true;
        }
        return false;
    }
}