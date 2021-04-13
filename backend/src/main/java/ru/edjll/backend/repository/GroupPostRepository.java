package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.group.post.GroupPostDtoForGroupPage;
import ru.edjll.backend.entity.GroupPost;

import java.util.Optional;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {

    @Query( "select new ru.edjll.backend.dto.group.post.GroupPostDtoForGroupPage(gp.id, gp.text, gp.createdDate, gp.modifiedDate, gp.group.creator.id) " +
            "from GroupPost gp " +
            "where gp.group.id = :groupId " +
            "order by gp.modifiedDate, gp.createdDate desc")
    Page<GroupPostDtoForGroupPage> getDtoByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Query("select new ru.edjll.backend.dto.group.post.GroupPostDtoForGroupPage(gp.id, gp.text, gp.createdDate, gp.modifiedDate, gp.group.creator.id) from GroupPost gp where gp.id = :id")
    Optional<GroupPostDtoForGroupPage> getDtoById(@Param("id") Long id);
}
