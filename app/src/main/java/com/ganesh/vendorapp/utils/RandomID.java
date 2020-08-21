package com.ganesh.vendorapp.utils;

import java.util.UUID;

public class RandomID {

    public String getRandomID() {
        UUID uniqueKey = UUID.randomUUID();
        String id = uniqueKey.toString();
        return "PR" + id.substring(0, id.indexOf("-"));
    }
}
