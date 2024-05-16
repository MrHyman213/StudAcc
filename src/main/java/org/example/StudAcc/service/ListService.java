package org.example.StudAcc.service;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.DirectoryDTO;
import org.example.StudAcc.DTO.DisciplineDTO;
import org.example.StudAcc.DTO.EmployeeDTO;
import org.example.StudAcc.model.acc.Role;
import org.example.StudAcc.model.address.District;
import org.example.StudAcc.model.address.Region;
import org.example.StudAcc.model.organization.Discipline;
import org.example.StudAcc.model.organization.Employee;
import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.organization.Specialization;
import org.example.StudAcc.model.student.Education;
import org.example.StudAcc.model.student.OrderNumber;
import org.example.StudAcc.model.student.Student;
import org.example.StudAcc.repository.organization.employee.DisciplineRepository;
import org.example.StudAcc.repository.organization.employee.EmployeeRepository;
import org.example.StudAcc.service.address.DistrictService;
import org.example.StudAcc.service.address.RegionService;
import org.example.StudAcc.service.organization.GroupService;
import org.example.StudAcc.service.organization.SpecializationService;
import org.example.StudAcc.service.security.RoleService;
import org.example.StudAcc.service.student.EducationService;
import org.example.StudAcc.service.student.OrderNumberService;
import org.example.StudAcc.utils.exceptions.organization.DisciplineNotFoundException;
import org.example.StudAcc.utils.exceptions.organization.EmployeeNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListService {

    private final GroupService groupService;
    private final SpecializationService specializationService;
    private final EducationService educationService;
    private final OrderNumberService orderNumberService;
    private final EmployeeRepository employeeRepository;
    private final DisciplineRepository disciplineRepository;
    private final RegionService oblastService;
    private final DistrictService districtService;
    private final ModelMapper mapper;
    private final RoleService roleService;

    public List<Specialization> getSpecializationList(){
        return specializationService.getAll();
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

    //group
    ///////////////////////////////////////////////////////

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

    @Transactional
    public void moveGroup(String out, String in) {
        Group inGroup = groupService.getByName(in);
        Iterator<Student> outIterator = groupService.getByName(out).getStudents().iterator();
        while (outIterator.hasNext()) {
            Student student = outIterator.next();
            inGroup.getStudents().add(student);
            student.setGroup(inGroup);
            outIterator.remove();
        }
    }

    public List<Group> getAllGroups() {
        return groupService.getAll();
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

    //education
    //////////////////////////////////////////////////////////////////
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

    //employee
    ////////////////////////////////////////////////////////////////////////////////
    @Transactional
    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }
    public Employee getEmployee(int id){
        try {
            return employeeRepository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new EmployeeNotFoundException("Работник с идентификатором " + id + " не найден.");
        }
    }

    @Transactional
    public void createEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Transactional
    public void updateEmployee(Employee employee, int id){
        employee.setId(id);
        employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(int id){
        employeeRepository.deleteById(id);
    }

    //discipline
    ////////////////////////////////////////////////////////////////////////////////
    public List<Discipline> getDisciplineList(int id) {
        return specializationService.getById(id).getDisciplineList();
    }

    public Discipline getDiscipline(String name){
        try {
            return disciplineRepository.findByName(name).get();
        } catch (NoSuchElementException e){
            throw new DisciplineNotFoundException("Дисциплина \"" + name + "\" не найдена.");
        }
    }

    public Discipline getDiscipline(int id){
        try {
            return disciplineRepository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new DisciplineNotFoundException("Дисциплина с идентификатором " + id + " не найдена.");
        }
    }
    public Discipline mapToModel(DirectoryDTO dto){
        Discipline discipline = mapper.map(dto, Discipline.class);
        discipline.setSpecialization(specializationService.getById(dto.getParentId()));
        return discipline;
    }

    @Transactional
    public void createDiscipline(Discipline discipline){
        disciplineRepository.save(discipline);
    }

    public void updateDiscipline(int id, String name) {
        getDiscipline(id).setName(name);
    }

    @Transactional
    public void deleteDiscipline(int id) {
        disciplineRepository.deleteById(id);
    }

    //region
    ////////////////////////////////////////////////////////////////////
    public List<Region> getRegionList(){
        return oblastService.getAll();
    }

    @Transactional
    public void createRegion(String name) {
        oblastService.create(name);
    }

    @Transactional
    public void updateRegion(int id, String name){
        oblastService.getById(id).setName(name);
    }

    @Transactional
    public void deleteRegion(int id){
        oblastService.delete(id);
    }

    public Region getRegionById(int id){
        return oblastService.getById(id);
    }

    // district
    ////////////////////////////////////////////////////////////////////
    public District getDistrictById(int id){
        return districtService.getById(id);
    }

    @Transactional
    public void createDistrict(String name, int parentId) {
        districtService.create(name, getRegionById(parentId));
    }

    @Transactional
    public void updateDistrict(String name, int id){
        getDistrictById(id).setName(name);
    }

    @Transactional
    public void deleteDistrict(int id){
        District district = getDistrictById(id);
        district.getSubEntry().getDistrictList().remove(district);
        districtService.delete(id);
    }

    public List<District> getDistrictList(int regionId) {
        return getRegionById(regionId).getDistrictList();
    }

    public List<Role> getAllRoles() {
        return roleService.getAll();
    }
}
