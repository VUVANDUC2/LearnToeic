package LearnToeic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import LearnToeic.entity.Flashcard;
import org.springframework.stereotype.Repository;
import java.util.List; 

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    @Query("SELECT f FROM Flashcard f WHERE f.user.userId = :userId AND f.deck = :deckName")
    List<Flashcard> findByDeck(Integer userId,String deckName);

    @Query("SELECT f.deck, COUNT(f) " +
       "FROM Flashcard f " +
       "WHERE f.user.userId = :userId " +
       "GROUP BY f.deck")
    List<Object[]> findDeckNamesAndCountsByUserId(@Param("userId") Integer userId);

    @Query("Select f from Flashcard f WHERE f.deck = :deckName AND f.user.userId = :userId")
    List<Flashcard> findByDeckNameAndId(@Param("userId") Integer userId, @Param("deckName") String deckName);
}
