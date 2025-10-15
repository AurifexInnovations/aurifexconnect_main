package com.erp.Service.ServiceType;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Response.ServiceResponse;
import com.erp.Enum.ServiceCategory;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.Service_Exception.ServiceNotFoundException;
import com.erp.Mapper.Service.ServiceMapper;
import com.erp.Model.Service;
import com.erp.Repository.Service.ServiceRepository;
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
    private final ServiceRepository repository;
    private final ServiceMapper serviceMapper;


    @Override
    public ServiceResponse addService(ServiceRequest serviceRequest)
    {
        Service service = serviceMapper.mapToService(serviceRequest);
        repository.save(service);
        return serviceMapper.mapToServiceResponse(service);
    }


    @Override
    public ServiceResponse updateById(ServiceRequest serviceRequest)
    {
        Service service = repository.findById(serviceRequest.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service Not Found, Invalid ID !!"));

        serviceMapper.mapToServiceEntity(serviceRequest, service);
        repository.save(service);
        return serviceMapper.mapToServiceResponse(service);
    }


    @Override
    public List<ServiceResponse> findByIdOrServiceName(CommanParam param)
    {
        List<Service> services = repository.findByServiceIdOrServiceName(param.getId(),param.getName());

        return serviceMapper.mapToServiceResponse(services);
    }


    @Override
    public ServiceResponse deleteByServiceId(CommanParam param)
    {
        Service service = repository.findById(param.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service Not Found, Invalid ID !!"));

        repository.deleteById(param.getId());
        return serviceMapper.mapToServiceResponse(service);
    }


    @Override
    public List<ServiceResponse> fetchAllServices()
    {
        List<Service> services = repository.findAll();

        return serviceMapper.mapToServiceResponse(services);
    }


    @Override
    public List<ServiceResponse> findByStatus(ServiceRequest serviceRequest)
    {
        List<Service> services = repository.findByServiceStatus(serviceRequest.getServiceStatus());

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
}
