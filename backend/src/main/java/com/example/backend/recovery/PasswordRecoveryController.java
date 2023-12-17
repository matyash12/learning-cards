package com.example.backend.recovery;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.ApiMessages;
import com.example.backend.ApiResponse;
import com.example.backend.EmailMessages;
import com.example.backend.email.EmailUtil;
import com.example.backend.recovery.recoveryTokens.RecoveryTokenEntity;
import com.example.backend.recovery.recoveryTokens.RecoveryTokenRepository;
import com.example.backend.security.session.SessionRepository;
import com.example.backend.security.user.UserEntity;
import com.example.backend.security.user.UserRepository;
import com.example.backend.user.PasswordChecker;

import jakarta.websocket.server.PathParam;
import com.example.backend.security.PasswordUtils;

@RestController
@RequestMapping("/recovery")
public class PasswordRecoveryController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private EmailMessages emailMessages;

    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private PasswordResetLinkGenerator passwordResetLinkGenerator;

    // to recover password using recovery email
    @PostMapping("/request/password")
    public @ResponseBody ResponseEntity<ApiResponse> requestPassword(@RequestParam String email) {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }

        String recoveryToken = passwordResetLinkGenerator.generateRecoveryToken();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date twoHoursAhead = calendar.getTime();

        RecoveryTokenEntity recoveryTokenEntity = new RecoveryTokenEntity(user, recoveryToken, new Date(),
                twoHoursAhead);

        recoveryTokenRepository.save(recoveryTokenEntity);

        String recoveryLink = passwordResetLinkGenerator.generatePasswordResetLink(recoveryToken, user);

        emailUtil.SendEmail(user.getEmail(), emailMessages.RecoverPasswordUsingEmailsubject(),
                emailMessages.RecoverPasswordUsingEmailbody(recoveryLink));

        return new ApiResponse(null, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
    }

    @PostMapping("/change/password")
    public @ResponseBody ResponseEntity<ApiResponse> changePassword(@RequestParam String token,
            @RequestParam Long userid, @RequestParam String password) {
        var optionalUser = userRepository.findById(userid);
        if (optionalUser.isEmpty()) {
            System.out.println("User not found");
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }
        UserEntity userEntity = optionalUser.get();

        RecoveryTokenEntity recoveryTokenEntity;
        try {
            recoveryTokenEntity = recoveryTokenRepository.findByToken(token);
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }

        if (recoveryTokenEntity.getDateExpired().before(new Date()) == true){
            System.out.println("Expired on: "+recoveryTokenEntity.getDateExpired().toString());
            System.out.println("Token is expired");
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }
        if (recoveryTokenEntity.isUsed() == true){
            System.out.println("Token was already used");
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }

        if (PasswordChecker.isPasswordStrong(password) == false) {
            System.out.println("Password is not strong");
            return new ApiResponse(null, PasswordChecker.getPasswordRequirementsMessage(), HttpStatus.NOT_FOUND)
                    .toResponseEntity();
        }

        if (recoveryTokenEntity.getUserEntity().getId() == userEntity.getId()) {
            // recover password


            var salt = passwordUtils.generateSaltAsString();
            System.out.println(salt);
            userEntity.setPasswordsalt(salt);
            userEntity.setPasswordhash(passwordUtils.hashPassword(password, salt));
            userRepository.save(userEntity);
            recoveryTokenEntity.setUsed(true);
            recoveryTokenRepository.save(recoveryTokenEntity);
            System.out.println("Password changed");
            return new ApiResponse(null, ApiMessages.OK, HttpStatus.OK).toResponseEntity();
        } else {
            // NOT_FOUND - so attacker cant guess the userid
            System.out.println("Recovery token doesnt match userentuty");
            return new ApiResponse(null, ApiMessages.NOT_FOUND, HttpStatus.NOT_FOUND).toResponseEntity();
        }

    }

}
