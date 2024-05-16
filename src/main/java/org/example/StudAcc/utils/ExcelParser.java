package org.example.StudAcc.utils;

import org.example.StudAcc.DTO.excel.PositionContainer;
import org.example.StudAcc.DTO.excel.Student;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ExcelParser {

    private static PositionContainer container;

    public static List<Student> csvToModel(String dirPath){
        List<String> paths = readFilesOnDirectory(dirPath);
        List<Student> studentList = new ArrayList<>();
        for (String path: paths){
               studentList.addAll(parse(readCSV(path)));
        }
        return studentList;
    }

    private static List<Student> parse(List<String> data){
        List<Student> studentList = new ArrayList<>();
        container = new PositionContainer();
        setPositions(data);
        String[] properties;
        for(String row: data) {
            Student student = new Student();
            properties = row.split(";");
            for (int i = 0; i < properties.length; i++){
                if (container.getColumn(i) != null) {
                    setField(student, container.getColumn(i), properties[i]);
                }
            }
            if(student.getDateOrder() != null && !Objects.equals(student.getDateOrder(), ""))
                processOrder(student);
            studentList.add(student);
        }
        return studentList;
    }

    private static void processOrder(Student student){
        student.setOrderNumber("№" + student.getOrderNumber() + " от " + student.getDateOrder());
    }

    private static void setField(Student student, String columnName, String value){
        switch (columnName) {
            case "Имя" -> student.setName(value);
            case "Фамилия" -> student.setSurname(value);
            case "Отчество" -> student.setPatronymic(value);
            case "Дата рождения" -> student.setBirthDate(LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            case "Пол" -> student.setSex(value);
            case "СНИЛС" -> student.setSnils(value);
            case "№ приказа" -> student.setOrderNumber(value);
            case "Дата приказа" -> student.setDateOrder(value);
            case "Дата окончания предыдущего обучения" -> student.setGraduationYear(Integer.parseInt(value.substring(6)));
            case "Образование" -> student.setEducation(value);
            case "Телефон" -> student.setPhone(value);
        }
    }

    private static void setPositions(List<String> data){
        String[] columns = data.get(0).split(";");
        for (int i = 0; i < columns.length; i++)
            container.addPosition(i, columns[i].replace("[^\\\\p{L}]", ""));
        data.remove(0);
    }

    private static List<String> readFilesOnDirectory(String path){
        File dir = new File(path);
        List<String> paths = new ArrayList<>();
        if(dir.isDirectory()){
            for (File file: Objects.requireNonNull(dir.listFiles())){
                if (!file.isDirectory() && file.getName().endsWith(".csv")) {
                    paths.add(file.getAbsolutePath());
                }
            }
        }
        return paths;
    }

    private static List<String> readCSV(String path){
        try {
            return Files.lines(
                    Path.of(path), Charset.forName("windows-1251")).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
