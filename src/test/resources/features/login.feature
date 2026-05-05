@login
Feature: User Authentication
  As a mobile app user
  I want to log in with valid or invalid credentials
  So that I can access or be denied access appropriately

  Scenario: Valid login with standard user
    Given user logs in with username "standard_user" and password "secret_sauce"
    Then products page is displayed

  Scenario Outline: Login with multiple user types
    Given user logs in with username "<username>" and password "<password>"
    Then products page is result "<result>"

    Examples:
      | username           | password     | result        |
      | standard_user      | secret_sauce | Displayed     |
      | locked_out_user    | secret_sauce | Not Displayed |
      | problem_user       | secret_sauce | Displayed     |
      |                    |              | Not Displayed |

  Scenario: Invalid login with wrong password shows error
    Given user logs in with username "standard_user" and password "wrong_password"
    Then Error message should display
