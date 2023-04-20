package ru.skypro.homework.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.models.Ads;

@Repository
public interface AdsRepository extends JpaRepository<Ads,Long> {
}
