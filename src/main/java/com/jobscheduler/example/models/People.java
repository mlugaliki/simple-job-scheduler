package com.jobscheduler.example.models;

import com.jobscheduler.example.types.UserType;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class People {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String surname;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        People person = (People) o;
        return id != null && Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
