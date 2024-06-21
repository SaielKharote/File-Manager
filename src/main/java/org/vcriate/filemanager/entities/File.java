package org.vcriate.filemanager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private LocalDateTime dateUploaded;
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
