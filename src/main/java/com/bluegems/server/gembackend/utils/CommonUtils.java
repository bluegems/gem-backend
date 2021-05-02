package com.bluegems.server.gembackend.utils;

public class CommonUtils {

    private static final String TAG_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String createTag(Integer tagLength) {
        StringBuilder tagBuilder = new StringBuilder();
        for (int i = 0; i < tagLength; i++) {
            int index = (int) (TAG_CHARACTERS.length() * Math.random());
            tagBuilder.append(TAG_CHARACTERS.charAt(index));
        }
        return tagBuilder.toString();
    }
}
