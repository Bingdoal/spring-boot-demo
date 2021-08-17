package springboot.test.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import springboot.test.model.entity.EntityBase;

import java.io.Serializable;

@NoRepositoryBean
public interface DaoBase<T extends EntityBase, ID extends Serializable> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
}
