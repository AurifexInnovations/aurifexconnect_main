package com.erp.Model;

import com.erp.Enum.AssignModule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User implements GenericUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_no")
    private long phoneNo;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "created_by_admin_id")
    private long createdByAdminId;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDate lastModifiedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();


    @ElementCollection(targetClass = AssignModule.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_modules", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "module")
    private Set<AssignModule> modules = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @OneToMany(mappedBy = "user")
    private List<Salary> salaries;

    @OneToMany(mappedBy = "user")
    private List<Leave> leaves;

    @OneToMany(mappedBy = "user")
    private List<Attendance> attendances;
}
