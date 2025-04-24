package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.AuthException;
import org.example.models.Session;
import org.example.repositories.SessionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final TokenService tokenService;

    public void createSession(String token) throws AuthException {

        Session session = new Session();
        session.setUserId(tokenService.getUserIdFromToken(token));
        session.setToken(token);
        session.setExpires(tokenService.getExpirationDateFromToken(token));

        sessionRepository.save(session);

    }


}
