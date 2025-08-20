package posting.example.posting.domain;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username setter
    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    // password setter
    @Setter
    @Column(nullable = false)
    private String password;

    // role setter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // posts setter (필요 시)
    // 사용자가 작성한 게시글들
    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

    // comments setter (필요 시)
    // 사용자가 작성한 댓글들
    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    // 기본키 getter
    public Long getId() {
        return id;
    }

    // username getter
    public String getUsername() {
        return username;
    }

    // password getter
    public String getPassword() {
        return password;
    }

    // role getter
    public Role getRole() {
        return role;
    }

    // posts getter (필요 시)
    public Set<Post> getPosts() {
        return posts;
    }

    // comments getter (필요 시)
    public Set<Comment> getComments() {
        return comments;
    }

}
