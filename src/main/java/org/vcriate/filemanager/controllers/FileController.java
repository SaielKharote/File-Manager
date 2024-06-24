package org.vcriate.filemanager.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vcriate.filemanager.config.CustomUserDetails;
import org.vcriate.filemanager.entities.File;
import org.vcriate.filemanager.entities.User;
import org.vcriate.filemanager.services.FileService;
import org.vcriate.filemanager.services.UserService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fm/files")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<File>> getAllFiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(fileService.getAllFiles(user));
    }

    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        File uploadedFile = fileService.uploadFile(file, userId);
        if (uploadedFile != null) {
            log.atInfo().log("File uploaded successfully");
            return ResponseEntity.ok(uploadedFile);
        } else {
            log.atError().log("Error uploading file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<File> downloadFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

}
