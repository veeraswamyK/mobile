@smoke @e2e
Feature: End-to-End Purchase Flow
  As a logged-in user
  I want to complete a full purchase
  So that I can verify the entire checkout journey works correctly

  Scenario: Complete purchase flow from login to order confirmation
    Given user logs in with username "standard_user" and password "secret_sauce"
    When user adds product to cart
    And user navigates to cart
    And user proceeds to checkout
    And user enters checkout details
    And user finishes order
    Then Product purchase is displayed
