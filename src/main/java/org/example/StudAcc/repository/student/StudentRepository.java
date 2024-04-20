package org.example.StudAcc.repository.student;

import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    List<Student> findByGroupOrderBySurname(Group group);

    Optional<Student> findByNameAndSurnameAndPatronymicAndBirthDate(String name, String surname, String patronymic, LocalDate birthDate);
}
