package springboot.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CmdInputDto {
    private List<String> cmds;
}
