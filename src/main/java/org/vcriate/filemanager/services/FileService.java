package org.vcriate.filemanager.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vcriate.filemanager.entities.File;
import org.vcriate.filemanager.entities.User;
import org.vcriate.filemanager.repositories.FileRepository;
import org.vcriate.filemanager.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class FileService {
    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public List<File> getAllFiles(User user) {
        return fileRepository.findByUserId(user.getId());
    }

    public File getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public File uploadFile(MultipartFile file, Long userId) {
        log.atInfo().log("Uploading file for user with ID: {}", userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            String filename = saveFile(file);
            File newFile = new File();
            newFile.setName(filename);
            newFile.setDateUploaded(LocalDateTime.now());
            newFile.setVersion(1);
            newFile.setUser(user);
            log.atInfo().log("Saving file: {}", newFile);
            return fileRepository.save(newFile);
        } else {
            log.atError().log("User not found with ID: {}", userId);
        }
        return null;
    }

    public File updateFile(Long id, File fileDetails) {
        File file = fileRepository.findById(id).orElse(null);
        if (file != null) {
            file.setName(fileDetails.getName());
            file.setDateUploaded(fileDetails.getDateUploaded());
            file.setVersion(fileDetails.getVersion());
            file.setUser(fileDetails.getUser());
            return fileRepository.save(file);
        }
        return null;
    }

    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

    private String saveFile(MultipartFile file) {
        // Implement file saving logic here
        // Return the filename
        return file.getOriginalFilename();
    }

    public List<File> getFilesByUserId(long userId) {
        return fileRepository.findByUserId(userId);
    }
}
