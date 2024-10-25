package com.project.CustomerProject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Builder.Default
    @Column(nullable = false)
    private boolean isAccountNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean isAccountNonLocked = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean isCredentialsNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean isEnabled = true;

    public User(String username, String password, Collection<Role> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private Collection<Role> authorities;

    public boolean checkAuthority(String role) {
        for (Role role1 : authorities) {
            if (role1.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
