package ru.skypro.homework.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.models.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
}
