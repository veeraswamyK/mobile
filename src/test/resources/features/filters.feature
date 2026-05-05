@prod
Feature: Product Filtering and Sorting
  As a logged-in user
  I want to filter and sort the product list
  So that I can find products more easily

  Background:
    Given user logs in with username "standard_user" and password "secret_sauce"

  Scenario: Toggle product list view
    Given user is in card view
    When user click on the view button
    Then Product visibility is changed

  Scenario: Sort products A to Z
    Given User able to sort the products based on the naming A-Z
    When user click on filter icon
    Then Sort items by pop-up is displayed
    When user selects the a-z
    Then list of products are in selected order

  Scenario: Sort products Z to A
    Given User able to sort the products based on the naming Z-A
    When user click on filter icon
    Then Sort items by pop-up is displayed
    When user selects the z-a
    Then list of products are in selected order

  Scenario: Sort products by price low to high
    Given User able to sort the products based on the price low to high
    When user click on filter icon
    Then Sort items by pop-up is displayed
    When user selects the price low to high
    Then list of products are in selected order

  Scenario: Sort products by price high to low
    Given User able to sort the products based on the price high to low
    When user click on filter icon
    Then Sort items by pop-up is displayed
    When user selects the price high to low
    Then list of products are in selected order

  Scenario: Cancel sort dialog returns to products page
    Given User able to cancel the sorting of the products
    When user click on filter icon
    Then Sort items by pop-up is displayed
    When user clicks on cancel
    Then products page is displayed
