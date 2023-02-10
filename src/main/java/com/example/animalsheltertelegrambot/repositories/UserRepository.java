package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.ShelterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ShelterUser, Long> {
}
