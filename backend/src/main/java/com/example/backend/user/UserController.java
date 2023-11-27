package com.example.backend.user;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.security.PasswordUtils;
import com.example.backend.security.UserToken;
import com.example.backend.security.session.SessionEntity;
import com.example.backend.security.session.SessionRepository;
import com.example.backend.security.user.UserEntity;
import com.example.backend.security.user.UserRepository;

import jakarta.servlet.http.Cookie;
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

    @GetMapping("/info")
    public @ResponseBody ResponseEntity<String> info() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        return new ResponseEntity<>(userId + "   " + authorities, HttpStatus.OK);

    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<String> login(HttpServletRequest request, @RequestParam String username,
            @RequestParam String password, HttpSession session) {
        session.invalidate();
        try {
            var user = userRepository.findByUsername(username);
            if (PasswordUtils.verifyPassword(password, user.getPasswordsalt(), user.getPasswordhash()) == true) {
                var ses = request.getSession(true);
                var sessionEntity = new SessionEntity();
                sessionEntity.setJSESSIONID(ses.getId());
                sessionEntity.setUserId(user.getId());
                sessionRepository.save(sessionEntity);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Password and username doesn't match!", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/logout")
     public @ResponseBody ResponseEntity<String> logout(HttpServletRequest request, HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<String> register(@RequestParam String username, @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (PasswordChecker.isPasswordStrong(password) == false) {
                return new ResponseEntity<>(PasswordChecker.getPasswordRequirementsMessage(), HttpStatus.NOT_FOUND);
            }
            if (UsernameChecker.isUsernameStrong(username) == false) {
                return new ResponseEntity<>(UsernameChecker.getUsernameRequirementsMessage(), HttpStatus.NOT_FOUND);
            }

            // checking if username already exists
            if (userRepository.findByUsername(username) != null) {
                return new ResponseEntity<>("Username already exists. Please choose a different username.", HttpStatus.NOT_FOUND);
            }

            var newUser = new UserEntity();
            newUser.setRoles("ROLE_USER");
            newUser.setUsername(username);
            var salt = PasswordUtils.generateSaltAsString();
            newUser.setPasswordsalt(salt);
            newUser.setPasswordhash(PasswordUtils.hashPassword(password, salt));

            userRepository.save(newUser);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
