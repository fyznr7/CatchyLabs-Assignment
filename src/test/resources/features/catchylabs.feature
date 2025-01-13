Feature: ATM Scenarios

  Background:
    When Login process with user "testUser"

  Scenario: Login to the system
    Then User logs out

  Scenario: Add money to account
    And User adds money using card "default" with amount "100"
    Then User logs out

  Scenario: Add money to account with invalid card
    And User adds money using card "invalidCard" with amount "100"
    Then User logs out

  Scenario: Transfer money between accounts
    And User transfers "50" from "Main Account->Testinium-2"
    Then User logs out

  Scenario: Complete flow with all actions
    And User adds money using card "default" with amount "100"
    And User transfers "50" from "Main Account->Testinium-2"
    Then User logs out

  Scenario: Negative balance control
    And User transfers "balance" from "Main Account->Testinium-2"
    Then User logs out
