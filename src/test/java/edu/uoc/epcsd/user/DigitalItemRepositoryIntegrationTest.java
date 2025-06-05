package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalItemEntity;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalItemRepositoryImpl;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalSessionEntity;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DigitalItemRepositoryImpl.class)

public class DigitalItemRepositoryIntegrationTest {

    @Autowired
    private DigitalItemRepository digitalItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    private DigitalSessionEntity digitalSessionEntity;
    private DigitalItemEntity  digitalItemEntity1, digitalItemEntity2, digitalItemEntity3, digitalItemEntity4;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {

        userEntity = UserEntity.builder()
                .fullName("Ivan Alves Durao")
                .email("ialvesdurao@uoc.edu")
                .password("")
                .phoneNumber("123456")
                .build();
        entityManager.persistAndFlush(userEntity);


        digitalSessionEntity = DigitalSessionEntity.builder()
                .description("Test: description")
                .location("Test: location")
                .link("Test: link")
                .user(userEntity)
                .build();
        entityManager.persistAndFlush(digitalSessionEntity);

        digitalItemEntity1 = DigitalItemEntity.builder()
                .digitalSession(digitalSessionEntity)
                .description("Test: Description 1")
                .lat(1L)
                .lon(1L)
                .link("Test: Link 1")
                .build();
        entityManager.persist(digitalItemEntity1);

        digitalItemEntity2 = DigitalItemEntity.builder()
                .digitalSession(digitalSessionEntity)
                .description("Test: Description 2")
                .lat(2L)
                .lon(2L)
                .link("Test: Link 2")
                .build();
        entityManager.persist(digitalItemEntity2);

        digitalItemEntity3 = DigitalItemEntity.builder()
                .digitalSession(digitalSessionEntity)
                .description("Test: Description 3")
                .lat(3L)
                .lon(3L)
                .link("Test: Link 3")
                .build();
        entityManager.persist(digitalItemEntity3);
    }


    @Test
    public void whenfindDigitalItemBySession_ExpectDigitalItemBySessionList() {

        List<DigitalItem> digitalItemList = digitalItemRepository.findDigitalItemBySession(digitalSessionEntity.getId());

        assertThat(digitalItemList).hasSize(3);
        assert digitalItemList.contains(digitalItemEntity1.toDomain());
        assert digitalItemList.contains(digitalItemEntity2.toDomain());
        assert digitalItemList.contains(digitalItemEntity3.toDomain());
    }
}
