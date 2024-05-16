package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.DirectoryDTO;
import org.example.StudAcc.model.acc.Role;
import org.example.StudAcc.model.address.District;
import org.example.StudAcc.model.address.Region;
import org.example.StudAcc.model.organization.Discipline;
import org.example.StudAcc.model.organization.Employee;
import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.organization.Specialization;
import org.example.StudAcc.model.student.Education;
import org.example.StudAcc.model.student.OrderNumber;
import org.example.StudAcc.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class ListController {

    private final ListService service;

    // specialization
    @GetMapping("/spec/list")
    public List<Specialization> specializationList(){
        return service.getSpecializationList();
    }

    @PostMapping("/spec/new")
    public HttpStatus createSpecialization(@RequestBody DirectoryDTO dto){
        service.createSpecialization(dto.getName());
        return HttpStatus.CREATED;
    }

    @PutMapping("/spec/update")
    public HttpStatus updateSpecialization(@RequestParam("id")int id, @RequestBody DirectoryDTO dto){
        service.updateSpecialization(id, dto.getName());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/spec/delete")
    public HttpStatus deleteSpecialization(@RequestParam("id")int id){
        service.deleteSpecialization(id);
        return HttpStatus.OK;
    }

    //group
    @PostMapping("/group/new")
    public HttpStatus createGroup(@RequestBody DirectoryDTO dto) {
        service.createGroup(dto.getParentId(), dto.getName());
        return HttpStatus.CREATED;
    }

    @PutMapping("/group/update")
    public HttpStatus updateGroup(@RequestParam("id")int id, @RequestBody DirectoryDTO dto){
        service.updateGroup(id, dto.getName());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/group/delete")
    public HttpStatus deleteGroup(@RequestParam("id")int id){
        service.deleteGroup(id);
        return HttpStatus.OK;
    }

    @GetMapping("/group/list")
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

    //Order
    @GetMapping("/order/list")
    public List<OrderNumber> orderList(){
        return service.getOrderList();
    }

    @PostMapping("/order/new")
    public HttpStatus createOrder(@RequestBody DirectoryDTO dto){
        service.createOrder(dto.getName());
        return HttpStatus.CREATED;
    }

    @PutMapping("/order/update")
    public HttpStatus updateOrder(@RequestParam("orderId") int id, @RequestBody DirectoryDTO dto){
        service.updateOrder(id, dto.getName());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/order/delete")
    public HttpStatus deleteOrder(@RequestParam("orderId")int id){
        service.deleteOrder(id);
        return HttpStatus.OK;
    }

    //education
    @GetMapping("/education/list")
    public List<Education> educationList(){
        return service.getEducationList();
    }

    @PostMapping("/education/new")
    public HttpStatus createEducation(@RequestBody DirectoryDTO dto){
        service.createEducation(dto.getName());
        return HttpStatus.CREATED;
    }

    @PutMapping("/education/update")
    public HttpStatus updateEducation(@RequestBody DirectoryDTO dto, @RequestParam("id") int id){
        service.updateEducation(id, dto.getName());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/education/delete")
    public HttpStatus deleteEducation(@RequestParam("id") int id){
        service.deleteEducation(id);
        return HttpStatus.OK;
    }

    //employee
    @GetMapping("/employee/list")
    public List<Employee> employeeList() {
        return service.getAllEmployee();
    }

    @PostMapping("/employee/new")
    public HttpStatus createEmployee(@RequestBody DirectoryDTO dto) {
        service.createEmployee(new Employee(dto.getName()));
        return HttpStatus.CREATED;
    }

    @PutMapping("/employee/update")
    public HttpStatus updateEmployee(@RequestBody DirectoryDTO dto, @RequestParam("id")int id) {
        service.updateEmployee(new Employee(dto.getName()), id);
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/employee/delete")
    public HttpStatus deleteEmployee(@RequestParam("id")int id){
        service.deleteEmployee(id);
        return HttpStatus.OK;
    }

    //discipline
    @GetMapping("/discipline/list")
    public List<Discipline> disciplineList(@RequestParam("id") int specId) {
        return service.getDisciplineList(specId);
    }

    @PostMapping("/discipline/new")
    public HttpStatus createDiscipline(@RequestBody DirectoryDTO dto) {
        service.createDiscipline(service.mapToModel(dto));
        return HttpStatus.CREATED;
    }

    @PutMapping("/discipline/update")
    public HttpStatus updateDiscipline(@RequestBody DirectoryDTO dto, @RequestParam("id") int id) {
        service.updateDiscipline(id, dto.getName());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/discipline/delete")
    public HttpStatus deleteDiscipline(@RequestParam("id") int id) {
        service.deleteDiscipline(id);
        return HttpStatus.OK;
    }

    //region
    @GetMapping("/region/list")
    public List<Region> regionList() {
        return service.getRegionList();
    }

    @PostMapping("/region/new")
    public HttpStatus newOblast(@RequestBody DirectoryDTO dto) {
        service.createRegion(dto.getName());
        return HttpStatus.CREATED;
    }

    @PutMapping("/region/update")
    public HttpStatus updateOblast(@RequestParam("id")int id, @RequestBody DirectoryDTO dto){
        service.updateRegion(id, dto.getName());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/region/delete")
    public HttpStatus deleteOblast(@RequestParam("id")int id){
        service.deleteRegion(id);
        return HttpStatus.OK;
    }


    //district
    @GetMapping("/district/list")
    public List<District> districtListByOblast(@RequestParam("id")int regionId){
        return service.getDistrictList(regionId);
    }

    @PostMapping("/district/new")
    public HttpStatus newDistrict(@RequestBody DirectoryDTO dto) {
        service.createDistrict(dto.getName(), dto.getParentId());
        return HttpStatus.ACCEPTED;
    }

    @PutMapping("/district/update")
    public HttpStatus updateDistrict(@RequestParam("id")int id, @RequestBody DirectoryDTO dto){
        service.updateDistrict(dto.getName(), id);
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/district/delete")
    public HttpStatus deleteDistrict(@RequestParam("id") int id){
        service.deleteDistrict(id);
        return HttpStatus.OK;
    }

    // Roles
    @Secured("ROLE_ADMIN")
    @GetMapping("/role/list")
    public List<Role> roleList(){
        return service.getAllRoles();
    }

}
