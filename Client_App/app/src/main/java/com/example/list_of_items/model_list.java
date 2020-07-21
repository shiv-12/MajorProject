package com.example.list_of_items;

import java.util.ArrayList;
import java.util.List;

public class model_list {

    List<String> vegiee_name = new ArrayList<>();
    List<String> qty = new ArrayList<>();
    String username, usermobile,totalprice;

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public List<String> getVegiee_name() {
        return vegiee_name;
    }

    public void setVegiee_name(String vegiee_name) {
        this.vegiee_name.add(vegiee_name);
    }

    public List<String> getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty.add(qty);
    }
}
