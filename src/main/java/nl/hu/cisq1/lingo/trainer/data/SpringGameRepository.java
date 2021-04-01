package nl.hu.cisq1.lingo.trainer.data;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

/**
 * We depend on an interface,
 * Spring generates an implementation based on our configured adapters
 * (see: application.properties and pom.xml)
 */
public interface SpringGameRepository extends JpaRepository<Game, String> {
    @Query(nativeQuery=true, value="SELECT * FROM games g WHERE g.id = ?1 ")
    Optional<Game> findById(String id);
}
