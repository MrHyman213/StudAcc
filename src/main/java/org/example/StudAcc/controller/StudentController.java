package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.student.ShortStudentDTO;
import org.example.StudAcc.DTO.student.StudentDTO;
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

    @GetMapping("/isEmpty")
    public Boolean checkGroup(@RequestParam("id")int groupId){
        return service.checkGroup(groupId);
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

    @DeleteMapping("/delete")
    public HttpStatus deleteStudent(@RequestParam("id")int id){
        service.delete(id);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/move")
    public HttpStatus moveStudent(@RequestParam("idStudent") int studentId, @RequestParam("idGroup") int groupId){
        service.move(studentId, groupId);
        return HttpStatus.ACCEPTED;
    }

    @GetMapping("/clearGroup")
    public HttpStatus clearGroup(@RequestParam("id")int id){
        service.clearGroup(id);
        return HttpStatus.OK;
    }
}

