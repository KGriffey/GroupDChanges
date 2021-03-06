Feature: Settlement
  Description: Settlements are created by meeple placements and are used in score counting.
  Scenario: Founding a settlementToExpand with white meeples
    Given I have a game map initalized
    When I am on level 1
    Then a white player must be placed on a non-volcano terrain

  Scenario: Founding a settlementToExpand with black meeples
    Given my game map  is initalized
    When I am on the first level
    Then a black player must be placed on a non-volcano terrain


#    Scenario: Founding a settlementToExpand (continued)
#      Given a game map is initalized
#      When I have exsisting settlements
#      Then I cannot found a settlementToExpand on an exsisting settlementToExpand

