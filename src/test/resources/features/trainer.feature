Feature: start new lingo game
  As a lingo participant
  I want to start a new game
  so I can practice in getting better at lingo

Scenario: a game has started
  When i start the game
  Then i start with a 5 letter word
  And i have 0 score

Feature: start new round
  As a lingo participant
  I want to start a new round
  so I can continue practicing

Scenario Outline: a round was won
  Given I am playing the game
  And the round was won
  And the previous word had "<previous word>" letters
  When I start a new round
  Then the next word had to be "<next word>" letters

  Examples:
    | previous length | next length |
    | 5               | 6           |
    | 6               | 7           |
    | 7               | 5           |
