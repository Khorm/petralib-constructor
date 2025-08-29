package com.petralib.type;

import com.petralib.project.repository.ProjectRepository;
import com.petralib.type.dto.TypeFullDto;
import com.petralib.type.dto.TypePage;
import com.petralib.type.dto.TypeShortDto;
import com.petralib.type.entity.TypeEntity;
import com.petralib.type.entity.TypeVariableEntity;
import com.petralib.type.mapper.ShortTypeMapper;
import com.petralib.type.mapper.TypeMapper;
import com.petralib.type.repository.TypeRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TypeService {

    TypeRepo typeRepo;
    ProjectRepository projectRepository;
    TypeMapper typeMapper;
    ShortTypeMapper shortTypeMapper;


    public TypePage getTypesPage(int pageSize, int lastPageNumber, Long projectId, String typeName) {
        Page<TypeEntity> typeEntities;
        if (null != typeName && !typeName.isBlank()) {
            typeEntities = typeRepo.findTypesByName(projectId, typeName,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        } else {
            typeEntities = typeRepo.findTypes(projectId,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        }
        Collection<TypeFullDto> dtos = typeMapper.map(typeEntities.toList());
        return new TypePage(typeEntities.getTotalPages(), dtos);
    }

    public Collection<TypeShortDto> getAllTypes(Long projectId) {
        Collection<TypeEntity> entities = typeRepo.findTypes(projectId);

        return shortTypeMapper.map(entities);
    }

    @Transactional
    public TypeEntity save(TypeFullDto dto, Long projectId) {
        if (typeRepo.existsByName(dto.getName(), projectId) && dto.getId() == null) {
            throw new IllegalArgumentException("Type with same name already exists");
        }
        System.out.println(dto);
        TypeEntity entity = typeMapper.dtoToEntity(dto);
        Set<String> namesSet = new HashSet<>();
        for (TypeVariableEntity variable : entity.getVariables()) {
            variable.setOwner(entity);
            if (!namesSet.contains(variable.getName())){
                namesSet.add(variable.getName());
            }else {
                throw new IllegalArgumentException("Same variable name is not allowed");
            }
        }
        entity.setProject(projectRepository.getReferenceById(projectId));
        return typeRepo.save(entity);
    }

    @Transactional
    public void delete(Long typeId) {
        typeRepo.deleteById(typeId);
    }

    @Transactional(readOnly = true)
    public Collection<TypeVariableEntity> getTypeVariables(Long typeId){
        TypeEntity type = typeRepo.findById(typeId).orElseThrow(NullPointerException::new);
        return type.getVariables();
    }
}
