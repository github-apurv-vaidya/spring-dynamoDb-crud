package com.aws.dynamodb.crud.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;
import com.aws.dynamodb.crud.util.Constants;

@Configuration
public class TableConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(TableConfiguration.class);
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Value("${dynamoDb.tableName}")
	private String tableName;
	@Value("${dynamoDb.hashKey}")
	private String hashKey;
	private String region = Constants.REGION;
	private String accessId = Constants.ACCESS_KEY;
	private String accessKey = Constants.SECRET_KEY;

	@PostConstruct
	public void initializeTable() throws TableNeverTransitionedToStateException, InterruptedException {
		List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
		List<KeySchemaElement> schemaElement = new ArrayList<>();
		schemaElement.add(new KeySchemaElement(hashKey, KeyType.HASH));
		attributeDefinitions.add(new AttributeDefinition(hashKey, ScalarAttributeType.S));
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(10L, 10L);
		CreateTableRequest request = new CreateTableRequest(attributeDefinitions, tableName, schemaElement,
				provisionedThroughput);
		TableUtils.createTableIfNotExists(amazonDynamoDB, request);
		TableUtils.waitUntilActive(amazonDynamoDB, tableName);
	}

	@Bean
	public AmazonDynamoDB getAmazonDynamoDB() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessId, accessKey);
		AWSCredentialsProvider awsCredentialProvider = new AWSStaticCredentialsProvider(awsCredentials);
		return AmazonDynamoDBClientBuilder.standard().withCredentials(awsCredentialProvider).withRegion(region).build();
	}

	@Bean
	public DynamoDBMapper getDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
		return new DynamoDBMapper(amazonDynamoDB);
	}
}
