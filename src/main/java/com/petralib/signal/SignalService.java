package com.petralib.signal;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.service.BlockService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class SignalService {

    BlockService blockService;

    @Transactional
    public void saveSignal(BlockEntity signal, Long workflowId, Long projectId){
        BlockEntity workflow = blockService.getBlockWithVariables(workflowId);
//        blockService.saveBlock(signal, BlockType.SIGNAL,projectId, workflow.getService().getId());

    }
}
