package org.example.StudAcc.service.student;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.student.ShortStudentDTO;
import org.example.StudAcc.DTO.student.StudentDTO;
import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.organization.Specialization;
import org.example.StudAcc.model.student.Education;
import org.example.StudAcc.model.student.OrderNumber;
import org.example.StudAcc.model.student.Student;
import org.example.StudAcc.model.student.parent.Parent;
import org.example.StudAcc.repository.student.StudentRepository;
import org.example.StudAcc.service.organization.GroupService;
import org.example.StudAcc.service.organization.SpecializationService;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository repository;
    private final GroupService groupService;
    private final SpecializationService specializationService;
    private final EducationService educationService;
    private final OrderNumberService orderNumberService;
    private final ModelMapper mapper;

    public List<Specialization> getSpecializationList(){
        return specializationService.getAll();
    }

    private Student getByInitialsAndBirthDate(String name, String surname, String patronymic, LocalDate birthDate){
        return repository.findByNameAndSurnameAndPatronymicAndBirthDate(name, surname, patronymic, birthDate).orElse(null);
    }

    @Transactional
    public void createSpecialization(String name){
        specializationService.create(name);
    }

    @Transactional
    public void updateSpecialization(int id, String name){
        specializationService.getById(id).setName(name);
    }

    @Transactional
    public void deleteSpecialization(int id){
        specializationService.delete(id);
    }

    public List<Group> getGroupBySpecId(int specId){
        return specializationService.getById(specId).getGroupList();
    }

    @Transactional
    public void createGroup(int specId, String name){
        groupService.create(name, specializationService.getById(specId));
    }

    @Transactional
    public void updateGroup(int id, String name){
        groupService.getById(id).setName(name);
    }

    @Transactional
    public void deleteGroup(int id){
        groupService.delete(id);
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
        return groupService.getById(id).getStudents().stream().map(this::modelToMap).collect(Collectors.toList());
    }

    public List<Parent> getParents(int studentId) {
        return getById(studentId).getParentList();
    }

    @Transactional
    public void create(Student student) {
        repository.save(student);
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

    //orders
    ////////////////////////////////////////////////////////////////////

    public List<OrderNumber> getOrderList() {
        return orderNumberService.getAll();
    }

    @Transactional
    public void createOrder(String name){
        orderNumberService.create(name);
    }

    @Transactional
    public void updateOrder(int id, String name){
        orderNumberService.getById(id).setName(name);
    }

    @Transactional
    public void deleteOrder(int id){
        orderNumberService.delete(id);
    }

    public List<Education> getEducationList(){
        return educationService.getAll();
    }

    @Transactional
    public void createEducation(String name){
        educationService.create(name);
    }

    @Transactional
    public void updateEducation(int id, String name){
        educationService.getById(id).setName(name);
    }

    @Transactional
    public void deleteEducation(int id){
        educationService.delete(id);
    }

    @Transactional
    public void processStudents(List<org.example.StudAcc.DTO.excel.Student> list){
        Student foundedStudent;
        for (org.example.StudAcc.DTO.excel.Student student: list){
            foundedStudent = getByInitialsAndBirthDate(student.getName(), student.getSurname(), student.getPatronymic(), student.getBirthDate());
            if (foundedStudent != null)
                update(foundedStudent.getId(), mapToModel(student, foundedStudent));
            else {
                create(mapToModel(student, new Student()));
            }
        }
    }

    public Student mapToModel(org.example.StudAcc.DTO.excel.Student properties, Student student){
        student = mapper.map(properties, Student.class);
        student.setGroup(groupService.getByName(properties.getGroup()));
        student.setEducation(educationService.getByName(properties.getEducation()));
        student.setOrderNumber(orderNumberService.getByName(properties.getOrderNumber()));
        return student;
    }

    public Student mapToModel(StudentDTO dto){
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Student student = mapper.map(dto, Student.class);
        if(dto.getEducationId() != 0)
            student.setEducation(educationService.getById(dto.getEducationId()));
        if(dto.getGroupId() != 0)
            student.setGroup(groupService.getById(dto.getGroupId()));
        if(dto.getOrderNumberId() != 0)
            student.setOrderNumber(orderNumberService.getById(dto.getOrderNumberId()));
        return student;
    }

    public ShortStudentDTO modelToMap(Student student) {
        return mapper.map(student, ShortStudentDTO.class);
    }
}
