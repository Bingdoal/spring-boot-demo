package springboot.test.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.test.dto.UserDto;
import springboot.test.dto.basic.ValidList;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Test")
@Slf4j
@RestController()
@RequestMapping("/v1/test")
@Validated
public class DemoController {

    @PostMapping("/listValidation")
    @ResponseStatus(HttpStatus.OK)
    @Validated(UserDto.Create.class)
    public void testListValidation(@RequestBody @Valid List<UserDto> userDtoList) {

    }

    @PostMapping("/testValidList")
    @ResponseStatus(HttpStatus.OK)
    public void testValidList(@RequestBody @Validated(UserDto.Create.class) ValidList<UserDto> userDtoList) {

    }
}
