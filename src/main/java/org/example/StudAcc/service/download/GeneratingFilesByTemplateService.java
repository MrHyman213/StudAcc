package org.example.StudAcc.service.download;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.ReportDTO;
import org.example.StudAcc.DTO.TemplatesDTO;
import org.example.StudAcc.model.download.Path;
import org.example.StudAcc.model.student.Student;
import org.example.StudAcc.repository.download.PathRepository;
import org.example.StudAcc.service.ListService;
import org.example.StudAcc.service.organization.GroupService;
import org.example.StudAcc.service.student.StudentService;
import org.example.StudAcc.utils.GenerateDocFromTemplate;
import org.example.StudAcc.utils.exceptions.download.FileFormatException;
import org.example.StudAcc.utils.exceptions.download.FileNameAlreadyUsedException;
import org.example.StudAcc.utils.exceptions.download.PathNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneratingFilesByTemplateService {

    private final PathRepository repository;
    private final GroupService groupService;
    private final StudentService studentService;
    private final ListService listService;
    private final PathRepository pathRepository;
    private final ModelMapper mapper;
    @Lazy
    private final GenerateDocFromTemplate generator;
    private static final String PATH_TEMPLATE = "src\\main\\document\\word\\template\\";
    private static  final String PATH_TEST = "src\\main\\document\\word\\";
    private final Map<String, String> keyWords = new HashMap<>();

    @Bean
    public Map<String, String> getKeyWords(){
        return keyWords;
    }

    public List<TemplatesDTO> getTemplates() {
        return repository.findAll().stream().map(this::modelToMap).collect(Collectors.toList());
    }

    public Path getById(int id) {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new PathNotFoundException("Шаблон с идентификатором " + id + " не найден.");
        }
    }

    public ResponseEntity<Resource> generateFileByTemplateId(int idTemplate, int id, ReportDTO dto){
        Path path = getById(idTemplate);
        generator.init(path.getName());
        generator.setTemplate(PATH_TEMPLATE + path.getPath());
        if(!path.isDocType()) {
            List<Student> studentList = studentService.getByGroup(groupService.getById(id));
            setKeyWordsOnText(dto, id);
            generator.replaceTextInDocumentBody();
            generator.completionOfDataOnTable(studentList);
        } else {
            Student student = studentService.getById(dto.getStudentId());
            setKeyWordsOnText(dto);
            generator.replaceTextInDocumentBody();
            generator.completionOfDataOnTable(List.of(student));
        }
        generator.create();
        return discharge(path.getName() + ".docx", PATH_TEST);
    }

    private void setKeyWordsOnText(ReportDTO dto, int groupId) {
        keyWords.put("group", groupService.getById(groupId).getName());
        try {
            keyWords.put("dateEvent", dto.getDateEvent().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } catch (NullPointerException ignored) {}
        if (dto.getEmployeeIdList() != null && !dto.getEmployeeIdList().isEmpty()) {
            List<String> employeeList = new ArrayList<>();
            for (int id : dto.getEmployeeIdList()) {
                if (id != 0)
                    employeeList.add(listService.getEmployee(id).getShortName());
            }
            StringBuilder builder = new StringBuilder();
            employeeList.forEach(str -> builder.append(str).append(", "));
            builder.replace(builder.length() - 2, builder.length() - 1, "");
            keyWords.put("teacherList", builder.toString());
        }
        if (dto.getDisciplineId() != null)
            keyWords.put("discipline", listService.getDiscipline(dto.getDisciplineId()).getName());
        keyWords.put("numberProtocol", dto.getNumberProtocol());
    }

    private void setKeyWordsOnText(ReportDTO dto){
        System.out.println(dto);
        keyWords.put("group", studentService.getById(dto.getStudentId()).getGroup().getName());
        keyWords.put("discipline", listService.getDiscipline(dto.getDisciplineId()).getName());
        keyWords.put("shortInit", studentService.getById(dto.getStudentId()).getShortName());
        keyWords.put("semester", dto.getSemester());
        keyWords.put("yearStudy", dto.getYearStudy());
        keyWords.put("numberProtocol", dto.getNumberProtocol());
    }

    public ResponseEntity<Resource> downloadTemplate(int id){
        return discharge(getById(id).getPath(), PATH_TEMPLATE);
    }

    private ResponseEntity<Resource> discharge(String name, String path){
        copyFile(path + name);
        File file = new File(path + name);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(name, StandardCharsets.UTF_8));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Accept-Encoding", "UTF-8");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        ByteArrayResource resource;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(java.nio.file.Path.of(file.getAbsolutePath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Transactional
    public void createNewTemplate(MultipartFile file, String name, boolean docType, boolean onClick){
        String fileName = file.getOriginalFilename();
        int dotCount = 0;
        assert fileName != null;
        if(validateFileName(fileName)) {
            for (int i = 0; i < file.getOriginalFilename().length(); i++)
                if (file.getOriginalFilename().charAt(i) == '.') dotCount++;
            if (dotCount == 1) {
                if (!contains(file.getOriginalFilename())){
                    try{
                        createFile(file.getBytes(), fileName);
                        createPath(name, fileName, docType, onClick);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else throw new FileNameAlreadyUsedException("Файл с данным именем уже загружался ранее и не был обработан. Попробуйте позже или переименуйте файл.");
            } else throw new FileFormatException("Не верный формат файла.");
        } else throw new FileFormatException("Не верный формат файла.");
    }

    private boolean validateFileName(String fileName){
        return fileName.endsWith(".docx");
    }

    @Transactional
    public void updateTemplate(int id, MultipartFile file) {
        Path path = getById(id);
        if(file != null) {
            if (validateFileName(Objects.requireNonNull(file.getOriginalFilename()))) {
                String fileName = file.getOriginalFilename();
                try {
                    deleteFile(path.getPath());
                    createFile(file.getBytes(), fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                path.setPath(fileName);
            } else throw new FileFormatException("Указан не верный формат файла.");
        }
    }

    @Transactional
    public void deleteTemplate(int id){
        Path path = getById(id);
        deleteFile(path.getPath());
        deletePath(id);
    }

    private void createFile(byte[] bytes, String fileName){
        try {
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(PATH_TEMPLATE + fileName));
            stream.write(bytes);
            stream.close();
            copyFile(PATH_TEMPLATE + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFile(String path){
        try {
            Files.delete(java.nio.file.Path.of(PATH_TEMPLATE + path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean contains(String fileName){
        return pathRepository.findByPath(fileName).isPresent();
    }

    @Transactional
    private void createPath(String name, String path, boolean docType, boolean onClick){
        pathRepository.save(new Path(name, path, docType, onClick));
    }

    @Transactional
    public void updatePath(String name, Boolean docType, int id){
        if(name != null) getById(id).setName(name);
        if(docType != null) getById(id).setDocType(docType);
    }

    @Transactional
    private void deletePath(int id){
        pathRepository.deleteById(id);
    }

    private void copyFile(String path){
        java.nio.file.Path destFile = Paths.get(path);
        SeekableByteChannel destFileChannel;
        try {
            destFileChannel = Files.newByteChannel(destFile);
            destFileChannel.close();
            Files.copy(destFile, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TemplatesDTO modelToMap(Path path){
        return mapper.map(path, TemplatesDTO.class);
    }
}
