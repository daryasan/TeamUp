import org.example.dto.RegisterDto;
import org.example.services.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.event.annotation.BeforeTestClass;


public class AuthServiceTest {

    @InjectMocks
    AuthService authService;
    Authentication authentication;
    SecurityContext securityContext;

    @BeforeTestClass
    public void init() {
        //authService = new AuthService();
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void register() {
        RegisterDto successfulCaseParticipant =
                new RegisterDto(1, "mr_beast", "mr_beast@gmail.com", "Мистер", "Бист", "Мистербистович", "mrBeast123");

    }

//    RegisterDto successfulCaseMentor =
//            new RegisterDto(2, "strykalo", "mval_stry@gmail.com", "Юрий", "Каплан", "Стрыкалович", "1234Strykalo");
//    RegisterDto successfulCaseOrganizer =
//            new RegisterDto(3, "ivan", "ivan@gmail.com", "Иван", "Иванов", "Иванович", "ИванИванИван1");
//    RegisterDto wrongNickname1 =
//            new RegisterDto(1, "иван     00", "mr_beast1@gmail.com", "Мистер", "Бист", "Мистербистович", "mrBeast123");
//    RegisterDto wrongNickname2 =
//            new RegisterDto(1, "ivan!0", "mr_beast2@gmail.com", "Мистер", "Бист", "Мистербистович", "mrBeast123");
//    RegisterDto sameNickname =
//            new RegisterDto(1, "strykalo", "mr_beast3@gmail.com", "Мистер", "Бист", "Мистербистович", "mrBeast123");
//    RegisterDto wrongEmail1 =
//            new RegisterDto(1, "mr_beast1", "mr_beastgmail.com", "Мистер", "Бист", "Мистербистович", "mrBeast123");
//    RegisterDto wrongEmail2 =
//            new RegisterDto(1, "mr_beast2", "фф@gmailcom", "Мистер", "Бист", "Мистербистович", "mrBeast123");
//    RegisterDto sameEmail =
//            new RegisterDto(1, "aaaa_aa", "mr_beast@gmail.com", "Мистер", "Бист", "Мистербистович", "mrBeast123");
//    RegisterDto wrongPassword1 =
//            new RegisterDto(1, "mr_beast3", "mr_beast4@gmail.com", "Мистер", "Бист", "Мистербистович", "qwerty");
//    RegisterDto wrongPassword2 =
//            new RegisterDto(1, "mr_beast4", "mr_beast5@gmail.com", "Мистер", "Бист", "Мистербистович", "qwerty123");
//    RegisterDto emptyFirstName =
//            new RegisterDto(1, "mr_beast5", "mr_beast6@gmail.com", "", "Бист", "Мистербистович", "mrBeast123");
//    RegisterDto emptyLastName =
//            new RegisterDto(1, "mr_beast6", "mr_beas7@gmail.com", "Мистер", "", "Мистербистович", "mrBeast123");
//    RegisterDto emptyMiddleName =
//            new RegisterDto(1, "mr_beast7", "mr_beast8@gmail.com", "Мистер", "Бист", "", "mrBeast123");
//    RegisterDto emptyEmail =
//            new RegisterDto(1, "mr_beast8", "", "Мистер", "Бист", "", "mrBeast123");
//    RegisterDto emptyNickname =
//            new RegisterDto(1, "", "mr_beast9@gmail.com", "Мистер", "Бист", "", "mrBeast123");
//    RegisterDto emptyPassword =
//            new RegisterDto(1, "mr_beast9", "mr_beast10@gmail.com", "Мистер", "Бист", "", "mrBeast123");
//    RegisterDto emptyAll =
//            new RegisterDto(1, "", "", "", "", "", "");



}
