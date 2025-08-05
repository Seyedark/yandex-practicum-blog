package ru.yandex.practicum.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

    @Value("${images.local.path}")
    String imagesLocalPath;

    @Value("${images.url.path}")
    String imagesUrlPath;


    public String saveFileOnMachine(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }
        String machineImagesLocalPath = imagesLocalPath.substring("file:///".length()) + imagesUrlPath;
        File uploadDirFile = new File(machineImagesLocalPath);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }
        String originalFilename = imageFile.getOriginalFilename();
        String nameWithoutExtension = originalFilename.substring(0, Math.min(originalFilename.lastIndexOf('.'), 5));
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);

        String imageName = nameWithoutExtension + "_" + timestamp + fileExtension;
        File destFile = new File(uploadDirFile, imageName);
        try {
            imageFile.transferTo(destFile);
            return imagesUrlPath + imageName;
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteFileOnMachine(String imagePath) {
        String machineImagesLocalPath = imagesLocalPath.substring("file:///".length()) + imagePath;
        File fileToDelete = new File(machineImagesLocalPath);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }
}