package com.petralib.scenario;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.mapper.VariableMapper;
import com.petralib.scenario.dto.*;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.mapper.ScenarioBlockMapper;
import com.petralib.scenario.service.ScenarioBlockService;
import com.petralib.scenario.service.ScenarioVariablesService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/scenario")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ScenarioRestController {

    ScenarioBlockService scenarioBlockService;
    ScenarioVariablesService scenarioVariablesService;
    VariableMapper variableMapper;
    ScenarioBlockMapper scenarioBlockMapper;

    @GetMapping
    public ResponseEntity<ScenarioDto> getScenarioBlocksForWorkflow(@RequestParam Long workflowId) {
        ScenarioDto scenarioDto = scenarioBlockService.getScenarioBlocks(workflowId);
        return ResponseEntity.ok(scenarioDto);
    }

    @PostMapping
    public ResponseEntity<String> saveScenario(@RequestBody ScenarioDto dtos, @RequestParam Long workflowId) {
        scenarioBlockService.saveScenario(dtos, workflowId);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("{scenarioBlockId}/variables")
    public ResponseEntity<ScenarioVariablesDto> getScenarioVariables(@PathVariable Long scenarioBlockId){
        return ResponseEntity.ok(scenarioVariablesService.getVariables(scenarioBlockId));
    }

    @PostMapping("{scenarioBlockId}/variables")
    public ResponseEntity<String> saveScenarioVariables(@PathVariable Long scenarioBlockId, @RequestBody ScenarioVariablesInputDto dto){
        scenarioVariablesService.saveVariables(dto.getDtos(), scenarioBlockId, dto.getVersion());
        return ResponseEntity.ok("ok");
    }

    @GetMapping("{workflowId}/exit")
    public ResponseEntity<ScenarioBlockDto> getWorkflowExitScenario(@PathVariable Long workflowId){
        ScenarioBlockEntity scenarioBlockEntity = scenarioBlockService.getExitWorkflowScenarioBlock(workflowId);
        return ResponseEntity.ok(scenarioBlockMapper.entityToDto(scenarioBlockEntity));
    }


    @DeleteMapping("{scenarioBlockId}/variables/local/{removedId}")
    public ResponseEntity<String> deleteLocalVariable(@PathVariable Long scenarioBlockId, @PathVariable Long removedId){
        scenarioVariablesService.deleteLocalVariable(scenarioBlockId, removedId);
        return ResponseEntity.ok("ok");
    }


    @PostMapping("{scenarioBlockId}/variables/local")
    public ResponseEntity<?> saveLocalVariables(@PathVariable Long scenarioBlockId, @Valid @RequestBody List<VariableDto> variables, Errors errors){
        if (errors.hasErrors()) {
            Collection<String> validationErrors = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        } else {
            Collection<VariableEntity> savedVariables = scenarioVariablesService.saveLocalVariables(variables, scenarioBlockId);
            return ResponseEntity.ok(variableMapper.map(savedVariables));
        }
    }

}
