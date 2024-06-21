package org.vcriate.filemanager.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vcriate.filemanager.entities.File;
import org.vcriate.filemanager.entities.User;
import org.vcriate.filemanager.services.FileService;

import java.util.List;

@RestController
@RequestMapping("/fm")
public class FileController {
    @Autowired
    private FileService fileService;

}
