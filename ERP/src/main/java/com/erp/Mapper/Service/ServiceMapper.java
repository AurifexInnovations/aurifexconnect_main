package com.erp.Mapper.Service;

import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Response.ServiceResponse;
import com.erp.Model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    /**
     * Maps a {@link Service} entity to a {@link ServiceResponse}.
     *
     * @param service The {@link Service} entity to be mapped.
     * @return A {@link ServiceResponse} object containing the mapped details.
     */
    ServiceResponse mapToServiceResponse(Service service);

    /**
     * Maps a list of {@link Service} entities to a list of {@link ServiceResponse}.
     *
     * @param services List of service entities.
     * @return List of corresponding service response DTOs.
     */
    List<ServiceResponse> mapToServiceResponse(List<Service> services);

    /**
     * Maps a {@link ServiceRequest} DTO to a new {@link Service} entity.
     *
     * @param serviceRequest The incoming request DTO.
     * @return A new Service entity.
     */
    Service mapToService(ServiceRequest serviceRequest);

    /**
     * Maps a {@link ServiceRequest} to an existing {@link Service} entity for updates.
     *
     * @param serviceRequest The request DTO.
     * @param service The existing entity to update.
     */
    void mapToServiceEntity(ServiceRequest serviceRequest, @MappingTarget Service service);
}
