package ru.edjll.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.entity.Post;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query( "select new ru.edjll.backend.dto.post.PostDto(p.id, p.text, p.createdDate, p.modifiedDate, u.id, u.firstName, u.lastName, u.username) " +
            "from Post p join p.user u " +
            "where p.id = :id" )
    PostDto getPostDtoById(@Param("id") Long id);

    @Query( "select new ru.edjll.backend.dto.post.PostDto(p.id, p.text, p.createdDate, p.modifiedDate, u.id, u.firstName, u.lastName, u.username) " +
            "from Post p join p.user u " +
            "where u.id = :id " +
            "order by p.createdDate desc" )
    Collection<PostDto> getAllPostDtoByUserId(@Param("id") String id);
}
