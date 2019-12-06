package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.db.entity.Role;
import bueffle.db.entity.User;
import bueffle.exception.CardNotFoundException;
import bueffle.exception.CollectionNotFoundException;
import bueffle.exception.UserNotFoundException;
import bueffle.model.CardRepository;
import bueffle.model.CollectionRepository;
import bueffle.model.RoleRepository;
import bueffle.model.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackendService {

    private final CardRepository cardRepository;
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    BackendService(CardRepository cardRepository, CollectionRepository collectionRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.cardRepository = cardRepository;
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
    }

    public List<Card> getAllCards() {
        return new ArrayList<>(cardRepository.findAll());
    }

    /**
     * Creates new card. If there is no default collection yet, a default collection gets created.
     * The new card is added to the default collection then.
     * @param card the card to add
     */
    public void addCard(Card card) {
        if(!defaultCollectionExists()) {
            addDefaultCollection();
        }
        card.addCollection(collectionRepository.findByName("default"));
        cardRepository.save(card);
    }

    /**
     * Updates name and description of an existing card
     * @param newCard the fields which should be updated are contained in this card instance
     * @param cardId the id of the card which should be updated
     */
    public void updateCard(Card newCard, Long cardId) {
        cardRepository.findById(cardId)
                .map(card -> {
                    card.setQuestion(newCard.getQuestion());
                    card.setAnswer(newCard.getAnswer());
                    return cardRepository.save(card);
                })
                .orElseThrow(() -> new CollectionNotFoundException(cardId));
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    private boolean defaultCollectionExists() {
        return collectionRepository.findByName("default") != null;
    }

    private void addDefaultCollection() {
        addCollection(new Collection("default", "default"));
    }

    /**
     * Adds a card with cardId to a collection with collectionId. In both cases, the id gets checked
     * and an appropriate Exception gets thrown when the id can't be found in the database.
     * @param cardId id of the card which should be added to the collection
     * @param collectionId id of the collection to which the card should be added
     */
    public void addCardToCollection(Long cardId, Long collectionId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        card.addCollection(collectionRepository.findById(collectionId)
                                .orElseThrow(() -> new CollectionNotFoundException(collectionId)));
        cardRepository.save(card);
    }

    public List<Collection> getAllCollections() {
        return new ArrayList<>(collectionRepository.findAll());
    }

    public Collection getCollection(Long id) {
        return collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
    }

    public void addCollection(Collection collection) {
        collectionRepository.save(collection);
    }

    /**
     * Updates name and description of an existing collection
     * @param newColl the fields which should be updated are contained in this collection instance
     * @param collectionId the id of the collection which should be updated
     */
    public void updateCollection(Collection newColl, Long collectionId) {
        collectionRepository.findById(collectionId)
                .map(collection -> {
                    collection.setName(newColl.getName());
                    collection.setDescription(newColl.getDescription());
                    return collectionRepository.save(collection);
                })
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
    }

    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }

    /**
     * Adding user, adding a role for the user if it has none yet.
     * Hashing the Password so it doesn't get saved in plain text in the database.
     * Sending the user to the userRepository.
     * @param user user
     */
    public void addUser(User user) {
        if(!userRoleExists()) {
            addUserRole();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }

    private boolean userRoleExists() {
        return roleRepository.findByName("ROLE_USER") != null;
    }

    private void addUserRole() {
        roleRepository.save(new Role("ROLE_USER"));
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
