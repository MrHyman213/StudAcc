package org.example.StudAcc.service.organization;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.EmployeeDTO;
import org.example.StudAcc.DTO.OrganizationDTO;
import org.example.StudAcc.model.organization.Discipline;
import org.example.StudAcc.model.organization.Employee;
import org.example.StudAcc.model.organization.Organization;
import org.example.StudAcc.repository.organization.OrganizationRepository;
import org.example.StudAcc.repository.organization.employee.DisciplineRepository;
import org.example.StudAcc.repository.organization.employee.EmployeeRepository;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.example.StudAcc.utils.exceptions.organization.DisciplineNotFoundException;
import org.example.StudAcc.utils.exceptions.organization.EmployeeNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository repository;
    private final EmployeeRepository employeeRepository;
    private final DisciplineRepository disciplineRepository;
    private final ModelMapper mapper;

    @Bean
    public Organization getInfo() {
        return repository.findAll().get(0);
    }

    @Transactional
    public void update(Organization organization){
        getInfo().setName(organization.getName());
        getInfo().setPhone(organization.getPhone());
        getInfo().setAddress(organization.getAddress());
    }

    public Organization mapToModel(OrganizationDTO dto){
        return mapper.map(dto, Organization.class);
    }

    //Discipline
    public List<Discipline> getDisciplineList(){
        return disciplineRepository.findAll();
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


    //Employee
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

    public Employee mapToModel(EmployeeDTO dto){
        Employee employee = mapper.map(dto, Employee.class);
        //Конвертация дисциплин
        if(dto.getDisciplineList() != null){
            if(employee.getDisciplineList() != null)
                dto.getDisciplineList().forEach(discipline -> employee.getDisciplineList().add(getDiscipline(discipline.getName())));
            else
                employee.setDisciplineList(dto.getDisciplineList().stream().
                        map(discipline -> getDiscipline(discipline.getName())).collect(Collectors.toList()));
        }
        return employee;
    }

    @Transactional
    public void updateDirector(int id) {
        getInfo().setDirector(getEmployee(id));
    }

    @Transactional
    public void updateSecretary(int id){
        getInfo().setSecretary(getEmployee(id));
    }
}
