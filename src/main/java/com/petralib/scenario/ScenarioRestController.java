package com.petralib.scenario;

import com.petralib.scenario.dto.ScenarioDto;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.dto.ScenarioVariablesDto;
import com.petralib.scenario.service.ScenarioBlockService;
import com.petralib.scenario.service.ScenarioVariablesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/scenario")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ScenarioRestController {

    ScenarioBlockService scenarioBlockService;
    ScenarioVariablesService scenarioVariablesService;

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
    public ResponseEntity<String> saveScenarioVariables(@PathVariable Long scenarioBlockId, @RequestBody Collection<ScenarioVariableDto> dtos){
        scenarioVariablesService.saveVariables(dtos, scenarioBlockId);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("{workflowId}/variables/exit")
    public ResponseEntity<ScenarioVariablesDto> getWorkflowExitVariables(@PathVariable Long workflowId){
        return ResponseEntity.ok(scenarioVariablesService.getWorkflowExitVariables(workflowId));
    }

    @PutMapping("{workflowId}/variables/exit")
    public ResponseEntity<String> saveWorkflowExitVariables(@PathVariable Long workflowId,
                                                           @RequestBody Collection<ScenarioVariableDto> dtos){
        scenarioVariablesService.saveVariablesExitWorkflow(dtos, workflowId);
        return ResponseEntity.ok("ok");
    }

}
