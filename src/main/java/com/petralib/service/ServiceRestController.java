package com.petralib.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.file.model.ConstructorModel;
import com.petralib.service.dto.ServiceDto;
import com.petralib.service.dto.ServiceMapper;
import com.petralib.service.dto.ServicePage;
import com.petralib.service.entity.ServiceEntity;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/service")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceRestController {

    ServiceService serviceService;
    ServiceMapper serviceMapper;


    //    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping("page")
    public ResponseEntity<?> getServicePage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
                                            @RequestParam String name, @RequestParam Integer pageElementsCount) {
        ServicePage blockPage = serviceService.getServiceByProjectAndName(pageElementsCount, pageNumber, projectId, name);
        return ResponseEntity.ok(blockPage);
    }

    //    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping
    public ResponseEntity<?> getServices(@RequestParam Long projectId) {
        Collection<ServiceEntity> serviceEntities = serviceService.getServices(projectId);
        System.out.println(serviceEntities);
        Collection<ServiceDto> serviceDtos = serviceMapper.map(serviceEntities);
        return ResponseEntity.ok(serviceDtos);
    }

    //    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{serviceId}")
    public ServiceDto getService(@PathVariable Long serviceId) {
        ServiceEntity serviceEntity = serviceService.getService(serviceId);
        return serviceMapper.fromEntityToDto(serviceEntity);
    }

    @GetMapping("file/{serviceId}")
    public ResponseEntity<ByteArrayResource> getFIle(@PathVariable Long serviceId) {
        ConstructorModel constructorModel = serviceService.createConstructorModel(serviceId);

        // 2. Конвертируем в JSON
        String jsonData;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.writeValueAsString(constructorModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error generating JSON", e);
        }

        // 3. Создаем ресурс для скачивания
        byte[] bytes = jsonData.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(bytes);

        // 4. Формируем ответ с заголовками для скачивания
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data_export.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(bytes.length)
                .body(resource);
    }

    //    @ProjectGrant(userAction = UserAction.WRITE)
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody ServiceDto serviceDto, Errors errors) {
        if (errors.hasErrors()) {
            Collection<String> validationErrors = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        serviceService.saveService(serviceDto);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("{serviceId}")
    public ResponseEntity<?> delete(@PathVariable Long serviceId) {
        serviceService.deleteService(serviceId);
        return ResponseEntity.ok(serviceId);
    }
}
