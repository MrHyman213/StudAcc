package org.example.StudAcc.model.download;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String path;
    private boolean docType = false;
    private boolean onClick = false;

    public Path(String name, String path, boolean docType, boolean onClick) {
        this.name = name;
        this.path = path;
        this.docType = docType;
        this.onClick = onClick;
    }
}
