package edu.uoc.epcsd.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.user.application.rest.DigitalItemRESTController;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.service.DigitalItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DigitalItemRESTController.class)
@ExtendWith(MockitoExtension.class)
public class DigitalItemRESTControllerUnitTest {

    private final long digitalSessionId = 654321L;

    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DigitalItemService digitalItemService;

    DigitalItem digitalItem;

    @BeforeEach
    public void setUp() {
        long digitalItemId = 123456L;
        String description = "Description: test";
        long lat = 456123L;
        long lon = 789123L;
        String link = "www.test.com";
        digitalItem = DigitalItem.builder()
                .id(digitalItemId)
                .digitalSessionId(digitalSessionId)
                .description(description)
                .lat(lat)
                .lon(lon)
                .link(link)
                .build();

    }

    @Test
    public void whenFindDigitalItemBySession_ExpectDigitalItemList() throws Exception {

        List<DigitalItem> expectedDigitalItemList = new LinkedList<>();
        expectedDigitalItemList.add(this.digitalItem);

        when(digitalItemService.findDigitalItemBySession(digitalSessionId)).thenReturn(expectedDigitalItemList);

        MvcResult mvcResult = mockMvc.perform(get("/digitalItem/digitalItemBySession")
                .param("digitalSessionId", String.valueOf(digitalSessionId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<DigitalItem> returnedDigitalItemList = this.objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<DigitalItem>>() {});

        assertThat(returnedDigitalItemList).isEqualTo(expectedDigitalItemList);

        verify(digitalItemService, times(1)).findDigitalItemBySession(digitalSessionId);

    }
}
