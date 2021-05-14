package ru.edjll.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "city",
        indexes = {
                @Index(name = "city_index__id__country_id", columnList = "id, country_id")
        }
)
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @OneToMany(mappedBy = "city", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<UserInfo> users;

    public City(Long id) {
        this.id = id;
    }
}
