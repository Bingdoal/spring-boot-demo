package springboot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springboot.demo.dto.CmdResultDto;
import springboot.demo.middleware.exception.RestException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CmdExecuteService {
    public CmdResultDto execute(String... cmds) throws RestException {
        CmdResultDto cmdResultDto = new CmdResultDto();
        List<String> cmdList = new ArrayList<>();
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            cmdList.add("cmd.exe");
            cmdList.add("/c");
        } else {
            cmdList.add("/bin/bash");
            cmdList.add("-c");
        }
        cmdList.addAll(List.of(cmds));
        cmdResultDto.setCmd(cmdList);
        try {
            Process process = Runtime.getRuntime().exec(cmdList.toArray(new String[0]));
            log.info("CMD: {}", String.join(" ", cmds));

            StringBuilder output = new StringBuilder();
            BufferedReader outReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = outReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            cmdResultDto.setResult(output.toString().trim());
            log.info("output: {}", cmdResultDto.getResult());
            cmdResultDto.setExitCode(process.waitFor());

            cmdResultDto.setSuccess(cmdResultDto.getExitCode() == 0);
            if (!cmdResultDto.isSuccess()) {
                BufferedReader errReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                StringBuilder error = new StringBuilder();
                while ((line = errReader.readLine()) != null) {
                    error.append(line).append("\n");
                }
                cmdResultDto.setError(error.toString().trim());
                log.info("error: {}", cmdResultDto.getError());
            }

            return cmdResultDto;
        } catch (Exception e) {
            log.error("CMD: {} => error: {}", String.join(" ", cmds), e.getMessage(), e);
            throw new RestException(500, e.getMessage());
        }
    }
}
