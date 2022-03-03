package springboot.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.demo.dto.FileCreateDto;
import springboot.demo.dto.FileDto;
import springboot.demo.middleware.exception.StatusException;
import springboot.demo.service.FileServerService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/v1/file")
public class FileServerController {
    @Autowired
    private FileServerService fileServerService;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<FileDto> listPath(@RequestParam(value = "path", defaultValue = "/") String path) throws StatusException {
        return fileServerService.getList(path);
    }

    @PostMapping("/createFolder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public FileCreateDto createFolder(@RequestParam(value = "path") String path) throws StatusException {
        return fileServerService.createFolder(path);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public FileCreateDto uploadFile(@RequestParam(value = "path") String path,
                                    @RequestPart(value = "filename", required = false) String filename,
                                    @RequestPart(value = "file") MultipartFile file) throws StatusException {
        try {
            String uploadName = file.getOriginalFilename();
            if (filename != null) {
                uploadName = filename;
            }

            return fileServerService.upload(path, uploadName, file.getBytes());
        } catch (IOException e) {
            throw new StatusException(400, "File upload failed");
        }
    }

    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePath(@RequestParam(value = "path") String path) throws StatusException {
        fileServerService.delete(path);
    }

    @GetMapping("/download")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> download(@RequestParam(value = "path") String path) throws StatusException {
        ByteArrayResource resource = fileServerService.download(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .name("filename").filename(resource.getFilename()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
