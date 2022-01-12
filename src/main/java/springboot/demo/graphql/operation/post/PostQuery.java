package springboot.demo.graphql.operation.post;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import springboot.demo.dto.basic.PageDto;
import springboot.demo.dto.basic.PageResultDto;
import springboot.demo.model.dao.PostDao;
import springboot.demo.model.entity.Post;

import java.util.List;

@Component
@Slf4j
public class PostQuery implements GraphQLQueryResolver {
    @Autowired
    private PostDao postDao;

    public List<Post> getPostList() {
        return postDao.findAll();
    }

    public Post getPost(Long id) {
        return postDao.findById(id).orElse(null);
    }

    public PageResultDto<Post> pagePost(PageDto pageDto) {
        Pageable pageable = Pageable.ofSize(pageDto.getSize()).withPage(pageDto.getPage() - 1);
        return new PageResultDto<>(postDao.findAll(pageable));
    }

    public PageResultDto<Post> postFilter(PostFilter filter, PageDto pageDto) {

        return null;
    }

}
