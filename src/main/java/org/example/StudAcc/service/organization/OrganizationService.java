package org.example.StudAcc.service.organization;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.DisciplineDTO;
import org.example.StudAcc.DTO.EmployeeDTO;
import org.example.StudAcc.DTO.OrganizationDTO;
import org.example.StudAcc.model.organization.Discipline;
import org.example.StudAcc.model.organization.Employee;
import org.example.StudAcc.model.organization.Organization;
import org.example.StudAcc.model.organization.Specialization;
import org.example.StudAcc.repository.organization.OrganizationRepository;
import org.example.StudAcc.repository.organization.employee.DisciplineRepository;
import org.example.StudAcc.repository.organization.employee.EmployeeRepository;
import org.example.StudAcc.service.ListService;
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
    private final ListService listService;
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

    public void updateDirector(int id) {
        getInfo().setDirector(listService.getEmployee(id));
    }
    @Transactional
    public void updateSecretary(int id){
        getInfo().setSecretary(listService.getEmployee(id));
    }


}
