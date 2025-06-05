package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.exception.UserNotFoundException;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import edu.uoc.epcsd.user.domain.service.DigitalSessionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DigitalSessionServiceUnitTest {

    private final Long userId = 123456L;

    private User user = User.builder().id(userId).build();;

    @Mock
    private DigitalSessionRepository digitalSessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DigitalSessionServiceImpl digitalSessionService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void whenFindDigitalSessionByExistingUser_ExpectDigitalSessionList() {

        List<DigitalSession> expectedList = new LinkedList<>();

        when(userRepository.findUserById(user.getId())).thenReturn(Optional.of(user));
        when(digitalSessionRepository.findDigitalSessionByUser(user.getId())).thenReturn(expectedList);

        List<DigitalSession> resultedList = digitalSessionService.findDigitalSessionByUser(user.getId());

        assertThat(resultedList).isEqualTo(expectedList);

        verify(userRepository, times(1)).findUserById(user.getId());
        verify(digitalSessionRepository, times(1)).findDigitalSessionByUser(user.getId());

    }

    @Test
    public void whenFindDigitalSessionByNonExistingUser_ExpectUserNotFoundException() {

        when(userRepository.findUserById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            digitalSessionService.findDigitalSessionByUser(user.getId());
        });

        verify(userRepository, times(1)).findUserById(user.getId());
        verifyNoInteractions(digitalSessionRepository);
    }

}
