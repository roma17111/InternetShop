package ru.skypro.homework.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserInfoDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_table_user")
@ToString(exclude = {"comments", "ads"})
public class UserInfo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "email")
    private String email;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;


    @Column(name = "reg_date")
    private String regDate = String.valueOf(LocalDateTime.now());

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Ads> ads;

    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Comment> comments;

    public UserInfo(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public UserInfo(String email, String firstName, String lastName, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public UserInfo(String email,
                    String password,
                    String firstName,
                    String lastName,
                    String phone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public UserInfo(long id, String email, String firstName, String lastName, String phone, Role role) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }

    public UserInfo(String email, String password, String firstName, String lastName, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }

    public UserInfo(long id, String email, String firstName, String lastName, String phone, Avatar avatar) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.avatar = avatar;
    }

    private String getValidUserAvatar(UserInfo userInfo) {
        if (userInfo.getAvatar() == null) {
            return "";
        } else {
            return "/users/avatars2/" + String.valueOf(userInfo.getId());
        }
    }
    public static UserInfoDto mapToUserInfoDto(UserInfo userInfo) {
        return new UserInfoDto(userInfo.getId(),
                userInfo.getEmail(),
                userInfo.firstName,
                userInfo.lastName,
                userInfo.getPhone(),
                userInfo.getRegDate(),
                userInfo.getValidUserAvatar(userInfo));
    }

    public void addAdFromUser(Ads a) {
        ads.add(a);
    }

    public void deleteAdFromUser(Ads a) {
        ads.remove(a);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = new HashSet<>();
        roles.add(this.role);
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
