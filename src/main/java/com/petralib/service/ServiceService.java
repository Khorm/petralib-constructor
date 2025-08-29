package com.petralib.service;

import com.petralib.block.dto.BlockDto;
import com.petralib.block.dto.BlockPage;
import com.petralib.project.service.ProjectService;
import com.petralib.service.dto.ServiceDto;
import com.petralib.service.dto.ServiceMapper;
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

    @Transactional(readOnly = true)
    public BlockPage getServiceByProjectAndName(int pageSize, int lastPageNumber, Long projectId, String serviceName) {
        Page<ServiceEntity> serviceEntities;
        if (null != serviceName && !serviceName.isBlank()){
            serviceEntities = serviceRepository.findServiceByName(projectId, serviceName,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        }else {
            serviceEntities = serviceRepository.findService(projectId,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        }
        Collection<BlockDto> dtos = serviceMapper.mapToBlock(serviceEntities.toList());
        return new BlockPage(serviceEntities.getTotalPages(), dtos);
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
        if (serviceRepository.existsByName(serviceEntity.getName(), serviceDto.getProjectId())){
            throw new IllegalArgumentException("Service with this name already exists");
        }
        return serviceRepository.save(serviceEntity);
    }

    @Transactional
    public void deleteService(Long serviceId){
        serviceRepository.deleteById(serviceId);
    }
}
