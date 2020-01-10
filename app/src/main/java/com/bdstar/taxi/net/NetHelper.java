package com.bdstar.taxi.net;

import java.util.ArrayList;
import java.util.List;

public enum NetHelper {
    INSTANCE;

    private List<String> errorLinks;

    NetHelper(){
        errorLinks = new ArrayList<>();
    }

    public boolean isNetOk(){
        return errorLinks.size() == 0;
    }

    public void putErrorLink(String errorLink){
        for (String err: errorLinks){
            if (err.equals(errorLink)) return;
        }

        errorLinks.add(errorLink);
    }

    public void removeErrorLink(String errorLink){

    }
}
