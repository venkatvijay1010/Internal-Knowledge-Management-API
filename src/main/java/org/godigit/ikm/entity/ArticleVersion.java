package org.godigit.ikm.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="article_versions", uniqueConstraints=@UniqueConstraint(columnNames={"article_id","version"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ArticleVersion {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="article_id", nullable=false) private Article article;
  @Column(nullable=false) private Integer version;
  @Column(nullable=false) private String title;
  @Column(nullable=false, columnDefinition="text") private String body;
}
