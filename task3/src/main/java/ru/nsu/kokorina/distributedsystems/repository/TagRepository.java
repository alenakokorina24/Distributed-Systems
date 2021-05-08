package ru.nsu.kokorina.distributedsystems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.kokorina.distributedsystems.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
