package ru.skypro.homework.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.mdels.FullAds;

@Repository
public interface FullAdsRepository extends JpaRepository<FullAds,Long> {

}
