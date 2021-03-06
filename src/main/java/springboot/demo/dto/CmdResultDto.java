package springboot.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CmdResultDto {
    private List<String> cmd;
    private String result;
    private String error;
    private int exitCode;
    private boolean success;
}
