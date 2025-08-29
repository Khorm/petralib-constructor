package com.petralib.type;

import com.petralib.type.dto.TypeFullDto;
import com.petralib.type.dto.TypePage;
import com.petralib.type.dto.TypeVariableDto;
import com.petralib.type.entity.TypeEntity;
import com.petralib.type.mapper.TypeMapper;
import com.petralib.type.mapper.TypeVariableMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<?> getTypesPage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
                                          @RequestParam String name, @RequestParam Integer pageElementsCount) {
//        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
//        System.out.println(securityUser.getUsername());
        TypePage typePage = typeService.getTypesPage(pageElementsCount, pageNumber, projectId, name);
        return ResponseEntity.ok(typePage);
    }

    @GetMapping
    public ResponseEntity<?> getTypes(@RequestParam Long projectId){
        return ResponseEntity.ok(typeService.getAllTypes(projectId));
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody TypeFullDto dto, @RequestParam @NotNull Long projectId, Errors errors){
        if (errors.hasErrors()) {
            Collection<String> validationErrors = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }
        try {
            TypeEntity result = typeService.save(dto, projectId);
            return ResponseEntity.ok(typeMapper.entityToDto(result));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }

    }

    @DeleteMapping("{typeId}")
    public void delete(@PathVariable Long typeId){
        typeService.delete(typeId);
    }

    @GetMapping("values/{typeId}")
    public ResponseEntity<?> getTypeValues(@PathVariable Long typeId){
        Collection<TypeVariableDto> typeVariableDtos = typeVariableMapper.map(typeService.getTypeVariables(typeId));
        return ResponseEntity.ok(typeVariableDtos);
    }
}
