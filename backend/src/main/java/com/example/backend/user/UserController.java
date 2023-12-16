package com.example.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.ApiMessages;
import com.example.backend.ApiResponse;
import com.example.backend.security.PasswordUtils;
import com.example.backend.security.session.SessionEntity;
import com.example.backend.security.session.SessionRepository;
import com.example.backend.security.user.UserEntity;
import com.example.backend.security.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * info_response_class
     */
    public class info_response_class {
        public String userId;
        public String authorities;

        public info_response_class(String userId, String authorities) {
            this.userId = userId;
            this.authorities = authorities;
        }

    }

    @GetMapping("/info")
    public @ResponseBody ResponseEntity<ApiResponse> info() {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        var response = new ApiResponse(new info_response_class(userId, authorities), "Info about you", HttpStatus.OK);

        return response.toResponseEntity();

    }

    // @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded", params = "username")
    // public @ResponseBody ResponseEntity<ApiResponse> loginUsername(HttpServletRequest request,
    //         @RequestParam String username,
    //         @RequestParam String password, HttpSession session) {

    //     return new ApiResponse(null, "Outdated request. Please update", HttpStatus.BAD_REQUEST).toResponseEntity();
    //     // session.invalidate();
    //     // try {
    //     //     var user = userRepository.findByUsername(username);
    //     //     if (user == null) {
    //     //         return new ApiResponse(null, "Password and username doesn't match!", HttpStatus.NOT_FOUND)
    //     //                 .toResponseEntity();
    //     //     }
    //     //     if (PasswordUtils.verifyPassword(password, user.getPasswordsalt(), user.getPasswordhash()) == true) {
    //     //         var ses = request.getSession(true);
    //     //         var sessionEntity = new SessionEntity();
    //     //         sessionEntity.setJSESSIONID(ses.getId());
    //     //         sessionEntity.setUserId(user.getId());
    //     //         sessionRepository.save(sessionEntity);
    //     //         return new ApiResponse(null, "Login successful", HttpStatus.OK).toResponseEntity();
    //     //     } else {
    //     //         return new ApiResponse(null, "Password and username doesn't match!", HttpStatus.UNAUTHORIZED)
    //     //                 .toResponseEntity();
    //     //     }
    //     // } catch (Exception e) {
    //     //     System.out.println(e);
    //     //     return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
    //     //             .toResponseEntity();
    //     // }

    // }

    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<ApiResponse> loginEmail(HttpServletRequest request, @RequestParam String email,
            @RequestParam String password, HttpSession session) {
        
        session.invalidate();
        try {
            var user = userRepository.findByEmail(email);
           if (user == null) {
                return new ApiResponse(null, "Password and username doesn't match!", HttpStatus.NOT_FOUND)
                        .toResponseEntity();
            }
            if (PasswordUtils.verifyPassword(password, user.getPasswordsalt(), user.getPasswordhash()) == true) {
                var ses = request.getSession(true);
                var sessionEntity = new SessionEntity();
                sessionEntity.setJSESSIONID(ses.getId());
                sessionEntity.setUserId(user.getId());
                sessionRepository.save(sessionEntity);
                return new ApiResponse(null, "Login successful", HttpStatus.OK).toResponseEntity();
            } else {
                return new ApiResponse(null, "Password and username doesn't match!", HttpStatus.UNAUTHORIZED)
                        .toResponseEntity();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
                    .toResponseEntity();
        }
    }

    @PostMapping("/logout")
    public @ResponseBody ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpSession session) {
        session.invalidate();
        return new ApiResponse(null, "Logout successful", HttpStatus.OK).toResponseEntity();

    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<ApiResponse> register(@RequestParam(required = false) String username,
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (PasswordChecker.isPasswordStrong(password) == false) {
                return new ApiResponse(null, PasswordChecker.getPasswordRequirementsMessage(), HttpStatus.NOT_FOUND)
                        .toResponseEntity();
            }
            if (username != null) {
                if (UsernameChecker.isUsernameStrong(username) == false) {
                    return new ApiResponse(null, UsernameChecker.getUsernameRequirementsMessage(), HttpStatus.NOT_FOUND)
                            .toResponseEntity();
                }
            }
            if (EmailChecker.isEmailValid(email) == false) {
                return new ApiResponse(null, EmailChecker.getEmailValidationMessage(email), HttpStatus.NOT_FOUND)
                        .toResponseEntity();
            }
            if (username != null) {
                // checking if username already exists
                if (userRepository.findByUsername(username) != null) {
                    return new ApiResponse(null, "Username already exists. Please choose a different username.",
                            HttpStatus.NOT_FOUND).toResponseEntity();
                }
            }

            // checking if email already exists
            if (userRepository.findByEmail(email) != null) {
                return new ApiResponse(null, "Email already exists. Please choose a different email.",
                        HttpStatus.NOT_FOUND).toResponseEntity();
            }

            var salt = PasswordUtils.generateSaltAsString();
            var user = new UserEntity(username, email, PasswordUtils.hashPassword(password, salt), salt, "ROLE_USER");
            userRepository.save(user);

            return new ApiResponse(null, "New account created", HttpStatus.OK).toResponseEntity();
        } catch (Exception e) {
            System.out.println(e);
            return new ApiResponse(null, ApiMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
                    .toResponseEntity();
        }
    }
}
