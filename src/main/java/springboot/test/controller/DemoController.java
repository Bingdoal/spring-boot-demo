package springboot.test.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.test.dto.UserDto;
import springboot.test.dto.basic.ValidList;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Api(tags = "Test")
@Slf4j
@RestController()
@RequestMapping("/v1/test")
@Validated
public class DemoController {
    @Autowired
    Validator validator;

    @PostMapping("/listValidation")
    @ResponseStatus(HttpStatus.OK)
    @Validated(UserDto.Create.class)
    public void testListValidation(@RequestBody @Valid List<UserDto> userDtoList) {

    }

    @PostMapping("/testValidList")
    @ResponseStatus(HttpStatus.OK)
    public void testValidList(@RequestBody @Validated(UserDto.Create.class) ValidList<UserDto> userDtoList) {

    }

    @PostMapping("/validManual")
    @ResponseStatus(HttpStatus.OK)
    public void validManual(@RequestBody UserDto userDto) {
        Set<ConstraintViolation<UserDto>> validateSet = validator.validate(userDto);
        if (!validateSet.isEmpty()) {
            for (ConstraintViolation<UserDto> violation : validateSet) {
                log.info("violation message: {}", violation.getMessage());
            }
        }
    }
}
