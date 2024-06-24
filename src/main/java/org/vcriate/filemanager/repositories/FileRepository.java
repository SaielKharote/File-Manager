package org.vcriate.filemanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vcriate.filemanager.entities.File;
import org.vcriate.filemanager.entities.User;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByUserId(long userId);
    List<File> findByUser(User user);
}

