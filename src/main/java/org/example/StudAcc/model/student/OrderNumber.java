package org.example.StudAcc.model.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "order_number")
@Data
@NoArgsConstructor
public class OrderNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "orderNumber")
    @JsonIgnore
    private List<Student> studentList;

    public OrderNumber(String name, List<Student> studentList) {
        this.name = name;
        this.studentList = studentList;
    }
}
