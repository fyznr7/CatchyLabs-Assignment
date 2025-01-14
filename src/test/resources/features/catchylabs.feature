@web
Feature: ATM Scenarios

  Background:
    Given Setup Driver
      | Browser |
      | CHROME  |
    When Login process with user "testUser"

  @web @login
  Scenario: Login to the system
    Then User logs out

  @web @addMoney
  Scenario: Add money to account
    And User adds money using card "default" with amount "100"
    Then User logs out

  @web @invalidCard
  Scenario: Add money to account with invalid card
    And User adds money using card "invalidCard" with amount "100"
    Then User logs out

  @web @transfer
  Scenario: Transfer money between accounts
    And User transfers "50" from "Main Account->Testinium-2"
    Then User logs out

  @web @completeFlow
  Scenario: Complete flow with all actions
    And User adds money using card "default" with amount "100"
    And User transfers "50" from "Main Account->Testinium-2"
    Then User logs out

  @web @negativeBalance
  Scenario: Negative balance control
    And User transfers "balance" from "Main Account->Testinium-2"
    Then User logs out
