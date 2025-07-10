package com.erp.Service.ServiceType;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Response.ServiceResponse;
import com.erp.Exception.Service_Exception.ServiceNotFoundByIdException;
import com.erp.Mapper.Service.ServiceMapper;
import com.erp.Model.Service;
import com.erp.Repository.Service.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceTypeImpl implements ServiceType {

    private final ServiceRepository repository;
    private final ServiceMapper serviceMapper;

    @Override
    public ServiceResponse addService(ServiceRequest serviceRequest) {
        Service service = serviceMapper.mapToService(serviceRequest);
        repository.save(service);
        return serviceMapper.mapToServiceResponse(service);
    }

    @Override
    public ServiceResponse updateById(ServiceRequest serviceRequest) {
        Service service = repository.findById(serviceRequest.getId())
                .orElseThrow(() -> new ServiceNotFoundByIdException("Invalid ID! Service not found."));

        serviceMapper.mapToServiceEntity(serviceRequest, service);
        repository.save(service);
        return serviceMapper.mapToServiceResponse(service);
    }

    @Override
    public List<ServiceResponse> findByIdOrServiceName(CommanParam param) {
        List<Service> services = repository.findByServiceIdOrServiceName(param.getId(),param.getName());
        if (services.isEmpty()) {
            throw new ServiceNotFoundByIdException("No services found with the given Details!");
        }
        return serviceMapper.mapToServiceResponse(services);
    }

    @Override
    public ServiceResponse deleteByServiceId(CommanParam param) {
        Service service = repository.findById(param.getId())
                .orElseThrow(() -> new ServiceNotFoundByIdException("Service not found, invalid ID."));
        repository.deleteById(param.getId());
        return serviceMapper.mapToServiceResponse(service);
    }

    @Override
    public List<ServiceResponse> fetchAllServices() {
        List<Service> services = repository.findAll();
        if (services.isEmpty()) {
            throw new ServiceNotFoundByIdException("No services found!");
        }
        return serviceMapper.mapToServiceResponse(services);
    }

    @Override
    public List<ServiceResponse> findByStatus(ServiceRequest serviceRequest) {
        List<Service> services = repository.findByServiceStatus(serviceRequest.getServiceStatus());
        if (services.isEmpty()) {
            throw new ServiceNotFoundByIdException("No services found with status: " + serviceRequest.getServiceStatus());
        }
        return serviceMapper.mapToServiceResponse(services);
    }

    @Override
    public List<String> fetchAllCategories() {
        List<Service> services = repository.findAll();
        return services.stream()
                .map(Service::getCategories)
                .distinct()
                .toList();
    }

    @Override
    public List<ServiceResponse> fetchServiceByCategory(@RequestBody ServiceRequest serviceRequest){
        List<Service> services = repository.findServiceByCategories(serviceRequest.getCategories());
        if(services.isEmpty()){
            throw  new ServiceNotFoundByIdException("Service Not Found!");
        }
        return serviceMapper.mapToServiceResponse(services);
    }

}
