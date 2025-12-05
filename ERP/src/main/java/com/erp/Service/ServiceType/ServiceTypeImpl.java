package com.erp.Service.ServiceType;

import com.erp.CustomRepository.ServiceCustomRepository;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Request.ServiceTypeGetRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ServiceResponse;
import com.erp.Dto.Response.ServiceTypeResponse;
import com.erp.Enum.ServiceCategory;
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
import com.erp.Repository.Service.Entitymanager.ServiceTypeRepo;
import com.erp.Repository.Service.ServiceRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Security.util.UserIdentity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
@Slf4j
public class ServiceTypeImpl implements ServiceType
{
    private final ServiceCustomRepository customRepository;
    private final ServiceRepository repository;
    private final ServiceMapper serviceMapper;
    private final ServiceTypeRepo serviceTypeRepo;
    private final BranchRepository branchRepository;
    private final UserIdentity userIdentity;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public ServiceResponse addService(ServiceRequest serviceRequest)
    {
        Service service = serviceMapper.mapToService(serviceRequest);
        Branch branch = branchRepository.findById(serviceRequest.getBranchId())
                        .orElseThrow(() -> new BranchNotFoundException("Branch Not Found"));
        service.setBranch(branch);

        if (serviceRequest.getInventoryIds() != null && !serviceRequest.getInventoryIds().isEmpty()) {

            List<Inventory> inventories = inventoryRepository.findAllById(serviceRequest.getInventoryIds());

            if (inventories.size() != serviceRequest.getInventoryIds().size()) {
                throw new InventoryNotFoundException("Some inventory IDs are invalid");
            }

            service.setInventories(inventories);
        }

        repository.save(service);
        return toResponse(service);
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
    public List<ServiceResponse> findByIdOrServiceName(CommanParam param)
    {
        List<Service> services = repository.findByServiceIdOrServiceName(param.getId(),param.getName());

        if(services.isEmpty())
        {
            throw new ResourceNotFoundException("Services Not Found !! Using ID or Name");
        }

        return serviceMapper.mapToServiceResponse(services);
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
        return serviceResponse;
    }
}
