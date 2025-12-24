package com.erp.Service.ServiceType;

import com.erp.CustomRepository.ServiceCustomRepository;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Request.ServiceTypeGetRequest;
import com.erp.Dto.Response.*;
import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.Service_Exception.ServiceNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.Branch.BranchMapper;
import com.erp.Mapper.Service.ServiceMapper;
import com.erp.Model.*;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import com.erp.Repository.Service.Entitymanager.ServiceTypeRepo;
import com.erp.Repository.Service.ServiceDocumentsRepository;
import com.erp.Repository.Service.ServiceRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.inerfaces.S3StorageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class ServiceTypeImpl implements ServiceType
{
    @Value("${aws.s3.bucket}")
    private String bucket;

    private final ServiceCustomRepository customRepository;
    private final ServiceRepository repository;
    private final ServiceMapper serviceMapper;
    private final ServiceTypeRepo serviceTypeRepo;
    private final BranchRepository branchRepository;
    private final UserIdentity userIdentity;
    private final UserRepository userRepository;
    private final InventoryRepositoryV2 inventoryRepository;
    private final ServiceDocumentsRepository serviceDocumentsRepository;
    private final S3StorageService s3StorageService;
    private final S3Presigner s3Presigner;

    @Override
    @Transactional
    public ServiceResponse addService(ServiceRequest serviceRequest, MultipartFile[] files)
    {
        Service service = serviceMapper.mapToService(serviceRequest);
        Branch branch = branchRepository.findById(serviceRequest.getBranchId())
                        .orElseThrow(() -> new BranchNotFoundException("Branch Not Found"));
        service.setBranch(branch);

        List<FileUploadResponse> fileUploadResponses = s3StorageService.uploadFile(files, "service");
        List<ServiceDocuments> serviceDocuments = new ArrayList<>();

        if (serviceRequest.getInventoryIds() != null && !serviceRequest.getInventoryIds().isEmpty()) {

            List<InventoryV2> inventories = inventoryRepository.findAllById(serviceRequest.getInventoryIds());

            if (inventories.size() != serviceRequest.getInventoryIds().size()) {
                throw new InventoryNotFoundException("Some inventory IDs are invalid");
            }

            service.setInventories(inventories);
        }

        Service saved = repository.save(service);

        for(FileUploadResponse fileUploadResponse : fileUploadResponses){
            ServiceDocuments serviceDocument = new ServiceDocuments();
            serviceDocument.setService(saved);
            serviceDocument.setDocumentUrl(fileUploadResponse.getS3Key());
            serviceDocument.setDocumentName(fileUploadResponse.getFileName());
            serviceDocuments.add(serviceDocument);
        }

        serviceDocumentsRepository.saveAll(serviceDocuments);
        return toResponse(saved);
    }


    @Override
    public ServiceResponse updateById(ServiceRequest serviceRequest)
    {
        Service service = repository.findById(serviceRequest.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service Not Found, Invalid ID !!"));

        serviceMapper.mapToServiceEntity(serviceRequest, service);
        Branch branch = branchRepository.findById(serviceRequest.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found"));
        service.setBranch(branch);
        repository.save(service);
        return toResponse(service);
    }

    @Override
    public ServiceResponse deleteByServiceId(CommanParam param)
    {
        Service service = repository.findById(param.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service Not Found, Invalid ID !!"));

        repository.deleteById(param.getId());
        return toResponse(service);
    }


    @Override
    public ResultDto<ServiceResponse> fetchAllServices()
    {
        List<ServiceResponse> services = new ArrayList<>();
        for(Service s : repository.findAll()){
            services.add(toResponse(s));
        }
        ResultDto<ServiceResponse> resultDto = new ResultDto<>();

        resultDto.setResults(services != null ? services : List.of());
        resultDto.setCount(services != null ? services.size() : 0);

        return resultDto;
    }


    @Override
    public List<ServiceResponse> findByStatus(ServiceRequest serviceRequest)
    {
        List<Service> services = repository.findByServiceStatus(serviceRequest.getServiceStatus());

        if(services.isEmpty())
        {
            throw new ResourceNotFoundException("Services Not Found !! Using Status");
        }

        return serviceMapper.mapToServiceResponse(services);
    }


    @Override
    public List<String> fetchAllCategories()
    {
        List<String> categories = new ArrayList<>();
        for(ServiceCategory category : ServiceCategory.values())
        {
            categories.add(String.valueOf(category));
        }
        return categories;
    }


    @Override
    public List<ServiceResponse> findByServiceCategory(@RequestBody ServiceRequest serviceRequest)
    {
        List<Service> services = repository.findByServiceCategory(serviceRequest.getServiceCategory());

        if(services.isEmpty())
        {
            throw new ResourceNotFoundException("Services Not Found !! Using Category");
        }

        return serviceMapper.mapToServiceResponse(services);
    }


    public List<Service> getAllServicesByIds(List<Long> serviceIds)
    {
        log.info("[ServiceTypeImpl]  [getAllServicesByIds]  getting services by {}",serviceIds);
        List<Service> services = repository.findAllById(serviceIds);
        if(services.isEmpty())
        {
            return new ArrayList<>();
        }
        return services;
    }

    @Override
    public ResultDto<ServiceTypeResponse> getAllServices(ServiceTypeGetRequest request) {
        log.info("[ServiceTypeImpl]  [getAllServices]  into get all services data " );
        ResultDto<ServiceTypeResponse>  serviceTypeResponses = serviceTypeRepo.getAllServices(request);
        log.info("[ServiceTypeImpl]  [getAllServices]  exit get all services data " );
        return serviceTypeResponses;
    }


    @Override
    public ResultDto<ServiceResponse> getAllServicesByFilter(FilterRequest filterRequest)
    {
        ResultDto<ServiceResponse>  serviceResponses = customRepository.getServiceDetailsFilter(filterRequest);

        log.info("[ServiceTypeImpl]  [getAllServicesByFilter]  exit get all services data " );

        return serviceResponses;
    }

    @Override
    public ResultDto<ServiceResponse> fetchAllServicesManagerWise() {
        GenericUser genericUser = userIdentity.getCurrentUser();

        User user = userRepository.findByEmail(genericUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        List<ServiceResponse> services = new ArrayList<>();
        for(Service s : repository.findByBranch_BranchId(user.getBranch().getBranchId())){
            services.add(toResponse(s));
        }
        ResultDto<ServiceResponse> resultDto = new ResultDto<>();

        resultDto.setResults(services != null ? services : List.of());
        resultDto.setCount(services != null ? services.size() : 0);

        return resultDto;
    }

    private ServiceResponse toResponse(Service service)
    {
        ServiceResponse serviceResponse = serviceMapper.mapToServiceResponse(service);
        serviceResponse.setBranchId(service.getBranch().getBranchId());

        List<String> files = serviceDocumentsRepository.findAllUrlsByServiceId(service.getServiceId());
        List<String> imageUrls = files.stream()
                .map(this::generatePresignedUrl)
                .toList();
        serviceResponse.setFiles(imageUrls);

        List<Long> inventoryIds = new ArrayList<>();
        for(InventoryV2 inventoryV2 : service.getInventories()){
            inventoryIds.add(inventoryV2.getItemId());
        }
        serviceResponse.setInventoryIds(inventoryIds);

        return serviceResponse;
    }

    private String generatePresignedUrl(String s3Key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(p -> p
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));

        return presignedRequest.url().toString();
    }


    @Override
    public ResultDto<DropDown> findServicesBranchWiseDropDown() {
        GenericUser genericUser = userIdentity.getCurrentUser();

        User user = userRepository.findByEmail(genericUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        List<DropDown> services = new ArrayList<>();
        for(Service s : repository.findByBranch_BranchIdAndServiceStatus(user.getBranch().getBranchId(), ServiceStatus.ACTIVE)){
            services.add(new DropDown(s.getServiceId(), s.getServiceName()));
        }
        ResultDto<DropDown> resultDto = new ResultDto<>();

        resultDto.setResults(services);
        resultDto.setCount(services.size());

        return resultDto;
    }

    @Override
    public ServiceResponse findById(Long id) {
        Service service = repository.findByServiceIdAndServiceStatus(id, ServiceStatus.ACTIVE)
                .orElseThrow(() -> new ServiceNotFoundException("Service Not Found !!"));

        return toResponse(service);
    }
}
