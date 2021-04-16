package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;
import ru.edjll.backend.entity.GroupUser;
import ru.edjll.backend.entity.GroupUserKey;

import javax.persistence.SqlResultSetMapping;
import javax.transaction.Transactional;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserKey> {

    @Query( "select new ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage(gu.id.user.username, gu.id.user.firstName) " +
            "from GroupUser gu " +
            "where gu.id.group.id = :groupId")
    Page<GroupUserDtoForGroupPage> getDtoByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Modifying
    @Transactional
    void deleteAllByIdGroupId(Long groupId);
}
