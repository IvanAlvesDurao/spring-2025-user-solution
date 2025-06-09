package edu.uoc.epcsd.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalItemEntity;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalSessionEntity;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DigitalItemRESTControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private DigitalSessionEntity digitalSessionEntity;
    List<DigitalItem> expectedDigitalItemList;

    @BeforeEach
    public void setUp() {

        UserEntity userEntity = UserEntity.builder()
                .fullName("Ivan Alves Durao")
                .email("ialvesdurao@uoc.edu")
                .password("")
                .phoneNumber("123456")
                .build();
        entityManager.persist(userEntity);


        digitalSessionEntity = DigitalSessionEntity.builder()
                .description("Test: description")
                .location("Test: location")
                .link("Test: link")
                .user(userEntity)
                .build();
        entityManager.persist(digitalSessionEntity);

        DigitalItemEntity digitalItemEntity1 = DigitalItemEntity.builder()
                .digitalSession(digitalSessionEntity)
                .description("Test: Description 1")
                .lat(1L)
                .lon(1L)
                .link("Test: Link 1")
                .build();
        entityManager.persist(digitalItemEntity1);

        DigitalItemEntity digitalItemEntity2 = DigitalItemEntity.builder()
                .digitalSession(digitalSessionEntity)
                .description("Test: Description 2")
                .lat(2L)
                .lon(2L)
                .link("Test: Link 2")
                .build();
        entityManager.persist(digitalItemEntity2);

        DigitalItemEntity digitalItemEntity3 = DigitalItemEntity.builder()
                .digitalSession(digitalSessionEntity)
                .description("Test: Description 3")
                .lat(3L)
                .lon(3L)
                .link("Test: Link 3")
                .build();
        entityManager.persist(digitalItemEntity3);

        expectedDigitalItemList = List.of(
                digitalItemEntity1.toDomain(),
                digitalItemEntity2.toDomain(),
                digitalItemEntity3.toDomain()
        );

        entityManager.flush();
    }

    @Test
    public void whenFindDigitalItemBySessionHTTP_ExpectDigitalItemBySessionList() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/digitalItem/digitalItemBySession")
                        .param("digitalSessionId", String.valueOf(digitalSessionEntity.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<DigitalItem> returnedDigitalItemList = this.objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<DigitalItem>>() {});

        returnedDigitalItemList.forEach(System.out::println);

        assertThat(returnedDigitalItemList).isEqualTo(expectedDigitalItemList);


    }
}
