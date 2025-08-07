package ru.yandex.practicum.blog.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.service.FileService;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringJUnitConfig(classes = FileService.class)
@DisplayName("Класс для проверки взаимодействия с файловым сервисом")
@TestPropertySource(locations = "classpath:test-application.properties")
public class FileServiceTest {

    @Value("${images.local.path}")
    String imagesLocalPath;

    @Value("${images.url.path}")
    String imagesUrlPath;

    @Autowired
    FileService fileService;

    @Test
    @DisplayName("Проверка создания файла на машине")
    void saveFileOnMachineTest() {
        String fileName = "testImage.jpg";
        String contentType = "image/jpeg";
        byte[] content = "some image bytes".getBytes();

        MultipartFile imageFile = new MockMultipartFile(fileName, fileName, contentType, content);
        String fileNameOnMachine = fileService.saveFileOnMachine(imageFile);
        File fileToDelete = new File(imagesLocalPath.substring("file:///".length()) + fileNameOnMachine);
        assertTrue(fileToDelete.exists());
        fileToDelete.delete();
    }

    @Test
    @DisplayName("Проверка удаления файла на машине")
    void deleteFileOnMachineTest() throws IOException {
        String machineImagesLocalPath = imagesLocalPath.substring("file:///".length()) + imagesUrlPath;
        File uploadDirFile = new File(machineImagesLocalPath);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }
        String fileName = "123.jpeg";
        File newFile = new File(uploadDirFile, fileName);
        newFile.createNewFile();
        assertTrue(newFile.exists());
        fileService.deleteFileOnMachine(imagesUrlPath + fileName);
        assertFalse(newFile.exists());
    }
}