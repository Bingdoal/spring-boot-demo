package springboot.demo.dto;

import lombok.Data;

@Data
public class FileDto {
    private String name;
    private String path;
    private String type;
    private String size;
    private long mtime;

    public boolean isDirectory() {
        return type != null && type.equals("dir");
    }
}
