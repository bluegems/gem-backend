package com.bluegems.server.gembackend.graphql.model;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageInput {
    String base64String;
    String filename;
}
