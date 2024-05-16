package org.example.service;

import org.example.model.Entry;
import org.example.model.student.Address;
import org.example.DTO.student.AddressDTO;
import org.example.DTO.student.StudentDTO;
import org.example.model.student.ShortStudent;
import org.example.model.student.Student;
import org.example.util.EntryContainer;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingService {

    private static ModelMapper modelMapper = EntryContainer.getModelMapper();

    public static Map<String, Integer> convertToMap(List<? extends Entry> list){
        Map<String, Integer> map = new HashMap<>();
        for (Entry entry: list)
            map.put(entry.getName(), entry.getId());
        return map;
    }

    public static Map<String, Integer> convertStudentToMap(List<ShortStudent> list){
        Map<String, Integer> map = new HashMap<>();
        for (ShortStudent student: list)
            map.put(student.getInitials(), student.getId());
        return map;
    }

    public static StudentDTO studentToDTO(Student student){
        StudentDTO dto = modelMapper.map(student, StudentDTO.class);
        dto.setGroupId(student.getGroup().getId());
        if(student.getAddress() != null)
            dto.setAddressId(student.getAddress().getId());
        else
            dto.setAddressId(0);
        dto.setOrderNumberId(student.getOrderNumber().getId());
        dto.setEducationId(student.getEducation().getId());
        return dto;
    }

    public static AddressDTO addressToDTO(Address address){
        AddressDTO dto = modelMapper.map(address, AddressDTO.class);
        dto.setIdDistrict(address.getDistrict().getId());
        return dto;
    }
}
