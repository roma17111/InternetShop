package ru.skypro.homework.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.mdels.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Long> {
}
