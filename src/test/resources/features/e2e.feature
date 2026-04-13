Feature: Swag Labs End-to-End Flow
@smoke
  Scenario: Complete purchase flow
    Given user logs in with username "standard_user" and password "secret_sauce"
    When user adds product to cart
    And user navigates to cart
    And user proceeds to checkout
    And user enters checkout details
    And user finishes order
    Then Product purchase is displayed