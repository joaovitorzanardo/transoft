package br.com.transoft.backend.entity;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.login.UserAccountDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_account")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserAccount {

    @Id
    @Column(name = "user_account_id")
    private String userAccountId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserAccountDto toDto() {
        return new UserAccountDto(this.name, this.email, this.active, this.role.getRole());
    }

}
