@cart
Feature: Shopping Cart
  As a logged-in user
  I want to manage products in my cart
  So that I can review and proceed to checkout

  Background:
    Given User is in products page

  Scenario: Add single product and verify cart badge updates
    When user adds product to cart
    Then Count of products in cart gets updated
    And selected item is appeared

  Scenario: Add multiple different products to cart
    When user adds product to cart multiple times
    Then Count of products in cart gets updated

  Scenario: Remove a product from within the cart
    When user adds product to cart multiple times
    Then Count of products in cart gets updated
    When user removes product from cart
    Then Count of products in cart gets updated

  Scenario: Remove a product from the products page
    When user adds product to cart
    And user removes product from products page
    Then Count of products in cart gets updated

  Scenario: Reset app state clears the cart
    When user adds product to cart
    And click on reset app
    Then products in cart should be empty

  Scenario: Cart total matches sum of individual product prices
    When user adds multiple product to cart
    Then calculate the total price

  Scenario: Continue shopping returns to products page
    When user adds product to cart
    Then Count of products in cart gets updated
    When click on cart
    And click on Continue shopping button
    Then products page is displayed

  Scenario: Checkout button navigates to address page
    When user adds product to cart
    Then Count of products in cart gets updated
    And selected item is appeared
    When click on cart
    And click on checkout
    Then Address page is displayed
