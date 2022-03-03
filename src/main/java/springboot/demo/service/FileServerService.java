package springboot.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import springboot.demo.dto.FileCreateDto;
import springboot.demo.dto.FileDto;
import springboot.demo.middleware.exception.StatusException;
import springboot.demo.utils.properties.HttpFileServerInfo;

import java.util.List;

@Service
@Slf4j
public class FileServerService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpFileServerInfo httpFileServerInfo;
    @Autowired
    private ObjectMapper objectMapper;

    public List<FileDto> getList(String path) throws StatusException {
        String url = httpFileServerInfo.getUrl() + "/" + path + "?json=true&_=" + System.currentTimeMillis();
        final HttpEntity<JsonNode> requestEntity = new HttpEntity<>(getHeaders());
        try {
            final ResponseEntity<JsonNode> responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, JsonNode.class);
            return objectMapper.convertValue(responseEntity.getBody().path("files"),
                    new TypeReference<List<FileDto>>() {
                    });
        } catch (final HttpStatusCodeException e) {
            throw new StatusException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    public FileDto getInfo(String path) throws StatusException {
        String url = httpFileServerInfo.getUrl() + "/" + path + "?op=info&_=" + System.currentTimeMillis();
        final HttpEntity<JsonNode> requestEntity = new HttpEntity<>(getHeaders());
        try {
            final ResponseEntity<JsonNode> responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, JsonNode.class);
            return objectMapper.convertValue(responseEntity.getBody(), FileDto.class);
        } catch (final HttpStatusCodeException e) {
            throw new StatusException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    public FileCreateDto upload(String path, final String filename, byte[] file) throws StatusException {
        String url = httpFileServerInfo.getUrl() + "/" + path + "?json=true&_=" + System.currentTimeMillis();
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpHeaders fileHeader = new HttpHeaders();
        fileHeader.setContentDisposition(ContentDisposition.builder("form-data")
                .name("file").filename(filename).build());
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource fileSource = new ByteArrayResource(file) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
        final HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileSource, fileHeader);
        body.add("file", fileEntity);
        body.add("filename", filename);
        final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
        try {
            final ResponseEntity<JsonNode> responseEntity = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);
            return objectMapper.convertValue(responseEntity.getBody(), FileCreateDto.class);
        } catch (final HttpStatusCodeException e) {
            throw new StatusException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    public FileCreateDto createFolder(String path) throws StatusException {
        String url = httpFileServerInfo.getUrl() + "/" + path + "?json=true&_=" + System.currentTimeMillis();
        final HttpEntity<JsonNode> requestEntity = new HttpEntity<>(getHeaders());
        try {
            final ResponseEntity<JsonNode> responseEntity = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);
            return objectMapper.convertValue(responseEntity.getBody(), FileCreateDto.class);
        } catch (final HttpStatusCodeException e) {
            throw new StatusException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    public void delete(String path) throws StatusException {
        String url = httpFileServerInfo.getUrl() + "/" + path + "?json=true&_=" + System.currentTimeMillis();
        final HttpEntity<String> requestEntity = new HttpEntity<>(getHeaders());
        try {
            final ResponseEntity<String> responseEntity = this.restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
        } catch (final HttpStatusCodeException e) {
            throw new StatusException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    public ByteArrayResource download(String path) throws StatusException {
        FileDto fileDto = getInfo(path);
        String[] tmp = path.split("[/\\\\]");
        final String filename = tmp[tmp.length - 1];
        if (fileDto.isDirectory()) {
            return new ByteArrayResource(downloadFolder(path)) {
                @Override
                public String getFilename() {
                    return filename + ".zip";
                }
            };
        }
        return new ByteArrayResource(downloadFile(path)) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }

    public byte[] downloadFile(String path) throws StatusException {
        String url = httpFileServerInfo.getUrl() + "/" + path + "?download=true&_=" + System.currentTimeMillis();
        final HttpEntity<JsonNode> requestEntity = new HttpEntity<>(getHeaders());
        try {
            final ResponseEntity<byte[]> responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);
            return responseEntity.getBody();
        } catch (final HttpStatusCodeException e) {
            throw new StatusException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    public byte[] downloadFolder(String path) throws StatusException {
        String url = httpFileServerInfo.getUrl() + "/" + path + "?op=archive&_=" + System.currentTimeMillis();
        final HttpEntity<JsonNode> requestEntity = new HttpEntity<>(getHeaders());
        try {
            final ResponseEntity<byte[]> responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);
            return responseEntity.getBody();
        } catch (final HttpStatusCodeException e) {
            throw new StatusException(e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(httpFileServerInfo.getUser(), httpFileServerInfo.getPassword());
        return httpHeaders;
    }
}
