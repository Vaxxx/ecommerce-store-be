package ng.com.createsoftware.ecommercestorebe.service;

import jakarta.transaction.Transactional;
import ng.com.createsoftware.ecommercestorebe.api.dto.LoginBody;
import ng.com.createsoftware.ecommercestorebe.api.dto.RegistrationBody;
import ng.com.createsoftware.ecommercestorebe.exception.EmailFailureException;
import ng.com.createsoftware.ecommercestorebe.exception.UserAlreadyExistsException;
import ng.com.createsoftware.ecommercestorebe.exception.UserNotVerifiedException;
import ng.com.createsoftware.ecommercestorebe.model.LocalUser;
import ng.com.createsoftware.ecommercestorebe.model.VerificationToken;
import ng.com.createsoftware.ecommercestorebe.model.repository.LocalUserDAO;
import ng.com.createsoftware.ecommercestorebe.model.repository.VerificationTokenDAO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;

    private EmailService emailService;
    private VerificationTokenDAO verificationTokenDAO;


    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService,
                       JWTService jwtService, EmailService emailService,
                       VerificationTokenDAO verificationTokenDAO) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenDAO = verificationTokenDAO;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {

        if(localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
            && localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
        ) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registrationBody.getUsername());

        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        user.setEmail(registrationBody.getEmail() );
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        //check verification before saving
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
       // verificationTokenDAO.save(verificationToken);
        return localUserDAO.save(user);
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> optUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
//        if(optUser.isPresent() && encryptionService.verifyPassword(loginBody.getPassword(), optUser.get().getPassword())){
//            return optUser.get().getUsername();
//        }
        if (optUser.isPresent()) {
            LocalUser user = optUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){
               if(user.getEmailVerified())
                  return jwtService.generateJWT(user);
               else{
                   List<VerificationToken> verificationTokens = user.getVerificationTokens();
                                     //if there is no verificationToken
                   Boolean resend = verificationTokens.size() == 0 ||
                   //check the first(zero) verificationToken in the DB
                           verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));//1 hour
                   if(resend){
                       VerificationToken verificationToken = createVerificationToken(user);
                       verificationTokenDAO.save(verificationToken);
                       emailService.sendVerificationEmail(verificationToken);
                   }
                  throw  new UserNotVerifiedException(resend);
               }
            }
        }
        return null;
    }

    private VerificationToken createVerificationToken(LocalUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }
//transactional is used when changing db(updating) and not querying it
    //with transactional, you are telling springboot that you are changing data
    ///and it should create a proper section for it
  @Transactional
    public boolean verifyUser(String token) {
         //first verify token exist in DB
        Optional<VerificationToken> optToken = verificationTokenDAO.findByToken((token));
        if(optToken.isPresent()){
            VerificationToken verificationToken = optToken.get();
            //get the user of the existing token
            LocalUser user = verificationToken.getUser();
            if(!user.getEmailVerified()){
                user.setEmailVerified(true);//set this field in the db
                localUserDAO.save(user);//and save user in db
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

}
