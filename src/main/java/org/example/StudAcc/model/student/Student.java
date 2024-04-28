package org.example.StudAcc.model.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.example.StudAcc.model.address.Address;
import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.student.parent.Parent;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String caseNumber;

    @ManyToOne
    @JoinColumn(name = "id_group", referencedColumnName = "id")
    private Group group;

    @ManyToMany(mappedBy = "studentList")
    @JsonIgnore
    private List<Parent> parentList;

    @ManyToOne
    @JoinColumn(name = "id_education", referencedColumnName = "id")
    private Education education;

    @ManyToOne
    @JoinColumn(name = "id_order_number", referencedColumnName = "id")
    private OrderNumber orderNumber;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "id_address", referencedColumnName = "id")
    private Address address;

    private Integer graduationYear;
    private boolean freeVisit;
    private boolean akadem;
    private String phone;
    private Boolean sex;
    private String snils;

    public String getInitials() {
        return surname + " " + name + " " + patronymic;
    }

    public String getShortName(){
        return surname + " " + name.charAt(0) + ". " + patronymic.charAt(0) + ".";
    }

    public OrderNumber getOrderNumber(){
        return orderNumber;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }
}
