import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.example.exceptions.AuthException;
import org.example.models.Session;
import org.example.repositories.SessionRepository;
import org.example.services.SessionService;
import org.example.services.TokenService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void create_session_successful() throws AuthException {

        String token = "validToken";
        long userId = 1L;
        Date expirationDate = new Date(System.currentTimeMillis() + 60000);

        when(tokenService.getUserIdFromToken(token)).thenReturn(userId);
        when(tokenService.getExpirationDateFromToken(token)).thenReturn(expirationDate);


        sessionService.createSession(token);


        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository, times(1)).save(sessionCaptor.capture());
        Session capturedSession = sessionCaptor.getValue();


        assertNotNull(capturedSession);
        assertEquals(userId, capturedSession.getUserId());
        assertEquals(token, capturedSession.getToken());
        assertEquals(expirationDate, capturedSession.getExpires());
    }

    @Test
    public void create_session_auth_exception() throws AuthException {

        String token = "invalidToken";

        when(tokenService.getUserIdFromToken(token)).thenThrow(new AuthException("Invalid token"));


        assertThrows(AuthException.class, () -> sessionService.createSession(token));


        verify(tokenService, times(1)).getUserIdFromToken(token);
        verifyNoMoreInteractions(sessionRepository);
    }
}
