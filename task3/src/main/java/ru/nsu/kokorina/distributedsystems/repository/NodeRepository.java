package ru.nsu.kokorina.distributedsystems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nsu.kokorina.distributedsystems.model.NodeEntity;

import java.util.List;

public interface NodeRepository extends JpaRepository<NodeEntity, Long> {

    NodeEntity findById(long id);

    @Query(value = "SELECT * FROM nodes WHERE earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.lat, nodes.lon)) < ?3",
            nativeQuery = true)
    List<NodeEntity> getNodesInArea(double lat, double lon, double rad);
}
