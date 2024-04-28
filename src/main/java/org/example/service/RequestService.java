package org.example.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.example.DTO.RequestType;
import org.example.DTO.entries.*;
import org.example.DTO.Login;
import org.example.DTO.student.Output.AddressDTO;
import org.example.DTO.student.Output.StudentDTO;
import org.example.DTO.student.Address;
import org.example.DTO.student.ShortStudent;
import org.example.DTO.student.Student;
import org.example.util.EntryContainer;
import org.example.util.RequestManager;
import org.example.util.User;
import org.example.util.exception.UnauthorizedException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class RequestService {

    private static User user = new User();
    private static HttpClient client = HttpClient.newHttpClient();
    private static HttpRequest request;
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }
    private static final String URL = "http://localhost:8000/studacc/";

    private static HttpResponse<String> request(RequestType type, String url, Object body) {
        try {
            Map<String, String> headers = Map.of("Authorization", "Bearer " + user.getToken(), "Content-Type", "application/json");
            switch (type){
                case GET -> request = RequestManager.get(URL + url, headers);
                case POST -> request = RequestManager.post(URL + url, body, headers);
                case PUT -> request = RequestManager.put(URL + url , body, headers);
                case DELETE -> request = RequestManager.delete(URL + url, headers);
            }
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean login(String login, String password) {
        try {
            request = RequestManager.post(URL + "auth", new Login(login, password), Map.of("Content-Type", "application/json"));
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                user.setToken(objectMapper.readValue(response.body(), Token.class).token);
                user.setLogin(login);
                user.setPassword(password);
                return true;
            } else if (response.statusCode() == 401)
                return false;
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static List<Specialization> getSpecList(){
        List<Specialization> specList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "student/spec/list", null);
            if(response.statusCode() == 401) throw new UnauthorizedException();
            specList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Specialization.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("specList", MappingService.convertToMap(specList));
        return specList;
    }

    public static List<String> getGroupList(String specName){
        List<Group> groupList;
        try {
            HttpResponse<String> response = request(RequestType.GET,
                    "student/group/listBySpecId?id=" + EntryContainer.getIdByName("specList", specName), null);
            if(response.statusCode() == 401) throw new UnauthorizedException();
            groupList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Group.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("groupList", MappingService.convertToMap(groupList));
        return groupList.stream().map(Group::getName).collect(Collectors.toList());
    }

    public static List<ShortStudent> getStudentListByGroupName(String groupName) {
        List<ShortStudent> students;
        try {
            HttpResponse<String> response = request(RequestType.GET, "student/byGroupId?id=" + EntryContainer.getIdByName("groupList", groupName), null);
            if(response.statusCode() == 401) throw new UnauthorizedException();
            students = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ShortStudent.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("studentList", MappingService.convertStudentToMap(students));
        return students;
    }

    public static Student getStudentByInitials(String initials) {
        Student student;
        try{
            HttpResponse<String> response = request(RequestType.GET,
                    "student/details?studentId=" + EntryContainer.getIdByName("studentList", initials), null);
            if(response.statusCode() == 401) throw new UnauthorizedException();
            student = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructType(Student.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public static List<String> getEducationList() {
        List<Education> educations;
        try {
            HttpResponse<String> response = request(RequestType.GET, "student/education/list", null);
            if(response.statusCode() == 401) throw new UnauthorizedException();
            educations = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Education.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("educationList", MappingService.convertToMap(educations));
        return educations.stream().map(Education::getName).collect(Collectors.toList());
    }

    public static List<String> getRegionList() {
        List<Region> regionList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "address/oblast/list", null);
            if(response.statusCode() == 401)
                throw new UnauthorizedException();
            regionList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Region.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("regionList", MappingService.convertToMap(regionList));
        return regionList.stream().map(Region::getName).collect(Collectors.toList());
    }


    public static List<String> getDistrictListByRegion(String region) {
        List<District> districtList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "address/oblast/districts?id=" + EntryContainer.getIdByName("regionList", region), null);
            if(response.statusCode() == 401) throw new UnauthorizedException();
            districtList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, District.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("districtList", MappingService.convertToMap(districtList));
        return districtList.stream().map(District::getName).collect(Collectors.toList());

    }

    public static List<String> getOrderList() {
        List<OrderNumber> orderList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "student/order/list", null);
            if(response.statusCode() == 401)
                throw new UnauthorizedException();
            orderList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, OrderNumber.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("orderList", MappingService.convertToMap(orderList));
        return orderList.stream().map(OrderNumber::getName).collect(Collectors.toList());
    }

    public static void createStudent(StudentDTO student) {
        request(RequestType.POST, "student/new", student);
    }

    public static void updateStudent(StudentDTO student, int studentId) {
        request(RequestType.PUT, "student/update?id=" + studentId, student);
    }

    public static void updateAddress(AddressDTO address, int addressId){
        request(RequestType.PUT, "address/update?id=" + addressId, address);
    }

    public static int createAddress(AddressDTO address) {
        HttpResponse<String> response = request(RequestType.POST, "address/new", address);
        if (response.statusCode() == 200)
            return Integer.parseInt(response.body());
        return 0;
    }

    public static List<String> getGroupList() {
        List<Group> groupList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "student/group/all", null);
            if (response.statusCode() == 401)
                throw new UnauthorizedException();
            groupList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Group.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("allGroupList", MappingService.convertToMap(groupList));
        return groupList.stream().map(Group::getName).collect(Collectors.toList());
    }

    public static void moveGroup(String out, String in) {
        HttpResponse<String> response = request(RequestType.GET, "student/group/move?out=" + out + "&in=" + in, null);
        if (response.statusCode() == 401)
            throw new UnauthorizedException();
    }

    public static void deleteStudent(int id) {
        request(RequestType.DELETE, "student?id=" + id, null);
    }
}
class Token{
    public String token;
}