package org.example.StudAcc.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.StudAcc.model.student.Student;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "`group`")
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_specialization", referencedColumnName = "id")
    @JsonIgnore
    private Specialization specialization;

    @Lazy
    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private List<Student> students;

    public Group(String name, Specialization specialization) {
        this.name = name;
        this.specialization = specialization;
    }
}
