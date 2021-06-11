package com.bluegems.server.gembackend.service;

import com.bluegems.server.gembackend.exception.graphql.GemGraphQLErrorExtensions;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.ImageInput;
import com.github.kskelm.baringo.BaringoClient;
import com.github.kskelm.baringo.model.Image;
import com.github.kskelm.baringo.util.BaringoApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImgurService {

    @Value("${imgur.album-delete-hash}")
    private String IMGUR_ALBUM_DELETE_HASH;

    @Autowired
    private BaringoClient baringoClient;

    public String deleteAndUploadImage(String existingImageId, ImageInput newImage) {
        String newImageId;
        if (newImage==null)
            newImageId = null;
        else
            newImageId = uploadImage(newImage.getBase64String(), newImage.getFilename()).getId();
        if (existingImageId != null)
            deleteImageIfExists(existingImageId);
        return newImageId;
    }

    public Image uploadImage(String base64Image, String filename) {
        try {
            return baringoClient.imageService().uploadBase64Image(
                    base64Image,
                    filename,
                    IMGUR_ALBUM_DELETE_HASH,
                    filename.substring(0, filename.lastIndexOf(".")),
                    ""
            );
        } catch (BaringoApiException exception) {
            log.error("Failed to upload image to imgur", exception);
            throw new ThrowableGemGraphQLException("Failed to upload image", GemGraphQLErrorExtensions.builder().invalidField("image").build());
        }
    }

    public void deleteImage(String imageId) {
        try {
            baringoClient.imageService().deleteImage(imageId);
        } catch (BaringoApiException exception) {
            log.error("Failed to delete image on imgur", exception);
            throw new ThrowableGemGraphQLException("Failed to delete older image");
        }
    }

    public void deleteImageIfExists(String imageId) {
        try {
            baringoClient.imageService().deleteImage(imageId);
        } catch (BaringoApiException exception) {
            // NO OP
        }
    }


}
