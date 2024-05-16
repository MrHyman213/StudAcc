package org.example.StudAcc.service.download.excel;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.excel.Student;
import org.example.StudAcc.service.student.StudentService;
import org.example.StudAcc.utils.ExcelParser;
import org.example.StudAcc.utils.exceptions.download.FileFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParseCSVService {

    private final String path = "src\\main\\document\\excel\\";
    private final String uploaded = path + "uploaded\\";
    private final String processed = path + "processed\\";
    private final StudentService studentService;

    public void process(MultipartFile file, int groupId){
        upload(file);
        String fileName = file.getOriginalFilename();
        String newDir = processed + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "\\";
        List<Student> studentList = ExcelParser.csvToModel(uploaded);
        if (!studentList.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(newDir));
                relocateFile(uploaded + fileName,
                        newDir + fileName);
                studentService.processStudents(studentList, groupId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        if (validateFileName(fileName)){
            try {
                createFile(file.getBytes(), fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else throw new FileFormatException("Не верный формат файла.");
    }

    private void relocateFile(String oldPath, String newPath) {
        try {
            Files.move(Paths.get(oldPath), Paths.get(newPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateFileName(String fileName){
        int dotCount = 0;
        for (int i = 0; i < fileName.length(); i++)
            if (fileName.charAt(i) == '.') dotCount++;
        return fileName.endsWith(".csv") && dotCount == 1;
    }

    private void createFile(byte[] bytes, String fileName){
        try {
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(uploaded + fileName));
            stream.write(bytes);
            stream.close();
            copyFile(uploaded + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyFile(String path) {
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
}
