package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    @RequestMapping("/cards")
    public List<Card> index() {
        return cardService.getAllCards();
    }

}