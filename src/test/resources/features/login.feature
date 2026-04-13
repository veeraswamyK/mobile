Feature: LoginTest
 @login
  Scenario: Valid login
    Given user logs in with username "standard_user" and password "secret_sauce"
    Then products page is displayed
  @login
    Scenario Outline: Valid login with multiple user
      Given user logs in with username "<username>" and password "<password>"
      Then products page is result "<result>"
      Examples:
      |username|password|result|
      |standard_user   |secret_sauce     |Displayed|
      |locked_out_user |secret_sauce     |Not Displayed|
      |problem_user    |secret_sauce     |Displayed|
      |                    |                      |Not Displayed|
  @login
    Scenario: Invalid login
      Given user logs in with username "standard_user" and password "secet_sauce"
      Then Error message should display