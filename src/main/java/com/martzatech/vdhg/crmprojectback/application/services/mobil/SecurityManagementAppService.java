package com.martzatech.vdhg.crmprojectback.application.services.mobil;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMobilMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserMobil;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserNew;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserMobilService;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import com.martzatech.vdhg.crmprojectback.infrastructure.configs.Jwt;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@AllArgsConstructor
@Slf4j
@Service
public class SecurityManagementAppService {
    private final Jwt jwt;
    private UserService userService;
    private final UserMobilService mobilService;
    private final UserMobilMapper mobilMapper;

    public User findCurrentUser(String token) {
        try {
            log.info("Inf Curren User ");
            return jwt.findCurrentUser(token);
        }catch (ExpiredJwtException e){
            throw new ExpiredJwtException(null, null, "Token expirado");
        }
    }

    @Transactional
    public UserNew validateUserLoginApp(final String userName, final String password) {
        log.info("Validate User Mobil App ( " + userName + " )");
        final String resultPassword = generateSHA256Hash(password);
        return mobilService.verifyPassword(userName, resultPassword);
    }

    public UserMobil saveUserMobil(final UserMobil model) {
        //return mobilService.save(buildUserMobil(model));
        return mobilService.save(model);
    }

    /* private UserMobil buildUserMobil(UserMobil model) {
         final UserMobil user = Objects.nonNull(model.getId())
                 ? mobilService.findById(model.getId())
                 .withPassword(generateSHA256Hash(model.getPassword()))
                 .withPasswordUpdateRequired(true)
                 .withUser(model.getUser())
                 .withCreationTime(LocalDateTime.now())
                 : mobilService.save(model);
         return user
                 .withPassword(user.getPassword())
                 .withPasswordUpdateRequired(user.isPasswordUpdateRequired())
                 .withUser(user.getUser())
                 .withCreationTime(LocalDateTime.now());
     }*/
    public static String generateSHA256Hash(String password) {
        try {
            // Obtén una instancia de MessageDigest con el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convierte la contraseña en bytes
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // Calcula el hash SHA-256 de los bytes de la contraseña
            byte[] hashBytes = digest.digest(passwordBytes);

            // Convierte el hash en una representación hexadecimal
            StringBuilder hexHash = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserById(final Integer id, final String token) {
        log.info("findUserById User Mobil ID : {}", id);
        validateToken(token);
        User user = userService.findById(id);
        validateTypeUser(user);
        return user;
    }

    private void validateTypeUser(final User user) {
        if (user.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())
                || user.getTypeUser().equals(UserTypeEnum.ASSOCIATE.name())) {
            log.info("Type User Valid Id:{}", user.getId() + " and TypeUser :" + user.getTypeUser());
            return;
        }
        throw new BusinessRuleException("The User type is not customer or Associate.");
    }

    public boolean validateToken(String token) {
        String userID = null;
        try {
          ResponseEntity<String> tokenExtra = jwt.getKey(token);
            userID =   tokenExtra.getBody();
        }catch (BusinessRuleException e){
             new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return userID != null ? true : false;
    }

   /* public UserNew updateLastLoginAndGetCurrentUser(String token) {
        final User user = findCurrentUser(token);
        user.withLastLogin(LocalDateTime.now());
        mobilService.save(UserMobil
                .builder()
                        .lastLogin()
                        .user()
                        .
                .build());
        return null ;
    }*/

}
