package LearnToeic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import LearnToeic.entity.Flashcard;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option; 

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

    @Modifying
    @Transactional
    @Query("DELETE FROM Flashcard f WHERE f.deck = :deckName AND f.user.userId = :userId")
    void deleteFlashcardByDeckName(@Param("userId") Integer userId,
                                   @Param("deckName") String deckName);
    @Query("SELECT f FROM Flashcard f WHERE f.flashcardId = :flashcardId AND f.user.userId = :userId")
    Optional<Flashcard> findById_UserId(@Param("flashcardId") int flashcardId,
                                         @Param("userId") int userId);
}
