package org.vcriate.filemanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vcriate.filemanager.entities.File;
import org.vcriate.filemanager.entities.User;
import org.vcriate.filemanager.repositories.FileRepository;
import org.vcriate.filemanager.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public File getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public File uploadFile(MultipartFile file, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            String filename = saveFile(file);
            File newFile = new File();
            newFile.setName(filename);
            newFile.setDateUploaded(LocalDateTime.now());
            newFile.setVersion(1);
            newFile.setUser(user);
            return fileRepository.save(newFile);
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
