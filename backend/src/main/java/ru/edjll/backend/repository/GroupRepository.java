package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.group.GroupDto;
import ru.edjll.backend.dto.group.GroupDtoForSearch;
import ru.edjll.backend.dto.group.GroupDtoForUserPage;
import ru.edjll.backend.entity.Group;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select creator_id from groups where id = :groupId", nativeQuery = true)
    Optional<String> findCreatorIdByGroupId(@Param("groupId") Long groupId);

    @Query("select new ru.edjll.backend.dto.group.GroupDto(g.id, g.address, g.title, g.description, g.creator.id) from Group g where g.address = :address")
    Optional<GroupDto> getDtoByAddress(@Param("address") String address);

    @Query( "select new ru.edjll.backend.dto.group.GroupDtoForUserPage(gu.id.group.address, gu.id.group.title) " +
            "from GroupUser gu " +
            "where gu.id.user.id = :userId")
    Page<GroupDtoForUserPage> getDtoByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("select new ru.edjll.backend.dto.group.GroupDtoForSearch(g.address, g.title, g.description) from Group g")
    Page<GroupDtoForSearch> getAll(Pageable pageable);
}
