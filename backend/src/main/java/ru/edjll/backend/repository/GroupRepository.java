package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.group.GroupDto;
import ru.edjll.backend.dto.group.GroupDtoForSearch;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.dto.group.GroupDtoForAdminPage;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select creator_id from groups where id = :groupId", nativeQuery = true)
    Optional<String> findCreatorIdByGroupId(@Param("groupId") Long groupId);

    @Query("select new ru.edjll.backend.dto.group.GroupDto(g.id, g.address, g.title, g.description, g.creator.id) " +
            "from Group g " +
            "where g.address = :address and g.enabled = true")
    Optional<GroupDto> getDtoByAddress(@Param("address") String address);

    @Query( "select new ru.edjll.backend.dto.group.GroupDtoForSearch(" +
                "gu.id.group.id, " +
                "gu.id.group.address, " +
                "gu.id.group.title, " +
                "gu.id.group.description, " +
                "case when gr.id.user.id is null then false else true end) " +
            "from GroupUser gu " +
                "join gu.id.group g " +
                "left join g.users gr on gr.id.user.id = :authId " +
            "where gu.id.user.id = :id and g.enabled = true")
    Page<GroupDtoForSearch> getDtoByUserId(@Param("id") String id, @Param("authId") String authId, Pageable pageable);

    @Query( "select new ru.edjll.backend.dto.group.GroupDtoForSearch(" +
                "gu.id.group.id, " +
                "gu.id.group.address, " +
                "gu.id.group.title, " +
                "gu.id.group.description, " +
                "false) " +
            "from GroupUser gu join gu.id.group " +
            "where gu.id.user.id = :id")
    Page<GroupDtoForSearch> getDtoByUserId(@Param("id") String id, Pageable pageable);

    @Query("select new ru.edjll.backend.dto.group.GroupDtoForSearch(g.id, g.address, g.title, g.description, false) from Group g where g.enabled = true")
    Page<GroupDtoForSearch> getAll(Pageable pageable);

    @Query( "select new ru.edjll.backend.dto.group.GroupDtoForSearch(g.id, g.address, g.title, g.description, case when gu.id.user.id is null then false else true end) " +
            "from Group g left join g.users gu on gu.id.user.id = :userId where g.enabled = true")
    Page<GroupDtoForSearch> getAll(@Param("userId") String userId, Pageable pageable);

    @Query("select new ru.edjll.backend.dto.group.GroupDtoForAdminPage(g.id, g.title, g.description, g.address, g.enabled) from Group g")
    Page<GroupDtoForAdminPage> getAllForAdmin(Pageable pageable);

    Boolean existsByAddressAndIdNot(String address, Long id);
}
