package springboot.demo.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.demo.dto.CmdInputDto;
import springboot.demo.dto.CmdResultDto;
import springboot.demo.dto.DbInputDto;
import springboot.demo.dto.UserDto;
import springboot.demo.middleware.exception.StatusException;
import springboot.demo.service.CmdExecuteService;
import springboot.demo.service.DbDumpService;
import springboot.demo.validation.ValidList;

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
    private Validator validator;
    @Autowired
    private CmdExecuteService cmdExecuteService;
    @Autowired
    private DbDumpService dbDumpService;

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

    @PostMapping("/execCmd")
    @ResponseStatus(HttpStatus.OK)
    public CmdResultDto execCmd(@RequestBody @Valid CmdInputDto cmdInputDto) throws StatusException {
        return cmdExecuteService.execute(cmdInputDto.getCmds().toArray(new String[0]));
    }

    @PostMapping("/backupDB")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void backupDB(@RequestBody DbInputDto dbInputDto) throws StatusException {
        switch (dbInputDto.getDbType()) {
            case influxdb:
                dbDumpService.dumpInfluxdb("influx_" + System.currentTimeMillis() + ".tar");
                return;
            case postgresql:
                dbDumpService.dumpPostgres("postgresql_" + System.currentTimeMillis() + ".sql");
                return;
        }
    }
}
