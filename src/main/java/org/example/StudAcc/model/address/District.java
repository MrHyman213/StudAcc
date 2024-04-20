package org.example.StudAcc.model.address;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_region", referencedColumnName = "id")
    private Region region;

    public District(String name, Region region) {
        this.name = name;
        this.region = region;
    }
}
