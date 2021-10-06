package springboot.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springboot.test.dto.JwtPayloadDto;
import springboot.test.dto.auth.LoginDto;
import springboot.test.dto.auth.TokenDto;
import springboot.test.service.JwtTokenService;
import springboot.test.utils.HeaderUtil;

import javax.validation.Valid;

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
        log.info("Refresh: {}", HeaderUtil.getUsername());
        return jwtTokenService.generateToken(new JwtPayloadDto(HeaderUtil.getUsername()));
    }

}
