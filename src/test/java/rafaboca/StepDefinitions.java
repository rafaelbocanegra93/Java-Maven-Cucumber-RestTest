package rafaboca;

import io.cucumber.java.en.*;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Assertions;

public class StepDefinitions {
    private String url;
    private String protocol;
    private String requestBody ="";
    private int statusCode;
    private JsonNode responseBody;
    private int userId;

    @Given("a server (.*)$")
    public void aServer(String url) {
        this.url = url;
    }

    @And("with the protocol (.*)$")
    public void withTheProtocol(String protocol) {
        this.protocol = protocol;
    }

    @And("using a body with parameter (.*) and value (.*)$")
    public void usingABodyWithParameterAndValue(String bodyParameter, String bodyValue) {
        this.requestBody += '"' + bodyParameter + "\": \"" + bodyValue + "\",";

    }
    @When("the server execute the query for (.*) a User$")
    public void theServerExecuteTheQueryFor(String action) {
        HttpResponse<JsonNode> response;
        switch (action){
            case "Create":
                response = Unirest.post(this.protocol+"://"+this.url+"/api/users")
                        .header("accept", "application/json")
                        .body("{\n" +
                                        this.requestBody.substring(0, this.requestBody.length() - 1) +
                                "}")
                        .asJson();
                this.statusCode=response.getStatus();
                break;
            case "Read":
                response = Unirest.get(this.protocol+"://"+this.url+"/api/users/"+this.userId)
                        .header("accept", "application/json")
                        .asJson();
                this.statusCode=response.getStatus();
                break;
            case "Update":
                response = Unirest.put(this.protocol+"://"+this.url+"/api/users/"+this.userId)
                        .header("accept", "application/json")
                        .body("{\n" +
                                this.requestBody.substring(0, this.requestBody.length() - 1) +
                                "}")
                        .asJson();
                this.statusCode=response.getStatus();
                break;
            case "Delete":
                response = Unirest.delete(this.protocol+"://"+this.url+"/api/users/"+this.userId)
                        .header("accept", "application/json")
                        .asJson();
                this.statusCode=response.getStatus();
                break;
            case "List":
                //TODO pending request
                break;
            default:
                System.err.println("not implemented");
                break;
        }
    }

    @Then("the server response with Status {int}")
    public void theServerResponseWithStatus(int statusCode) {
        Assertions.assertEquals(statusCode,this.statusCode);
    }

    @And("with the userID as ID {int}")
    public void theUserWithID(int userId) {
        this.userId=userId;
    }
}