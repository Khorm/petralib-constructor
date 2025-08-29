package com.petralib.service;

import com.petralib.block.dto.BlockPage;
import com.petralib.service.dto.ServiceDto;
import com.petralib.service.dto.ServiceMapper;
import com.petralib.service.entity.ServiceEntity;
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
        BlockPage blockPage = serviceService.getServiceByProjectAndName(pageElementsCount, pageNumber, projectId, name);
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
