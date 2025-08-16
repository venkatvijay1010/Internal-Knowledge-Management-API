package com.yourorg.kmapi.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String email;
  @Column(nullable=false) private String displayName;
  @Column(nullable=false) private Boolean active = true;
}
