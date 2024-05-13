package com.petralib.workflow.service;

import com.petralib.block.BlockMapper;
import com.petralib.block.BlockType;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.service.BlockService;
import com.petralib.variable.VariableService;
import com.petralib.workflow.dto.WorkflowDto;
import com.petralib.workflow.dto.WorkflowMapper;
import com.petralib.workflow.repository.WorkflowRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WorkflowService {

//    WorkflowRepository workflowRepository;
//    WorkflowMapper workflowMapper;
    BlockService blockService;
    BlockMapper blockMapper;
    VariableService variableService;

//    @Transactional(readOnly = true)
//    public WorkflowPage getHighLevelWorkflowsByProject(int pageSize, int lastPageNumber, Long projectId) {
//        Page<WorkflowEntity> workflowEntityPage = workflowRepository.findHighLevelWorkflows(projectId,
//                PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
//        List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos = workflowMapper.map(workflowEntityPage.toList());
//        return new WorkflowPage(workflowEntityPage.getTotalPages(), workflowEntityPage.getTotalElements(), workflowCollectionObjectDtos);
//    }
//
//    @Transactional(readOnly = true)
//    public Collection<WorkflowCollectionObjectDto> getChildrenWorkflows(Long parentId) {
//        List<WorkflowEntity> entities = workflowRepository.findWorkflowsByParent(parentId);
//        return workflowMapper.map(entities);
//    }
//
//    @Transactional(readOnly = true)
//    public WorkflowPage getWorkflowsByProject(int pageSize, int lastPageNumber, Long projectId) {
//        Page<WorkflowEntity> workflowEntityPage = workflowRepository.findWorkflows(projectId,
//                PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
//        List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos = workflowMapper.map(workflowEntityPage.toList());
//        return new WorkflowPage(workflowEntityPage.getTotalPages(), workflowEntityPage.getTotalElements(), workflowCollectionObjectDtos);
//    }
//
//    @Transactional(readOnly = true)
//    public WorkflowPage getWorkflowsByProjectAndName(int pageSize, int lastPageNumber, Long projectId, String workflowName) {
//        Page<WorkflowEntity> workflowEntityPage = workflowRepository.findWorkflowsByName(projectId, workflowName,
//                PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
//        List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos = workflowMapper.map(workflowEntityPage.toList());
//        return new WorkflowPage(workflowEntityPage.getTotalPages(), workflowEntityPage.getTotalElements(), workflowCollectionObjectDtos);
//    }

    @Transactional
    public void editWorkflow(WorkflowDto workflowDto, Long projectId) {
        BlockEntity blockEntity = blockMapper.fromWorkflowDtoToEntity(workflowDto);
        System.out.println("EDIT: " + blockEntity);
        blockEntity = blockService.saveBlock(blockEntity, BlockType.WORKFLOW, projectId);
        System.out.println(blockEntity.toString());
//        variableService.updateVariables(workflowDto.getVariableList(), blockEntity);
    }

    @Transactional(readOnly = true)
    public WorkflowDto getWorkflowWithVariables(Long workflowId){
        BlockEntity blockEntity = blockService.getBlockWithVariables(workflowId);
        return blockMapper.fromEntityToWorkflowDto(blockEntity);
    }

    @Transactional
    public void deleteWorkflow(Long workflowId){
        blockService.deleteBlock(workflowId);
    }
}
