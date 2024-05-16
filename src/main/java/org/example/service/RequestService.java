package org.example.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import javafx.application.Platform;
import kong.unirest.Unirest;
import org.example.DTO.EntryDTO;
import org.example.DTO.Login;
import org.example.DTO.ReportDTO;
import org.example.DTO.TemplatesDTO;
import org.example.DTO.student.AddressDTO;
import org.example.DTO.student.StudentDTO;
import org.example.model.Entry;
import org.example.model.RequestType;
import org.example.model.User;
import org.example.model.student.ShortStudent;
import org.example.model.student.Student;
import org.example.util.EntryContainer;
import org.example.util.FileManager;
import org.example.util.RequestManager;
import org.example.util.exception.UnauthorizedException;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class RequestService {
    private static final String IP_HOST = "localhost:8000";

    private static final User user = new User();
    private static final HttpClient client = HttpClient.newHttpClient();
    private static HttpRequest request;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }
    private static final String URL = "http://" + IP_HOST + "/studacc/";

    private static HttpResponse<String> request(RequestType type, String url, Object body) {
        try {
            Map<String, String> headers = Map.of("Authorization", "Bearer " + user.getToken(), "Content-Type", "application/json", "Accept-Encoding", "UTF-8");
            switch (type) {
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
                user.setUsername(login);
                user.setPassword(password);
                return true;
            } else if (response.statusCode() == 401)
                return false;
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean isAdmin() {
        List<Entry> roleList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "info", null);
            roleList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Entry.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return roleList.stream().map(Entry::getName).toList().contains("ROLE_ADMIN");
    }

    public static List<Entry> getGroupList() {
        List<Entry> groupList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "list/group/all", null);
            if (response.statusCode() == 401)
                throw new UnauthorizedException();
            groupList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Entry.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("allGroupList", MappingService.convertToMap(groupList));
        return groupList;
    }

    public static void moveGroup(String out, String in) {
        HttpResponse<String> response = request(RequestType.GET, "list/group/move?out=" + out + "&in=" + in, null);
        if (response.statusCode() == 401)
            throw new UnauthorizedException();
    }
    //student
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static List<ShortStudent> getStudentListByGroupId(int groupId) {
        List<ShortStudent> students;
        try {
            HttpResponse<String> response = request(RequestType.GET, "student/byGroupId?id=" + groupId, null);
            if(response.statusCode() == 401) throw new UnauthorizedException();
            students = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ShortStudent.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EntryContainer.put("studentList", MappingService.convertStudentToMap(students));
        return students;
    }

    public static boolean checkGroup(int groupId) {
        return request(RequestType.GET, "student/isEmpty?id=" + groupId, null).body().equals("true");
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

    public static void createStudent(StudentDTO student) {
        request(RequestType.POST, "student/new", student);
    }

    public static void updateStudent(StudentDTO student, int studentId) {
        request(RequestType.PUT, "student/update?id=" + studentId, student);
    }

    public static void deleteStudent(int id) {
        request(RequestType.DELETE, "student/delete?id=" + id, null);
    }

    public static void moveStudent(int studentId, int groupId){
        request(RequestType.POST, "student/move?idStudent=" + studentId + "&idGroup=" + groupId, null);
    }
    //address
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void updateAddress(AddressDTO address, int addressId) {
        request(RequestType.PUT, "address/update?id=" + addressId, address);
    }

    public static int createAddress(AddressDTO address) {
        HttpResponse<String> response = request(RequestType.POST, "address/new", address);
        if (response.statusCode() == 200)
            return Integer.parseInt(response.body());
        return 0;
    }

    //list
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static List<Entry> getList(String selectedList, int idMainItem, boolean isSub) {
        List<Entry> entryList;
        if(isSub) {
            entryList = getList("list/" + selectedList + "/list?id=" + idMainItem);
            EntryContainer.put("subItemList", MappingService.convertToMap(entryList));
        } else {
            entryList = getList("list/" + selectedList.toLowerCase() + "/list");
            EntryContainer.put("itemList", MappingService.convertToMap(entryList));
        }
        return entryList;
    }

    private static List<Entry> getList(String request){
        try {
            HttpResponse<String> response = request(RequestType.GET, request, null);
            if(response.statusCode() == 401)
                throw new UnauthorizedException();
            return objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Entry.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteList(String selectedList, int id) {
        request(RequestType.DELETE, "list/" + selectedList + "/delete?id=" + id, null);
    }

    public static void addList(EntryDTO entry, String selectedList) {
        request(RequestType.POST, "list/" + selectedList + "/new", entry);
    }

    public static void updateList(EntryDTO entry, String selectedList, int id){
        request(RequestType.PUT, "list/" + selectedList + "/update?id=" + id, entry);
    }

    //report
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void generateFile(int groupId, TemplatesDTO template, ReportDTO report) {
        Map<String, String> headers = Map.of("Authorization", "Bearer " + user.getToken(), "Content-Type", "application/json", "Accept-Encoding", "UTF-8");
        HttpRequest req = RequestManager.post(URL + "report/download?id=" + groupId + "&idTemplate=" + template.getId(), report, headers);
        String path = (System.getProperty("user.home") + "/Downloads/StudAcc/" + LocalDate.now() + ".docx");
        Platform.runLater(() -> {
            try {
                FileManager.createFile(client.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray()).get().body(), path);
                ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "start winword " + path.replace("/", "\\"));
                try {
                    builder.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<TemplatesDTO> getReportList() {
        List<TemplatesDTO> templateList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "report/list", null);
            if (response.statusCode() == 401)
                throw new UnauthorizedException();
            templateList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TemplatesDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return templateList;
    }

    public static void importStudents(File selectedFile, int groupId) {
        Unirest.post(URL + "report/csv/upload?id=" + groupId)
                .header("Accept-Encoding", "multipart/form-data")
                .header("Authorization", "Bearer " + user.getToken())
                .field("file", selectedFile).asString();
    }

    //role
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static List<User> getUserList() {
        List<User> userList;
        try {
            HttpResponse<String> response = request(RequestType.GET, "admin/list", null);
            if (response.statusCode() == 401)
                throw new UnauthorizedException();
            userList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }
}
class Token{
    public String token;
}