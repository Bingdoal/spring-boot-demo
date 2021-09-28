package springboot.test.model.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    protected Long id;
    @CreationTimestamp
    protected LocalDateTime creationTime;
    @UpdateTimestamp
    protected LocalDateTime modificationTime;
}
