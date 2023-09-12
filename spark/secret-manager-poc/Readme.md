# How execute it

```bash
java -jar target/scala-2.11/secretsmanager-poc-fatjar-1.0.jar
```

The code follows [AWSSecretsManagerClientBuilder.defaultClient()](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/secretsmanager/AWSSecretsManagerClientBuilder.html#defaultClient--) credentials retrieval process defined in [Class DefaultAWSCredentialsProviderChain](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.html)

# How build a fat jar

Based on https://www.baeldung.com/scala/sbt-fat-jar

```bash
sbt clean assembly
```

# How explore the JAR

Based on https://www.baeldung.com/java-view-jar-contents

```bash
jar tf target/scala-2.11/secretsmanager-poc-fatjar-1.0.jar  
```