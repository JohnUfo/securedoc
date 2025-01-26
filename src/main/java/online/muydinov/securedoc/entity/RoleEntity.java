package online.muydinov.securedoc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import online.muydinov.securedoc.enumeration.Authority;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity extends Auditable {
    private String name;
    private Authority authorities;
}
