@nav
Feature: Navigation Menu
  As a logged-in user
  I want to access the navigation menu
  So that I can log out or use other menu features

  Background:
    Given user is logged in

  Scenario: Navigation menu opens successfully
    When user click on nav-bar$
    Then nav bar options are displayed

  Scenario: User can logout via navigation menu
    When user click on nav-bar$
    And user click on logout
    Then login page is displayed

  Scenario: QR code scanner opens from navigation menu
    When click on the navbar
    And click on the qr code scanner

  Scenario: Geo location opens from navigation menu
    When click on the navbar
    And click on the Geo location
