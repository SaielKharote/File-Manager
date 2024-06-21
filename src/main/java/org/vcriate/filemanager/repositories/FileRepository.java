package org.vcriate.filemanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vcriate.filemanager.entities.File;
import org.vcriate.filemanager.entities.User;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByName(String name);

    List<File> findByUser(User user);

    List<File> findByUserId(long userId);
}

