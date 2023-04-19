package ru.skypro.homework.mdels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "email")
    private String email;
    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    @Column(name = "reg_date")
    private String regDate = String.valueOf(LocalDateTime.now());

    @Column(name = "image")
    private byte[] image;

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "id",joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();


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

    public void addRole(RegisterReqDto reqDto) {
        roles.add(reqDto.getRole());
    }
}
