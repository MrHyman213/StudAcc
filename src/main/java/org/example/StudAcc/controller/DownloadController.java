package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.ReportDTO;
import org.example.StudAcc.DTO.TemplatesDTO;
import org.example.StudAcc.service.download.GeneratingFilesByTemplateService;
import org.example.StudAcc.service.download.excel.ParseCSVService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class DownloadController {

    private final GeneratingFilesByTemplateService templateService;
    private final ParseCSVService parseCSVService;

    @GetMapping("/list")
    public List<TemplatesDTO> templateList(){
        return templateService.getTemplates();
    }

    @PostMapping("/download")
    public ResponseEntity<Resource> list(@RequestParam("id")int id, @RequestParam("idTemplate")int idTemplate, @RequestBody ReportDTO reportDTO){
        return templateService.generateFileByTemplateId(idTemplate, id, reportDTO);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/create")
    public HttpStatus create(@RequestParam("file") MultipartFile file, @RequestParam("reportName") String name,
                             @RequestParam("docType") boolean docType, @RequestParam("onClick") boolean onClick){
        templateService.createNewTemplate(file, name, docType, onClick);
        return HttpStatus.CREATED;
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update")
    public HttpStatus update(@RequestBody TemplatesDTO dto){
        templateService.updatePath(dto.getName(), dto.getDocType(), dto.getId());
        return HttpStatus.ACCEPTED;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/downloadTemplate")
    public ResponseEntity<Resource> downloadTemplate(@RequestParam("id")int id){
        return templateService.downloadTemplate(id);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/updateTemplate")
    public HttpStatus updateTemplate(@RequestParam("file") MultipartFile file, @RequestParam("idPath")int id){
        templateService.updateTemplate(id, file);
        return HttpStatus.ACCEPTED;
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete")
    public HttpStatus delete(@RequestParam("id")int id){
        templateService.deleteTemplate(id);
        return HttpStatus.OK;
    }

    // Excel

    @PostMapping("/csv/upload")
    public HttpStatus uploadCSV(@RequestParam("id") int groupId, @RequestParam("file") MultipartFile file){
        parseCSVService.process(file, groupId);
        return HttpStatus.ACCEPTED;
    }

}
