package tech.xavi.flatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.xavi.flatbot.entity.Ad;

import java.util.List;
import java.util.Set;

@Repository
public interface AdRepository extends JpaRepository<Ad,String> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM ad WHERE id = :id)", nativeQuery = true)
    boolean isPresentById(@Param("id") String id);

    @Query("SELECT a.price FROM Ad a WHERE a.id = :id")
    int findPriceById(String id);

    @Query("SELECT a.id FROM Ad a")
    List<String> findAllIds();

    void deleteByIdIn(Set<String> ids);

}
