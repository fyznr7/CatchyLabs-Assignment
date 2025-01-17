CatchyLabs Assignment
About the Project
This project is a Selenium-based automation framework using Cucumber and the BDD (Behavior-Driven Development) approach. It is designed to automate financial operations, including user login/logout scenarios, money transfers, and balance management. The framework is equipped with a configurable logging system and reusable helper classes for streamlined test execution.

Technologies and Libraries Used
Selenium: For browser automation to interact with web elements.
Cucumber: To write BDD-style test scenarios in Gherkin format.
Log4j 2: For customizable logging to monitor and debug test execution.
Unirest: For handling API requests.
Maven: For dependency management and project building.
Java: The programming language used for implementing test automation.
JUnit: For running the tests and generating reports.
Apache Commons: Provides utility methods for various functions like string manipulation.
Project Structure
1. Main Classes (src/main/java)
   log.CustomLayout: Custom PatternLayout for Log4j configuration to ensure logs are formatted according to project requirements.
2. Test Helpers (src/test/java/utils)
   DriverManager: Manages WebDriver lifecycle, including starting and stopping browser instances for tests.
   ConfigManager: Reads configuration data from properties files to be used for test execution, such as login credentials and URLs.
3. Step Definitions (src/test/java/steps)
   Financial Operations:
   AddMoneySteps: Implements the steps for adding money to a user's account.
   TransferMoneySteps: Contains the steps for transferring money between accounts.
   User Authentication:
   LoginSteps: Manages user login steps by interacting with the login page.
   LogoutSteps: Handles the process of logging out of the application.
   General Operations:
   MainSteps: Defines common operations such as navigation and interacting with the main page.
4. Test Runners (src/test/java/runners)
   TestRunner: A test runner class to execute Cucumber feature files.
   Hooks: Executes setup and teardown operations before and after each test scenario (e.g., WebDriver setup).
   HooksStepDefinitions: Contains step definitions related to setup and teardown for Cucumber scenarios.
5. Pages (src/main/java/pages)
   LoginPage: Defines the elements and actions on the login page.
   LogoutPage: Handles the logout page and its operations.
   MainPage: Contains elements and actions for the main application page.
   MoneyTransferPage: Defines elements and interactions for the money transfer feature.
   Scenarios
   Feature File (src/test/resources/features/catchylabs.feature)
   gherkin
   Copy code
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
Project Setup
Clone the repository:

bash
Copy code
git clone https://github.com/yourusername/CatchyLabs-Assignment.git
Install Maven dependencies:

bash
Copy code
mvn clean install
Run the tests:

bash
Copy code
mvn test
Logging
The project utilizes Log4j 2 for logging. The log format can be customized in the log4j2.xml file located in the resources directory. Log messages are generated at different levels (INFO, ERROR, DEBUG) to monitor the execution of the tests.

License
This project is licensed under the MIT License.