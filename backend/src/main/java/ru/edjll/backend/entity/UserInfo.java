package ru.edjll.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_info",
        indexes = {
                @Index(name = "user_info__city_id", columnList = "city_id"),
                @Index(name = "user_info__user_id__city_id", columnList = "user_id, city_id")
        }
)
public class UserInfo {

    @Id
    @Column(name = "user_id", length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @Column(name = "birthday")
    private LocalDate birthday;

    public UserInfo(City city, LocalDate birthday) {
        this.city = city;
        this.birthday = birthday;
    }
}
