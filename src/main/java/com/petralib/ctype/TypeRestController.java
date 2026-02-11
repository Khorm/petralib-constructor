package com.petralib.ctype;

import com.petralib.ctype.dto.CTypeFieldDto;
import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.ctype.dto.TypeFullDto;
import com.petralib.ctype.dto.TypePage;
import com.petralib.ctype.entity.CTypeEntity;
import com.petralib.ctype.mapper.TypeMapper;
import com.petralib.ctype.mapper.TypeVariableMapper;
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
@RequestMapping("/api/v1/type")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TypeRestController {

    TypeService typeService;
    TypeMapper typeMapper;
    TypeVariableMapper typeVariableMapper;

    @GetMapping("page")
    public ResponseEntity<TypePage> getTypesPage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
                                          @RequestParam String name, @RequestParam Integer pageElementsCount) {
        TypePage typePage = typeService.getTypesPage(pageElementsCount, pageNumber, projectId, name);
        return ResponseEntity.ok(typePage);
    }

    @GetMapping
    public ResponseEntity<Collection<CTypeShortDto>> getTypes(@RequestParam Long projectId){
        return ResponseEntity.ok(typeService.getAllTypes(projectId));
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody TypeFullDto dto, @RequestParam Long projectId, Errors errors){
        if (projectId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project ID is required");
        }
        if (errors.hasErrors()) {
            Collection<String> validationErrors = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
        }
        try {
            CTypeEntity result = typeService.save(dto, projectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(typeMapper.entityToDto(result));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving type: " + e.getMessage());
        }

    }

    @DeleteMapping("{typeId}")
    public void delete(@PathVariable Long typeId){
        typeService.delete(typeId);
    }

    @GetMapping("fields/{typeId}")
    public ResponseEntity<Collection<CTypeFieldDto>> getTypeValues(@PathVariable Long typeId){
        Collection<CTypeFieldDto> CTypeFieldDtos = typeVariableMapper.map(typeService.getTypeVariables(typeId));
        return ResponseEntity.ok(CTypeFieldDtos);
    }
}
