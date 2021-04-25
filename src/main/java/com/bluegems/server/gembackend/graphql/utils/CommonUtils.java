package com.bluegems.server.gembackend.graphql.utils;

import com.bluegems.server.gembackend.entity.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.ZonedDateTime;

public class CommonUtils {

    @Autowired
    private static Clock clock;

    private static final String TAG_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String createTag(Integer tagLength) {
        StringBuilder tagBuilder = new StringBuilder();
        for (int i = 0; i < tagLength; i++) {
            int index = (int)(TAG_CHARACTERS.length()*Math.random());
            tagBuilder.append(TAG_CHARACTERS.charAt(index));
        }
        return tagBuilder.toString();
    }

//    public static <T extends AuditEntity> addAuditTimestamps(<T extends AuditEntity> entity) {
//        entity.setModified(ZonedDateTime.now(clock));
//        if (entity.getCreated() == null) {
//            entity.setCreated(ZonedDateTime.now(clock));
//        }
//        return entity;
//    }
}
