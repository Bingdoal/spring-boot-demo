package springboot.demo.graphql.operation.demo;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class DemoMutation implements GraphQLMutationResolver {

    public LocalDateTime addOneDay(LocalDateTime dateTime) {
        return dateTime.plusDays(1);
    }

    public String uploadFile(List<Part> files, DataFetchingEnvironment env) throws IOException {
        List<Part> attachmentParts = env.getArgument("files");
        for (Part upload : attachmentParts) {
            log.info("getName: {}", upload.getName());
            log.info("getSubmittedFileName: {}", upload.getSubmittedFileName());
            for (String headerName : upload.getHeaderNames()) {
                log.info("{}: {}", headerName, upload.getHeader(headerName));
            }
            upload.write(upload.getSubmittedFileName());
            String content = new String(upload.getInputStream().readAllBytes());

            log.info("Content: {}", content);
            return content;
        }
        return null;
    }
}
