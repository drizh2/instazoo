package com.drizh2.instazoo.controllers;

import com.drizh2.instazoo.entities.ImageModel;
import com.drizh2.instazoo.payload.response.MessageResponse;
import com.drizh2.instazoo.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToProfile(@RequestParam("file")MultipartFile image,
                                                                Principal principal) throws IOException {
        imageUploadService.uploadImageToProfile(image, principal);
        return new ResponseEntity<>(new MessageResponse("Image has been added!"), HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable String postId,
                                                             @RequestParam("file") MultipartFile image,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToPost(image, principal, Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Image has been added"), HttpStatus.OK);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageForProfile(Principal principal) {
        ImageModel profileImage = imageUploadService.getImageToProfile(principal);
        return new ResponseEntity<>(profileImage, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModel> getImageToPost(@PathVariable String postId) {
        ImageModel postImage = imageUploadService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }
}
