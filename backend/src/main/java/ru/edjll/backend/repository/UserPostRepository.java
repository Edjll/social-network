package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.entity.UserPost;

import java.util.Collection;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {

    @Query( "select new ru.edjll.backend.dto.post.PostDto(p.id, p.text, p.createdDate, p.modifiedDate, u.id, concat(u.firstName, ' ', u.lastName), u.username) " +
            "from UserPost p join p.user u " +
            "where p.id = :id" )
    PostDto getPostDtoById(@Param("id") Long id);

    @Query( "select new ru.edjll.backend.dto.post.PostDto(p.id, p.text, p.createdDate, p.modifiedDate, u.id, concat(u.firstName, ' ', u.lastName), u.username) " +
            "from UserPost p join p.user u " +
            "where u.id = :id " )
    Page<PostDto> getAllPostDtoByUserId(@Param("id") String id, Pageable pageable);
}
