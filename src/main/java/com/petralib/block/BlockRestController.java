package com.petralib.block;

import com.petralib.block.dto.BlockDto;
import com.petralib.block.dto.BlockPage;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.mapper.BlockMapper;
import com.petralib.block.service.BlockService;
import com.petralib.type.enums.Multiplicity;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlockRestController {

    BlockService blockService;
    BlockMapper blockMapper;

    //    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping("workflow/page")
    public ResponseEntity<?> getWorkflowPage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
                                             @RequestParam String name, @RequestParam Integer pageElementsCount) {
        BlockPage blockPage = blockService.getBlocksByProjectAndName(pageElementsCount, pageNumber, projectId, name, BlockType.WORKFLOW);
        return ResponseEntity.ok(blockPage);
    }

    //    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping("action/page")
    public ResponseEntity<?> getActionPage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
                                           @RequestParam String name, @RequestParam Integer pageElementsCount) {
        BlockPage blockPage = blockService.getBlocksByProjectAndName(pageElementsCount, pageNumber, projectId, name, BlockType.ACTION);
        return ResponseEntity.ok(blockPage);
    }

    @GetMapping("source/page")
    public ResponseEntity<?> getSourcePage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
                                           @RequestParam String name, @RequestParam Integer pageElementsCount) {
        BlockPage blockPage = blockService.getBlocksByProjectAndName(pageElementsCount, pageNumber, projectId, name, BlockType.SOURCE);
        return ResponseEntity.ok(blockPage);
    }

    @GetMapping("source/acceptedSources")
    public Collection<BlockDto> getSourcesWithAcceptableReturnType(@RequestParam Long projectId, @RequestParam Multiplicity multiplicity,
                                                                                      @RequestParam Long returnTypeId){
        Collection<BlockEntity> sources = blockService.getSourcesWithAcceptableReturnType(returnTypeId, multiplicity, projectId);
        return blockMapper.map(sources);
    }

    @GetMapping("source/{sourceId}")
    public BlockDto getSourceById(@PathVariable Long sourceId){
        return blockMapper.fromEntityToDto(blockService.getBlockWithVariables(sourceId));
    }

    //    @ProjectGrant(userAction = UserAction.WRITE)
//    @GetMapping("{blockId}")
//    public BlockVariablesDto getBlock(@RequestParam Long projectId, @PathVariable Long blockId){
//        BlockEntity blockEntity = blockService.getBlockWithVariables(blockId);
//        return blockMapper.fromEntityToDto(blockEntity);
//    }
//
//    @ProjectGrant(userAction = UserAction.WRITE)
    @PostMapping("action")
    public ResponseEntity<?> saveAction(@RequestParam Long projectId, @Valid @RequestBody BlockDto dto, Errors errors) {
        return saveBlock(projectId, dto, BlockType.ACTION, errors);
    }

    @PostMapping("workflow")
    public ResponseEntity<?> saveWorkflow(@RequestParam Long projectId, @Valid @RequestBody BlockDto dto, Errors errors) {
        return saveBlock(projectId, dto, BlockType.WORKFLOW, errors);
    }

    @PostMapping("source")
    public ResponseEntity<?> saveSource(@RequestParam Long projectId, @Valid @RequestBody BlockDto dto, Errors errors) {
        return saveBlock(projectId, dto, BlockType.SOURCE, errors);
    }


    private ResponseEntity<?> saveBlock(Long projectId, BlockDto dto, BlockType type, Errors errors) {
        if (errors.hasErrors()) {
            Collection<String> validationErrors = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }
        blockService.save(dto, projectId, type);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("{blockId}")
    public void delete(@PathVariable Long blockId) {
        blockService.deleteBlock(blockId);
    }
}
