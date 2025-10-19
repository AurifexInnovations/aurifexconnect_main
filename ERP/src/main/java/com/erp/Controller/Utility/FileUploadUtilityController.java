package com.erp.Controller.Utility;

import com.erp.Dto.Response.BankAccountResponse;
import com.erp.Dto.Response.FileResponse;
import com.erp.Service.Utility.FileService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class FileUploadUtilityController {

    private final FileService fileService;

    @PostMapping("upload/multiple/files/id/{genId}/category/{cName}")
    public ResponseEntity<ListResponseStructure<FileResponse>> uploadMultipleFiles(
            @PathVariable("genId") long id,
            @PathVariable("cName") String category ,
            @RequestParam("files") MultipartFile[] files) {
        List<FileResponse> fileResponses =  fileService.uploadFiles(id , category ,files);
        return ResponseBuilder.success(HttpStatus.CREATED, "Image Uploaded SuccessFully", fileResponses);
    }

    @DeleteMapping("file/{fId}/id/{genId}/category/{cName}")
    public String deleteFile(
            @PathVariable("fId") long id,
            @PathVariable("genId") long genId,
            @PathVariable("cName") String category) {

        fileService.deleteFile(id , genId ,category);
        return "file Deleted Successfully";
    }


    @GetMapping("/file/")
    public ResponseEntity<ListResponseStructure<FileResponse>> getAllFiles(
            @PathVariable("genId") long genId,
            @PathVariable("cName") String category
    ) {
        List<FileResponse> fileResponses = fileService.getAllFiles(genId , category);
        return ResponseBuilder.success(HttpStatus.OK, "All File Fetched Successfully", fileResponses);
    }

}
