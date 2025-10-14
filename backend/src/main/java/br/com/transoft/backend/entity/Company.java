package br.com.transoft.backend.entity;

import br.com.transoft.backend.dto.CompanyDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Company {

    @Id
    @Column(name = "company_id")
    private String companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    public Company(String companyId) {
        this.companyId = companyId;
    }

    public CompanyDto toDto() {
        return CompanyDto.builder()
                .cnpj(cnpj)
                .name(name)
                .email(email)
                .build();
    }

}
