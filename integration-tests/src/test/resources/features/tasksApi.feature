Feature: Testing PasswordEncoder Function
  Method to hash and decrypt password for users

  Scenario: Password will be crypted
    Given user give a test as password
    When BCrypt change the password
    Then the password is hashed

  Scenario: Password will be crypted2
    Given user give a test as password
    When BCrypt change the password
    Then the password is hashed

  Scenario: Password will be crypted3
    Given user give a test as password
    When BCrypt change the password
    Then the password is hashed