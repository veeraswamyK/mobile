Feature: Navbar functionality
  Background: user logs in with username "standard_user" and password "secret_sauce"
    @nav
    Scenario:Verify User is able to open the qr scanner
      Given user is logged in
      When click on the navbar
      And click on the qr code scanner
  @nav
    Scenario:Verify User is able to handle maps
      Given user is logged in
      When click on the navbar
      And click on the Geo location
      Then maps are opened
  @nav
    Scenario:Verify User is able to navigating to about webpage
      Given user is logged in
      When click on the navbar
      And click on the Geo location
      Then About webpage is opened

  @nav
    Scenario: Valid logout
      Given user logs in with username "standard_user" and password "secret_sauce"
      Then products page is displayed
      When user click on nav-bar
      Then nav bar options are displayed
      When user click on logout
      Then login page is displayed
