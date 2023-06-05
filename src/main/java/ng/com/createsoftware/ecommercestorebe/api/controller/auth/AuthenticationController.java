package ng.com.createsoftware.ecommercestorebe.api.controller.auth;

import jakarta.validation.Valid;
import ng.com.createsoftware.ecommercestorebe.api.dto.LoginBody;
import ng.com.createsoftware.ecommercestorebe.api.dto.LoginResponse;
import ng.com.createsoftware.ecommercestorebe.api.dto.RegistrationBody;
import ng.com.createsoftware.ecommercestorebe.exception.EmailFailureException;
import ng.com.createsoftware.ecommercestorebe.exception.UserAlreadyExistsException;
import ng.com.createsoftware.ecommercestorebe.exception.UserNotVerifiedException;
import ng.com.createsoftware.ecommercestorebe.model.LocalUser;
import ng.com.createsoftware.ecommercestorebe.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

     @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
         String jwt = null;
         try {
             jwt = userService.loginUser(loginBody);
         } catch (UserNotVerifiedException e) {
              LoginResponse response = new LoginResponse();
              response.setSuccess(false);
              String reason = "USER_NOT_VERIFIED";
              if(e.isNewEmailSent())
                  reason += "_EMAIL_RESENT";
              response.setFailureReason(reason);
              return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
         }
         if(jwt==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        else{
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
     }

     @GetMapping("/login")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user ){
         return user;
     }

     @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token){
        if(userService.verifyUser(token))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

     }
}
