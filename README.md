
# CatchyLabs Assignment

## About the Project
This project is a Selenium-based automation framework utilizing Cucumber and the BDD (Behavior Driven Development) approach. It is designed to test financial operations, user login/logout scenarios, and money transfer processes. The framework includes a configurable logging system and reusable helper classes.

---

## Technologies and Libraries Used
- **Selenium**: For web automation.
- **Cucumber**: For writing BDD-based test scenarios.
- **Log4j 2**: For logging mechanism.
- **Unirest**: For handling API requests.
- **Maven**: For dependency management and project configuration.
- **Java**: The programming language forming the foundation of the entire system.
- **JUnit**: For test execution and reporting.
- **Apache Commons**: For utility methods and string operations.

---

## Project Structure

### 1. Main Classes (`src/main/java`)
- **`log.CustomLayout`**: A customized `PatternLayout` for Log4j, allowing you to tailor log formatting to your needs.

### 2. Test Helpers (`src/test/java/utils`)
- **`DriverManager`**: A helper class for managing Selenium WebDriver. It starts and closes browser sessions.
- **`ConfigManager`**: Reads configuration settings from the `config.properties` file.

### 3. Step Definitions (`src/test/java/steps`)
#### Financial Operations:
- **`AddMoneySteps`**: Steps for scenarios where users add money to their accounts.
- **`TransferMoneySteps`**: Steps for money transfer operations between users.

#### Login/Logout Operations:
- **`LoginSteps`**: Steps for user login processes.
- **`LogoutSteps`**: Steps for user logout processes.

#### General Operations:
- **`MainSteps`**: Steps for general scenarios and homepage operations.

### 4. Test Runners (`src/test/java/runners`)
- **`TestRunner`**: Executes Cucumber test scenarios.
- **`Hooks` and `HooksStepDefinitions`**: Steps that execute before/after test scenarios (e.g., initializing WebDriver or handling logging).

---

## Scenarios

### Feature File (`src/test/resources/features/catchylabs.feature`)
```gherkin
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
```

---

## Project Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/fyznr7/CatchyLabs-Assignment.git
   ```
2. Install Maven dependencies:
   ```bash
   mvn clean install
   ```
3. Run the tests:
   ```bash
   mvn test
   ```

---

## Logging
The project uses Log4j 2 for logging. The log format can be customized through the `log4j2.xml` file.

---