Feature: cart functionality
  Background: user logs in with username "standard_user" and password "secret_sauce"
    @cart
    Scenario: user able to add products to cart
      Given User is in products page
      When user adds product to cart
      Then Count of products in cart gets updated
      And selected item is appeared
  @cart
    Scenario: user able to add products to cart
      Given User is in products page
      When user adds product to cart multiple times
      Then Count of products in cart gets updated
      When user removes product from cart
      Then Count of products in cart gets updated
  @cart
  Scenario: user able to add products to cart
    Given User is in products page
    When user adds product to cart
    And user removes product from products page
    Then Count of products in cart gets updated
  @cart
    Scenario: user able to add products to cart
      Given User is in products page
      When user adds product to cart multiple times
      Then Count of products in cart gets updated

  @cart
    Scenario: user able to add products to cart
      Given User is in products page
      When user adds product to cart
      And click on reset app
      Then products in cart should be empty
  @cart
    Scenario: user able to add products to cart
      Given Verify correct price calculation
      When user adds multiple product to cart
      Then calculate the total price
  @cart
    Scenario: user able to add products to cart
      Given User is in products page
      When user adds product to cart
      Then Count of products in cart gets updated
      When click on cart
      And  click on Continue shopping button
      Then products page is displayed
  @cart
    Scenario: user able to add products to cart
      Given Verify Checkout button navigates correctly
      When user adds product to cart
      Then Count of products in cart gets updated
      And selected item is appeared
      When click on cart
      And click on checkout
      Then Address page is displayed

