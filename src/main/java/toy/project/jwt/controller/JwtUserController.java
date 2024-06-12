package toy.project.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toy.project.jwt.dto.SignupRequest;
import toy.project.jwt.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtUserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signupRequest) {
        return userService.signup(signupRequest);
    }

}
