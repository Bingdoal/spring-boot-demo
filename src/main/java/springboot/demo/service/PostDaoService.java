package springboot.demo.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.demo.dto.PostDto;
import springboot.demo.model.dao.PostDao;
import springboot.demo.model.entity.Post;
import springboot.demo.model.entity.QPost;
import springboot.demo.model.entity.QUser;

import java.util.List;

@Service
public class PostDaoService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    public Post create(PostDto postDto) {
        Post post = new Post();
        post.setAuthorId(postDto.getAuthorId());
        post.setContent(postDto.getContent());
        return postDao.save(post);
    }

    public List<Post> findByUsername(String username) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .join(QUser.user)
                .on(QPost.post.authorId.eq(QUser.user.id))
                .where(QUser.user.name.eq(username)).fetch();
    }
}
