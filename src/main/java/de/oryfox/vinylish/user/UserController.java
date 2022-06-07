package de.oryfox.vinylish.user;

import de.oryfox.vinylish.user.session.Session;
import de.oryfox.vinylish.user.session.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {
    final UserRepository userRepository;
    final SessionRepository sessionRepository;

    @PostMapping
    public void createUser(@RequestBody User user) {
        var opt = userRepository.findByEmail(user.getEmail());
        if (opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (user.getEmail() != null && !user.getEmail().equals("") && user.getPassword() != null && !user.getPassword().equals("") && user.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public void alterUser(@RequestHeader String token, @RequestBody User req) {
        var user = check(token);
        if (req.getEmail() != null)
            user.setEmail(req.getEmail());
        if (req.getPassword() != null)
            user.setEmail(new BCryptPasswordEncoder().encode(req.getPassword()));
        if (req.getDisplayName() != null)
            user.setDisplayName(req.getDisplayName());
        userRepository.save(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader String token) {
        var user = check(token);
        user.setDisabled(true);
        userRepository.save(user);
        sessionRepository.deleteAllByUser(user);
    }

    public User check(String token) {
        return sessionRepository.findUserByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("login")
    public String login(@RequestBody User user) {
        var opt = userRepository.findByEmail(user.getEmail());
        if (opt.isPresent() && !opt.get().isDisabled() && new BCryptPasswordEncoder().matches(user.getPassword(), opt.get().getPassword())) {
            var checkSession = sessionRepository.findByUser(opt.get());
            if (checkSession.isPresent()) {
                return checkSession.get().getToken();
            }

            var session = new Session(opt.get());
            return sessionRepository.save(session).getToken();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("logout")
    public void logout(@RequestHeader String token) {
        var session = sessionRepository.findByToken(token);
        if (session.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        sessionRepository.delete(session.get());
    }
}
