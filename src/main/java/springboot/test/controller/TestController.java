package springboot.test.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.test.dto.Create;
import springboot.test.dto.UserDto;
import springboot.test.dto.ValidList;

@Api(tags = "Test")
@Slf4j
@RestController()
@RequestMapping("/v1/test")
public class TestController {

    @PostMapping("/listValidation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void testListValidation(@RequestBody @Validated(Create.class) ValidList<UserDto> userDtoList) {

    }
}
