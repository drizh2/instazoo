package com.drizh2.instazoo.services;

import com.drizh2.instazoo.entities.ImageModel;
import com.drizh2.instazoo.entities.Post;
import com.drizh2.instazoo.entities.Profile;
import com.drizh2.instazoo.exceptions.ImageNotFoundException;
import com.drizh2.instazoo.repositories.ImageRepository;
import com.drizh2.instazoo.repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {

    public static final Logger LOG = LoggerFactory.getLogger(ImageUploadService.class);


    private final ImageRepository imageRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public ImageUploadService(ImageRepository imageRepository, ProfileRepository profileRepository) {
        this.imageRepository = imageRepository;
        this.profileRepository = profileRepository;
    }

    public ImageModel uploadImageToProfile(MultipartFile file, Principal principal) throws IOException {
        Profile profile = getProfileByPrincipal(principal);
        LOG.info("Uploading image profile to User {}", profile.getEmail());

        ImageModel profileImage = imageRepository.findImageModelByProfileId(profile.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(profileImage)) {
            imageRepository.delete(profileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setProfileId(profile.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }

    public ImageModel uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        Profile profile = getProfileByPrincipal(principal);
        Post post = profile.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());

        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(post.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        LOG.info("Uploading image to Post: {}", post.getId());

        return imageRepository.save(imageModel);
    }

    public ImageModel getImageToProfile(Principal principal) {
        Profile profile = getProfileByPrincipal(principal);

        ImageModel imageModel = imageRepository.findImageModelByProfileId(profile.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    public ImageModel getImageToPost(Long postId) {
        ImageModel imageModel = imageRepository.findImageModelByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: " + postId));

        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    private Profile getProfileByPrincipal(Principal principal) {
        String username = principal.getName();
        return profileRepository.findProfileByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }
}
