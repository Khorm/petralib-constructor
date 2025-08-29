package com.petralib.workflow.service;

import com.petralib.block.mapper.BlockMapper;
import com.petralib.block.service.BlockService;
//import com.petralib.variable.VariableService;

//@Service
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
public class WorkflowService {

    //    WorkflowRepository workflowRepository;
//    WorkflowMapper workflowMapper;
    BlockService blockService;
    BlockMapper blockMapper;
//    VariableService variableService;

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

//    @Transactional(readOnly = true)
//    public WorkflowPage getWorkflowsByProjectAndName(int pageSize, int lastPageNumber, Long projectId, String workflowName) {
//        Page<WorkflowEntity> workflowEntityPage = workflowRepository.findWorkflowsByName(projectId, workflowName,
//                PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
//        List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos = workflowMapper.map(workflowEntityPage.toList());
//        return new WorkflowPage(workflowEntityPage.getTotalPages(), workflowEntityPage.getTotalElements(), workflowCollectionObjectDtos);
//    }


//    @Transactional(readOnly = true)
//    public BlockDto getWorkflowWithVariables(Long workflowId){
//        BlockEntity blockEntity = blockService.getBlockWithVariables(workflowId);
//        return blockMapper.fromEntityToDto(blockEntity);
//    }

//    @Transactional
//    public void deleteWorkflow(Long workflowId){
//        blockService.deleteBlock(workflowId);
//    }
}
