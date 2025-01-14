@mweb
Feature: ATM MWEB Scenarios

  Background:
    Given Setup Driver
      | Browser     | DeviceName | Dimensions |
      | MWEB_CHROME | Pixel 2    | 400x800    |
    When Login process with user "testUser"

  @mweb @login
  Scenario: Login to the system
    Then User logs out

  @mweb @addMoney
  Scenario: Add money to account
    And User adds money using card "default" with amount "100"
    Then User logs out

  @mweb @invalidCard
  Scenario: Add money to account with invalid card
    And User adds money using card "invalidCard" with amount "100"
    Then User logs out

  @mweb @transfer
  Scenario: Transfer money between accounts
    And User transfers "50" from "Main Account->Testinium-2"
    Then User logs out

  @mweb @completeFlow
  Scenario: Complete flow with all actions
    And User adds money using card "default" with amount "100"
    And User transfers "50" from "Main Account->Testinium-2"
    Then User logs out

  @mweb @negativeBalance
  Scenario: Negative balance control
    And User transfers "balance" from "Main Account->Testinium-2"
    Then User logs out
