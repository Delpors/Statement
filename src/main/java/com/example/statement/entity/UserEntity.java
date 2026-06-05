package com.example.statement.entity;

import com.example.statement.util.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "enail", unique = true)
    private String email;

    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "created_at")
    private LocalDateTime created;

    @Column(name = "deleted_at")
    private LocalDateTime deleted;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InstitutionEntity> institution = new ArrayList<>();

    private boolean isActive = true;

    @PrePersist
    public void prePersist() {
        this.created = LocalDateTime.now();
    }
}
