package com.tuproyecto.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Si no vas a pedir el nombre en el registro, quítale el "nullable = false"
    // O asegúrate de llenarlo en el constructor.
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private TipoRol rol;

    // ✅ CONSTRUCTOR CORREGIDO
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
        this.rol = TipoRol.USER; // Por defecto, todos nacen como USER
        this.nombre = email;     // Usamos el email como nombre provisional para que no falle
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.rol == TipoRol.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    // --- Métodos de UserDetails ---
    @Override
    public String getUsername() { return email; }

    @Override
    public String getPassword() { return password; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // Definición del Enum
    public enum TipoRol {
        ADMIN,
        USER
    }
}