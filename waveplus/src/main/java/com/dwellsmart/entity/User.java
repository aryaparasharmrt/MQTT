package com.dwellsmart.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	private String email;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

//	@ElementCollection(targetClass = Role.class) // Defines a collection of Role enums that will be stored in a separate table
	
	@Enumerated(EnumType.STRING) // Specifies that the enum values should be stored as strings in the database
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)  // orphanRemoval = true -> think later
	@Builder.Default
	private Set<Role> roles = new HashSet<>(); // Default initialization
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Set<Device> devices;
	
    // One-to-One relationship with Resident
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
    private Resident resident;

	public void addRole(Role role) {
		if (role.getUser() != this) { // Prevents circular reference if already set
			roles.add(role); // Add enum value to the Set
			role.setUser(this); // Set back reference
		}
	}

	public void removeRole(Role role) {
        roles.remove(role); // Remove enum value from the Set
        role.setUser(null); // Clear back reference
    }
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    User user = (User) o;
	    return Objects.equals(id, user.id) && 
	           Objects.equals(username, user.username);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id, username);
	}

}
