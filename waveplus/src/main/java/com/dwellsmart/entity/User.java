package com.dwellsmart.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long userId;

	@Column(length = 150)
    private String fullname; 
	
	@Basic(optional = false)
	@Column(nullable = false)
	private String phoneNumber;
	
	@Basic(optional = false)
	@Column(unique = true, nullable = false)
	private String username;

	@Basic(optional = false)
	@Column(nullable = false)
	private String password;

	@Basic(optional = false)
	@Column(nullable = false)
	private String email;
	
	@CreationTimestamp
	@Column(updatable = false,nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)  //,insertable = false
	private LocalDateTime updatedAt;

//	@ElementCollection(targetClass = Role.class) // Defines a collection of Role enums that will be stored in a separate table
	
//	@Enumerated(EnumType.STRING) 
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)  // orphanRemoval = true ,  cascade = CascadeType.ALL, think later
	@Builder.Default
	private Set<Role> roles = new HashSet<>(); // Default initialization
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)  // orphanRemoval = true //, cascade = CascadeType.ALL 
	@JsonManagedReference
	@BatchSize(size = 5)  // 10 devices per query
	private Set<Device> devices;
	
//     One-to-One relationship with Resident
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY )//, optional = false think later , cascade = CascadeType.PERSIST / ALL think later
    @NotFound(action = NotFoundAction.IGNORE)  // Ignore if Resident is missing
    private Resident resident;
    
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<AccountTransaction> accountTransactions;

//    @PrePersist
//    public void ensureAtLeastOneRole() {
//        if (roles == null || roles.isEmpty()) {
//            roles.add(Role.builder().role(RoleType.MANAGER).assignedAt(LocalDateTime.now()).user(this).build()) ; // Add default role
//        }
//    }
    
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
	    return Objects.equals(userId, user.userId) && 
	           Objects.equals(username, user.username);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(userId, username);
	}

}
