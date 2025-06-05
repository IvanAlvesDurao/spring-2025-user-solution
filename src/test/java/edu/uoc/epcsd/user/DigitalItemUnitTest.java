package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DigitalItemUnitTest {

    @Test
    public void whenItemIsCreate_ExpectedIsAvailableByDefault(){
        DigitalItem digitalItemNoArgs = new DigitalItem();
        DigitalItem digitalItemWithArgs = DigitalItem.builder()
                .id(1L)
                .digitalSessionId(123L)
                .description("Description: test")
                .lat(123456L)
                .lon(654321L)
                .link("digitalItemTest.edu")
                .build();
        assertThat(digitalItemNoArgs.getStatus()).isEqualTo(DigitalItemStatus.AVAILABLE);
        assertThat(digitalItemWithArgs.getStatus()).isEqualTo(DigitalItemStatus.AVAILABLE);
    }
}
