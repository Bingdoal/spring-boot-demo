package springboot.test.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springboot.test.dto.auth.JwtPayloadDto;
import springboot.test.dto.auth.LoginDto;
import springboot.test.dto.auth.TokenDto;
import springboot.test.service.JwtTokenService;
import springboot.test.utils.HeaderUtils;

import javax.validation.Valid;

@Api(tags = "Authentication")
@Slf4j
@RestController()
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    JwtTokenService jwtTokenService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto login(@RequestBody @Valid LoginDto loginDto) {
        return jwtTokenService.generateToken(new JwtPayloadDto(loginDto.getUsername()));
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto refresh() {
        log.info("Refresh: {}", HeaderUtils.getUsername());
        return jwtTokenService.generateToken(new JwtPayloadDto(HeaderUtils.getUsername()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {

    }
}
