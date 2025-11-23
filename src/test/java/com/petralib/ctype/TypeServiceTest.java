package com.petralib.ctype;

import com.petralib.ctype.dto.TypeFullDto;
import com.petralib.ctype.entity.CTypeEntity;
import com.petralib.ctype.mapper.TypeMapper;
import com.petralib.ctype.repository.TypeRepo;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoTest
class TypeServiceTest {

    @Mock
    private TypeRepo typeRepo;

    @Mock
    private TypeMapper typeMapper;

    @InjectMocks
    private TypeService typeService;

    private TypeFullDto typeDto;

    @BeforeEach
    void setUp() {
        typeDto = new TypeFullDto();
        typeDto.setName("Test Type");
    }

    @Test
    void testSaveType() {
        // Given
        Long projectId = 1L;

        // When
        when(typeRepo.save(any(CTypeEntity.class))).thenReturn(new CTypeEntity());
        CTypeEntity result = typeService.save(typeDto, projectId);

        // Then
        assertNotNull(result);
        verify(typeRepo).save(any(CTypeEntity.class));
    }

    @Test
    void testGetTypesPage() {
        // Given
        Long projectId = 1L;
        Integer pageNumber = 0;
        String name = "test";
        Integer pageElementsCount = 10;

        // When
        var result = typeService.getTypesPage(pageElementsCount, pageNumber, projectId, name);

        // Then
        assertNotNull(result);
    }

    @Test
    void testDeleteType() {
        // Given
        Long typeId = 1L;

        // When
        typeService.delete(typeId);

        // Then
        verify(typeRepo).deleteById(typeId);
    }
}

