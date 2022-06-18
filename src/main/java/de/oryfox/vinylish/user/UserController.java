package de.oryfox.vinylish.user;

import de.oryfox.vinylish.ImageType;
import de.oryfox.vinylish.user.session.Session;
import de.oryfox.vinylish.user.session.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;

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
            user.setImageType(ImageType.DEFAULT);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public UserDto getUser(@RequestHeader String token) {
        return new UserDto(check(token));
    }

    @PutMapping("name")
    public void updateName(@RequestHeader String token, @RequestBody String name) {
        var user = check(token);
        user.setDisplayName(name);
        userRepository.save(user);
    }

    @GetMapping("image")
    @SneakyThrows
    public ResponseEntity<Object> getUserImage(@RequestParam String token) {
        var user = check(token);
        if (user.getImageType() == ImageType.CUSTOM) {
            var imgFile = new FileSystemResource("images/" + user.getId());
            return ResponseEntity.ok(new InputStreamResource(imgFile.getInputStream()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @PostMapping("image")
    public void getUserImage(@RequestParam String token, @RequestPart MultipartFile file) {
        var user = check(token);
        if (file.getContentType() == null || !file.getContentType().toLowerCase().contains("image"))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Please provide image");
        Files.write(new File("images/" + user.getId()).toPath(), file.getBytes());
        user.setImageType(ImageType.CUSTOM);
        userRepository.save(user);
    }

    @SneakyThrows
    @DeleteMapping("image")
    public void deleteImage(@RequestHeader String token) {
        var user = check(token);
        user.setImageType(ImageType.DEFAULT);
        userRepository.save(user);
        Files.deleteIfExists(new File("images/" + user.getId()).toPath());
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
