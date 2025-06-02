package com.ravel.teste.srm.services;

import com.ravel.teste.srm.dto.KingdomDTO;
import com.ravel.teste.srm.entity.Kingdom;
import com.ravel.teste.srm.repository.KingdomRepository;
import com.ravel.teste.srm.service.KingdomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KingdomServiceTest {

    @Mock
    private KingdomRepository kingdomRepository;

    @InjectMocks
    private KingdomService kingdomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveKingdom_Success() throws Exception {
        KingdomDTO dto = new KingdomDTO("Lothlórien", "Elf kingdom of middle earth");

        when(kingdomRepository.existsByKingdomName("Lothlórien")).thenReturn(false);

        kingdomService.saveKingdom(dto);

        ArgumentCaptor<Kingdom> captor = ArgumentCaptor.forClass(Kingdom.class);
        verify(kingdomRepository).save(captor.capture());
        Kingdom saved = captor.getValue();

        assertEquals("Lothlórien", saved.getKingdomName());
        assertEquals("Elf kingdom of middle earth", saved.getDescription());
    }

    @Test
    void saveKingdom_AlreadyExists_ThrowsException() {
        KingdomDTO dto = new KingdomDTO("Lothlórien", "Elf kingdom of middle earth");

        when(kingdomRepository.existsByKingdomName("Lothlórien")).thenReturn(true);

        assertThrows(Exception.class, () -> kingdomService.saveKingdom(dto));

        verify(kingdomRepository, never()).save(any());
    }

    @Test
    void getAll_ReturnsKingdomList() {
        Kingdom k1 = new Kingdom(1, "Lothlórien", "Elf kingdom of middle earth");
        Kingdom k2 = new Kingdom(2, "Mordor", "Kingdom of Sauron");

        when(kingdomRepository.findAll()).thenReturn(Arrays.asList(k1, k2));

        List<Kingdom> kingdoms = kingdomService.getAll();

        assertEquals(2, kingdoms.size());
        assertEquals("Lothlórien", kingdoms.get(0).getKingdomName());
        assertEquals("Mordor", kingdoms.get(1).getKingdomName());
    }

    @Test
    void getKingdomByName_ReturnsKingdom() {
        Kingdom kingdom = new Kingdom(1, "Lothlórien", "Elf kingdom of middle earth");

        when(kingdomRepository.getKingdomByKingdomName("Lothlórien")).thenReturn(kingdom);

        Kingdom result = kingdomService.getKingdomByName("Lothlórien");

        assertNotNull(result);
        assertEquals("Lothlórien", result.getKingdomName());
    }
}
