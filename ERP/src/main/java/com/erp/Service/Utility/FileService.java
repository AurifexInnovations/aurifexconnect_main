package com.erp.Service.Utility;

import com.erp.Dto.Response.FileResponse;
import com.erp.Model.File;
import com.erp.Model.GenericUser;
import com.erp.Repository.Utility.FileRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Strategy.FileUploadContext;
import com.erp.Strategy.FileUploadStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileUploadContext fileUploadContext;
    private final FileRepository fileRepository;
    private final UserIdentity userIdentity;

    public List<FileResponse> uploadFiles(Long genId, String category, MultipartFile[] files) {
        GenericUser user = userIdentity.getCurrentUser();
        Long uploadedBy = user.getId();

        FileUploadStrategy strategy = fileUploadContext.getStrategy(category);

        int seq = getLastSequence(genId , category);

        List<String> filePaths = strategy.uploadFiles(seq , genId, category, files);

        List<File> fileList = new ArrayList<>(filePaths.size());

        for (String path : filePaths) {
            File file = new File();

            file.setFileUrl(path);
            file.setSequence(++seq);
            file.setCategory(category);
            file.setUploadedBy(uploadedBy);
            file.setGenId(genId);
            file.setExtension(path.substring(path.lastIndexOf(".") + 1));
            file.setActive(true);
            file.setUploadedAt(LocalDateTime.now());

            fileList.add(file);
        }

        fileList = fileRepository.saveAll(fileList);

        List<FileResponse> fileResponses = new ArrayList<>(filePaths.size());

        for (File file : fileList){
            FileResponse fileResponse = new FileResponse();

            fileResponse.setId(file.getId());
            fileResponse.setUrl(file.getFileUrl());
            fileResponse.setSequence(file.getSequence());
            fileResponse.setUploadedBy(file.getUploadedBy());

            fileResponses.add(fileResponse);
        }

        return fileResponses;
    }

    public void deleteFile(long fileId , long genId , String category){
        List<File> files =
                fileRepository.findByGenIdAndCategoryAndOrderBySequence(genId ,category );

        List<File> updatedFiles = new LinkedList<>();

        boolean isDeleted = false;

        int previousSequence = 0 ;

        for (File file : files){

            if(file.getId() == fileId ){

                isDeleted = true;
                previousSequence = file.getSequence();

                file.setActive(false);
                file.setDeletedAt(LocalDateTime.now());

                updatedFiles.add(file);
                continue;
            }

            if(isDeleted){
                file.setSequence(++previousSequence);
                file.setModifiedAt(LocalDateTime.now());

                updatedFiles.add(file);
            }
        }

        fileRepository.saveAll(updatedFiles);
    }


    public  List<FileResponse>  getAllFiles(long genId , String category){
        List<File> files =
                fileRepository.findByGenIdAndCategoryAndOrderBySequence(genId ,category );

        List<FileResponse> fileResponses = new ArrayList<>(files.size());

        for (File file : files){
            FileResponse fileResponse = new FileResponse();

            fileResponse.setId(file.getId());
            fileResponse.setUrl(file.getFileUrl());
            fileResponse.setSequence(file.getSequence());
            fileResponse.setUploadedBy(file.getUploadedBy());

            fileResponses.add(fileResponse);
        }

       return  fileResponses;
    }
    private int getLastSequence(long genId , String category){
        Integer lastSeq = fileRepository.findByGenIdAndCategoryMaxSequence(genId, category);
        return (lastSeq == null) ? 0 : lastSeq;
    }
}
