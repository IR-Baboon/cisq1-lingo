Feature: practice lingo
  As a lingo participant
  I want practice 5, 6 or 7 letter words
  so I can practice in getting better at lingo

Scenario: a game has started
  When i start a new game
  Then i start with a 5 letter word
  And i have 0 score
  And i should see the first letter

Scenario Outline: guess a word
  Given I am playing a game
  And the word to guess is "<current word>"
  When my guess is "<my guess>"
  Then i get feedback "<feedback>"

  Examples:
  | current word | my guess | feedback |
  | BAARD        | BERGEN   | INVALID, INVALID, INVALID, INVALID, INVALID, INVALID |
  | BAARD        | BONJE    | CORRECT, ABSENT, ABSENT, ABSENT, ABSENT              |
  | BAARD        | BARST    | CORRECT, CORRECT, PRESENT, ABSENT, ABSENT            |
  | BAARD        | DRAAD    | ABSENT, PRESENT, CORRECT, PRESENT, CORRECT           |
  | BAARD        | BAARD    | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT,         |


Scenario Outline: a round was won
  Given I am playing a game
  And the round was won
  And the previous word had "<previous word>" letters
  When I start a new round
  Then the next word had to be "<next word>" letters

  Examples:
    | previous length | next length |
    | 5               | 6           |
    | 6               | 7           |
    | 7               | 5           |
