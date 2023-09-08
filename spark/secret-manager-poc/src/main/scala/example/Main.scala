package example

import com.amazonaws.client.builder.{AwsClientBuilder}
import com.amazonaws.services.secretsmanager.{AWSSecretsManager,AWSSecretsManagerClientBuilder}
import com.amazonaws.services.secretsmanager.model.{GetSecretValueRequest, GetSecretValueResult}

object Main extends Greeting with App {

  def getValue(secretsClient:AWSSecretsManager ,secretName:String ) {

    try {
        val valueRequest:GetSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);

        val valueResponse:GetSecretValueResult = secretsClient.getSecretValue(valueRequest);
        val secret:String = valueResponse.getSecretString();
        println(s"REtreived secret: $secret");

    } catch { case e : Exception => println("ERROR: " + e.getStackTrace) }
  }
  println(greeting)
  val region:String = "us-west-2";
  val endpoint:String = s"secretsmanager.$region.amazonaws.com";
  val config:AwsClientBuilder.EndpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
  val secretsClient:AWSSecretsManager = AWSSecretsManagerClientBuilder.defaultClient();
  this.getValue(secretsClient, "epia-data/prod/airflow/variables/aws_region")
}



trait Greeting {
  lazy val greeting: String = "hello"
}
