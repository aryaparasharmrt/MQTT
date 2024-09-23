package com.dwellsmart.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.dwellsmart.constants.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;

	// Many-to-One relationship with User
	@ManyToOne(fetch = FetchType.LAZY) // Many roles can be assigned to one user
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore // Prevent infinite recursion during serialization
	private User user;

	@Enumerated(EnumType.STRING) // Enum will be saved as a string in DB
	@Column(name = "role", nullable = false)
	private RoleType role;

	@Column(nullable = false)
	private LocalDateTime assignedAt;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = true)
	private Project project; // Nullable for BAM or global roles
	
	
	 // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role userRole = (Role) o;
        return Objects.equals(roleId, userRole.roleId) && Objects.equals(role, userRole.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, role);  // Avoid recursive access of user
    }

}
