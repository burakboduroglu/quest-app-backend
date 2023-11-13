package com.example.questappbackend.repos;

import com.example.questappbackend.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
