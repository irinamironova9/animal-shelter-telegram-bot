package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.DogInfoMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogInfoMessageRepository extends JpaRepository<DogInfoMessage, String> {
}
