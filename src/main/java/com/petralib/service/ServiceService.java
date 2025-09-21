package com.petralib.service;

import com.petralib.block.dto.BlockDto;
import com.petralib.block.dto.BlockPage;
import com.petralib.file.FileConstructor;
import com.petralib.file.model.ConstructorModel;
import com.petralib.project.service.ProjectService;
import com.petralib.service.dto.ServiceDto;
import com.petralib.service.dto.ServiceMapper;
import com.petralib.service.dto.ServicePage;
import com.petralib.service.entity.ServiceEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ServiceService {

    ServiceMapper serviceMapper;
    ServiceRepository serviceRepository;
    ProjectService projectService;
    FileConstructor fileConstructor;

    @Transactional(readOnly = true)
    public ConstructorModel createConstructorModel(Long serviceId){
        return fileConstructor.create(serviceId);
    }

    @Transactional(readOnly = true)
    public ServicePage getServiceByProjectAndName(int pageSize, int lastPageNumber, Long projectId, String serviceName) {
        Page<ServiceEntity> serviceEntities;
        if (null != serviceName && !serviceName.isBlank()){
            serviceEntities = serviceRepository.findServiceByName(projectId, serviceName,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        }else {
            serviceEntities = serviceRepository.findService(projectId,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        }
        Collection<ServiceDto> dtos = serviceMapper.mapToDto(serviceEntities.toList());
        return new ServicePage(lastPageNumber, dtos);
    }

    @Transactional(readOnly = true)
    public Collection<ServiceEntity> getServices(Long projectId){
        return serviceRepository.findServicesOfProject(projectId);
    }

    @Transactional
    public ServiceEntity getService(Long serviceId){
        Optional<ServiceEntity> blockEntity = serviceRepository.findById(serviceId);
        if (blockEntity.isPresent()){
            return blockEntity.get();
        }else {
            throw new NullPointerException("Block not found");
        }
    }
    @Transactional
    public ServiceEntity saveService(ServiceDto serviceDto) {
        ServiceEntity serviceEntity = serviceMapper.fromDtoToEntity(serviceDto);
        Optional<ServiceEntity> loadedEntity = serviceRepository.getByName(serviceEntity.getName(), serviceDto.getProjectId());
        loadedEntity.ifPresent(entity -> serviceEntity.setId(entity.getId()));
        return serviceRepository.save(serviceEntity);
    }

    @Transactional
    public void deleteService(Long serviceId){
        serviceRepository.deleteById(serviceId);
    }
}
