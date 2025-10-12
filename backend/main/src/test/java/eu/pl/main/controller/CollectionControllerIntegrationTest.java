package eu.pl.main.controller;

import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollectionController.class)
class CollectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollectionService collectionService;

    @Test
    void getAllCollections_shouldReturnListOfCollections() throws Exception {
        UUID ownerId = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
        List<CollectionResponseDto> mockCollections = Arrays.asList(
                new CollectionResponseDto(UUID.randomUUID(), "Test Collection 1", "en", "pl", OffsetDateTime.now(), 5),
                new CollectionResponseDto(UUID.randomUUID(), "Test Collection 2", "es", "fr", OffsetDateTime.now(), 10)
        );

        when(collectionService.getAllCollectionsForUser(any(UUID.class))).thenReturn(mockCollections);

        mockMvc.perform(get("/api/v1/collections")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Collection 1"))
                .andExpect(jsonPath("$[1].cardCount").value(10));
    }

    @Test
    void getAllCollections_shouldReturnEmptyList_whenNoCollectionsExist() throws Exception {
        when(collectionService.getAllCollectionsForUser(any(UUID.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/collections")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}
