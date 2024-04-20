package org.example.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
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
    private static final String url = "http://localhost:8000/studacc/";

    public static boolean login(String login, String password) {
        try {
            request = RequestManager.post(url + "auth", new Login(login, password), Map.of("Content-Type", "application/json"));
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
        request = RequestManager.get(url + "student/spec/list", Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401) throw new UnauthorizedException();
            specList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Specialization.class));
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("specList", EntryContainer.convertToMap(specList));
        return specList;
    }

    public static List<String> getGroupList(String specName){
        List<Group> groupList;
        request = RequestManager.get(url + "student/group/listBySpecId?id=" + EntryContainer.getIdByName("specList", specName), Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401) throw new UnauthorizedException();
            groupList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Group.class));
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("groupList", EntryContainer.convertToMap(groupList));
        return groupList.stream().map(Group::getName).collect(Collectors.toList());
    }

    public static List<ShortStudent> getStudentListByGroupName(String groupName) {
        List<ShortStudent> students;
        request = RequestManager.get(url + "student/byGroupId?id=" + EntryContainer.getIdByName("groupList", groupName),
                Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401) throw new UnauthorizedException();
            students = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ShortStudent.class));
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("studentList", EntryContainer.convertStudentToMap(students));
        return students;
    }

    public static Student getStudentByInitials(String initials) {
        Student student;
        request = RequestManager.get(url + "student/details?studentId=" + EntryContainer.getIdByName("studentList", initials),
                Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try{
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401) throw new UnauthorizedException();
            student = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructType(Student.class));
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public static List<String> getEducationList() {
        List<Education> educations;
        request = RequestManager.get(url + "student/education/list",
                Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401) throw new UnauthorizedException();
            educations = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Education.class));
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("educationList", EntryContainer.convertToMap(educations));
        return educations.stream().map(Education::getName).collect(Collectors.toList());
    }

    public static List<String> getRegionList() {
        List<Region> regionList;
        request = RequestManager.get(url + "address/oblast/list",
                Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401)
                throw new UnauthorizedException();
            regionList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Region.class));
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("regionList", EntryContainer.convertToMap(regionList));
        return regionList.stream().map(Region::getName).collect(Collectors.toList());
    }


    public static List<String> getDistrictListByRegion(String region) {
        List<District> districtList;
        request = RequestManager.get(url + "address/oblast/districts?id=" + EntryContainer.getIdByName("regionList", region),
                Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401) throw new UnauthorizedException();
            districtList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, District.class));
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("districtList", EntryContainer.convertToMap(districtList));
        return districtList.stream().map(District::getName).collect(Collectors.toList());

    }

    public static List<String> getOrderList() {
        List<OrderNumber> orderList;
        request = RequestManager.get(url + "student/order/list",
                Map.of("Authorization", "Bearer " + user.getToken()));
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            HttpResponse<String> response = futureResponse.get();
            if(response.statusCode() == 401)
                throw new UnauthorizedException();
            orderList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, OrderNumber.class));
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("orderList", EntryContainer.convertToMap(orderList));
        return orderList.stream().map(OrderNumber::getName).collect(Collectors.toList());
    }

    public static boolean updateStudent(StudentDTO student, int studentId) {
        try {
            request = RequestManager.put(url + "student/update?id=" + studentId, student,
                    Map.of("Authorization", "Bearer " + user.getToken(), "Content-Type", "application/json"));
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
                return true;
            else if (response.statusCode() == 401)
                throw new UnauthorizedException();
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    public static void updateAddress(AddressDTO address, int addressId){
        try {
            request = RequestManager.put(url + "address/update?id=" + addressId, address,
                    Map.of("Authorization", "Bearer " + user.getToken(), "Content-Type", "application/json"));
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createAddress(Address address){

    }

}
class Token{
    public String token;
}