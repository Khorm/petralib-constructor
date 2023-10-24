package com.petralib.workflow.service;

import com.petralib.workflow.dto.WorkflowCollectionObjectDto;
import com.petralib.workflow.dto.WorkflowMapper;
import com.petralib.workflow.dto.WorkflowPage;
import com.petralib.workflow.entity.WorkflowEntity;
import com.petralib.workflow.repository.WorkflowRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WorkflowService {

    WorkflowRepository workflowRepository;
    WorkflowMapper workflowMapper;

    @Transactional(readOnly = true)
    public WorkflowPage getHighLevelWorkflowsByProject(int pageSize, int lastPageNumber, Long projectId) {
        Page<WorkflowEntity> workflowEntityPage = workflowRepository.findHighLevelWorkflows(projectId,
                PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos = workflowMapper.map(workflowEntityPage.toList());
        return new WorkflowPage(workflowEntityPage.getTotalPages(), workflowEntityPage.getTotalElements(), workflowCollectionObjectDtos);
    }

    @Transactional(readOnly = true)
    public Collection<WorkflowCollectionObjectDto> getChildrenWorkflows(Long parentId) {
        List<WorkflowEntity> entities = workflowRepository.findWorkflowsByParent(parentId);
        return workflowMapper.map(entities);
    }

    @Transactional(readOnly = true)
    public WorkflowPage getWorkflowsByProject(int pageSize, int lastPageNumber, Long projectId) {
        Page<WorkflowEntity> workflowEntityPage = workflowRepository.findWorkflows(projectId,
                PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos = workflowMapper.map(workflowEntityPage.toList());
        return new WorkflowPage(workflowEntityPage.getTotalPages(), workflowEntityPage.getTotalElements(), workflowCollectionObjectDtos);
    }

    @Transactional(readOnly = true)
    public WorkflowPage getWorkflowsByProjectAndName(int pageSize, int lastPageNumber, Long projectId, String workflowName) {
        Page<WorkflowEntity> workflowEntityPage = workflowRepository.findWorkflowsByName(projectId, workflowName,
                PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos = workflowMapper.map(workflowEntityPage.toList());
        return new WorkflowPage(workflowEntityPage.getTotalPages(), workflowEntityPage.getTotalElements(), workflowCollectionObjectDtos);
    }
}
