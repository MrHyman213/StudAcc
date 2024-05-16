package org.example.StudAcc.service.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.StudAcc.DTO.student.ShortStudentDTO;
import org.example.StudAcc.DTO.student.StudentDTO;
import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.student.Student;
import org.example.StudAcc.model.student.parent.Parent;
import org.example.StudAcc.repository.student.StudentRepository;
import org.example.StudAcc.service.address.AddressService;
import org.example.StudAcc.service.organization.GroupService;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StudentService {

    private final StudentRepository repository;
    private final GroupService groupService;
    private final EducationService educationService;
    private final OrderNumberService orderNumberService;
    private final AddressService addressService;
    private final ModelMapper mapper;

    private Student getByInitialsAndBirthDate(String name, String surname, String patronymic, LocalDate birthDate){
        return repository.findByNameAndSurnameAndPatronymicAndBirthDate(name, surname, patronymic, birthDate).orElse(null);
    }

    public List<ShortStudentDTO> getAll() {
        return repository.findAll().stream().map(this::modelToMap).collect(Collectors.toList());
    }

    public List<Student> getByGroup(Group group){
        return repository.findByGroupOrderBySurname(group);
    }

    public Student getById(int id) {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new ObjectNotFoundException("Студент с идентификатором " + id + " не найден.");
        }
    }

    public List<ShortStudentDTO> getStudentsByGroup(int id) {
        return groupService.getById(id).getStudents().stream().map(this::modelToMap).sorted(Comparator.comparing(ShortStudentDTO::getSurname)).collect(Collectors.toList());
    }

    public List<Parent> getParents(int studentId) {
        return getById(studentId).getParentList();
    }

    public Boolean checkGroup(int groupId) {
        return groupService.getById(groupId).getStudents().isEmpty();
    }

    @Transactional
    public Student create(Student student) {
        return repository.save(student);
    }

    @Transactional
    public void update(int id, Student student) {
        student.setId(id);
        repository.save(student);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public void move(int studentId, int groupId) {
        Student student = getById(studentId);
        Group group = groupService.getById(groupId);
        student.getGroup().getStudents().remove(student);
        student.setGroup(group);
        group.getStudents().add(student);
    }

    @Transactional
    public void processStudents(List<org.example.StudAcc.DTO.excel.Student> list, int groupId){
        Student foundedStudent;
        Group group = groupService.getById(groupId);
        for (org.example.StudAcc.DTO.excel.Student student: list){
            foundedStudent = getByInitialsAndBirthDate(student.getName(), student.getSurname(), student.getPatronymic(), student.getBirthDate());
            Student processedStudent = mapToModel(student);
            if (foundedStudent != null)
                update(foundedStudent.getId(), processedStudent);
            else {
                processedStudent = create(processedStudent);
                processedStudent.setGroup(group);
                group.getStudents().add(processedStudent);
            }
        }
    }

    public Student mapToModel(org.example.StudAcc.DTO.excel.Student properties){
        Student student = mapper.map(properties, Student.class);
        student.setEducation(educationService.getByName(properties.getEducation()));
        student.setOrderNumber(orderNumberService.getByName(properties.getOrderNumber()));
        return student;
    }

    public Student mapToModel(StudentDTO dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Student student = mapper.map(dto, Student.class);
        if(dto.getEducationId() != 0){
            student.setEducation(educationService.getById(dto.getEducationId()));
        }
        if(dto.getGroupId() != 0)
            student.setGroup(groupService.getById(dto.getGroupId()));
        if(dto.getOrderNumberId() != 0)
            student.setOrderNumber(orderNumberService.getById(dto.getOrderNumberId()));
        if (dto.getAddressId() != 0) {
            student.setAddress(addressService.getById(dto.getAddressId()));
        }
        return student;
    }

    public ShortStudentDTO modelToMap(Student student) {
        return mapper.map(student, ShortStudentDTO.class);
    }
}
