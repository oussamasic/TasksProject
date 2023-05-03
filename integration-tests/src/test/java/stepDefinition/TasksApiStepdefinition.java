package stepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class TasksApiStepdefinition {


    @Given("user give a test as password")
    public void userGiveATestAsPassword() {
        System.out.println("hello1");
    }
    @When("BCrypt change the password")
    public void bcryptChangeThePassword() {
        System.out.println("hello2");
    }

    @Then("the password is hashed")
    public void thePasswordIsHashed() {
        System.out.println("hello3");
    }
}
