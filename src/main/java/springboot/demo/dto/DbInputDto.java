package springboot.demo.dto;

import lombok.Data;

@Data
public class DbInputDto {
    private DBType dbType;

    public enum DBType {
        influxdb, postgresql
    }
}
