package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.GroupDTO;
import org.example.StudAcc.DTO.student.ShortStudentDTO;
import org.example.StudAcc.DTO.student.StudentDTO;
import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.organization.Specialization;
import org.example.StudAcc.model.student.Education;
import org.example.StudAcc.model.student.OrderNumber;
import org.example.StudAcc.model.student.Student;
import org.example.StudAcc.model.student.parent.Parent;
import org.example.StudAcc.service.student.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    // specialization
    ////////////////////////////////////////////////////////////////////
    @GetMapping("/spec/list")
    public List<Specialization> specializationList(){
        return service.getSpecializationList();
    }

    @PostMapping("/spec/new")
    public HttpStatus createSpecialization(@RequestParam String name){
        service.createSpecialization(name);
        return HttpStatus.CREATED;
    }

    @PutMapping("/spec/update")
    public HttpStatus updateSpecialization(@RequestParam("id")int id, @RequestParam String name){
        service.updateSpecialization(id, name);
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/spec/delete")
    public HttpStatus deleteSpecialization(@RequestParam("id")int id){
        service.deleteSpecialization(id);
        return HttpStatus.OK;
    }

    //groups
    ////////////////////////////////////////////////////////////////////
    @GetMapping("/group/listBySpecId")
    public List<Group> groupList(@RequestParam("id")int id){
        return service.getGroupBySpecId(id);
    }

    @GetMapping("/group/all")
    public List<Group> allGroups(){
        return service.getAllGroups();
    }

    @GetMapping("/group/move")
    public HttpStatus moveGroup(@RequestParam("out") String out, @RequestParam("in") String in){
        service.moveGroup(out, in);
        return HttpStatus.OK;
    }

    @PostMapping("/group/new")
    public HttpStatus createGroup(@RequestBody GroupDTO dto) {
        service.createGroup(dto.getSpecId(), dto.getName());
        return HttpStatus.CREATED;
    }

    @PutMapping("/group/update")
    public HttpStatus updateGroup(@RequestParam("id")int id, @RequestBody GroupDTO dto){
        service.updateGroup(id, dto.getName());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/group/delete")
    public HttpStatus deleteGroup(@RequestParam("id")int id){
        service.deleteGroup(id);
        return HttpStatus.OK;
    }

    //students
    ////////////////////////////////////////////////////////////////////
    // Получение списка всех студентов
    @GetMapping("/list")
    public List<ShortStudentDTO> studentList(){
        return service.getAll();
    }

    // Получение списка студентов конкретной группы по имени (41т, 31сх, 21к и т.д)
    @GetMapping("/byGroupId")
    public List<ShortStudentDTO> byGroup(@RequestParam("id") int id) {
        return service.getStudentsByGroup(id);
    }

    // Получение студента по идентификатору
    @GetMapping("/details")
    public Student details(@RequestParam("studentId")int id){
        return service.getById(id);
    }

    // Получение списка родителей по идентификатору студента
    @GetMapping("/{studentId}/parents")
    public List<Parent> parentsOfStudent(@PathVariable("id")int id){
        return service.getParents(id);
    }

    @PostMapping("/new")
    public HttpStatus createStudent(@RequestBody StudentDTO dto){
        service.create(service.mapToModel(dto));
        return HttpStatus.CREATED;
    }

    @PutMapping("/update")
    public HttpStatus updateStudent(@RequestParam("id") int id, @RequestBody StudentDTO dto) {
        service.update(id, service.mapToModel(dto));
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping
    public HttpStatus deleteStudent(@RequestParam("id")int id){
        service.delete(id);
        return HttpStatus.ACCEPTED;
    }

    //Orders
    @GetMapping("/order/list")
    public List<OrderNumber> orderList(){
        return service.getOrderList();
    }

    @PostMapping("/order/new")
    public HttpStatus createOrder(@RequestParam("orderName") String orderName){
        service.createOrder(orderName);
        return HttpStatus.CREATED;
    }

    @PutMapping("/order/update")
    public HttpStatus updateOrder(@RequestParam("orderId") int id, @RequestParam("name") String name){
        service.updateOrder(id, name);
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/order/delete")
    public HttpStatus deleteOrder(@RequestParam("orderId")int id){
        service.deleteOrder(id);
        return HttpStatus.OK;
    }

    //education
    ////////////////////////////////////////////////////////////////////
    @GetMapping("/education/list")
    public List<Education> educationList(){
        return service.getEducationList();
    }

    @PostMapping("/education/new")
    public HttpStatus createEducation(@RequestParam("educationName")String name){
        service.createEducation(name);
        return HttpStatus.CREATED;
    }

    @PutMapping("/education/update")
    public HttpStatus updateEducation(@RequestParam("id") int id, @RequestParam("educationName") String name){
        service.updateEducation(id, name);
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/education/delete")
    public HttpStatus deleteEducation(@RequestParam("id") int id){
        service.deleteEducation(id);
        return HttpStatus.OK;
    }
}

