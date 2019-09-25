# simplemoneytransferapplication
A Simple simulation of money transfer between accounts via REST API

# Usage

## To run tests

mvn clean test

## To run application in standalone mode

mvn exec:java

## Endpoint examples

###  Add user:

POST http://localhost:4567/moneytfapp/accountusers

sample request:

{
	"userName": "sunnydas",
	"firstName":"Sunny",
	"lastName": "Das",
	"emailAddress": "abc@def.com",
	"dob": "",
	"address": ""
}

sample response

{
    "id": 1,
    "firstName": "Sunny",
    "lastName": "Das",
    "emailAddress": "abdc@def.com",
    "userName": "sunnydas",
    "dob": "",
    "address": ""
}

### Add account

POST http://localhost:4567/moneytfapp/account

sample request:

{
	"accountName": "sunny",
	"userId":"1",
	"branchCode": "BN01",
	"currencyCode": "INR",
	"balance": "100",
	"accountType": "savings"
}

sample response:

{
    "id": 1,
    "userId": 1,
    "branchCode": "BN01",
    "accountName": "sunny",
    "currencyCode": "INR",
    "balance": 100,
    "accountType": "savings"
}

### Transfer between accounts

POST http://localhost:4567/moneytfapp/transfer

sample request:

{
	"sourceAccountId" :"1",
	"targetAccountId": "2",
	"transferAmount": "10"
}

sample response:

{
    "sourceAccountId": 1,
    "targetAccountId": 2,
    "transferAmount": 10,
    "dateOfTransfer": "2019-09-25",
    "status": "SUCCESS"
}

### Update account balance

PUT http://localhost:4567/moneytfapp/account

sample request:

{
	"accountName": "ramesh",
	"id": "2",
	"userId":"2",
	"branchCode": "BN01",
	"currencyCode": "INR",
	"balance": "300",
	"accountType": "savings"
}

sample response:

{
    "id": 2,
    "userId": 2,
    "branchCode": "BN01",
    "accountName": "ramesh",
    "currencyCode": "INR",
    "balance": 300,
    "accountType": "savings"
}

### Get account details

GET http://localhost:4567/moneytfapp/account/1

sample response:

{
    "id": 1,
    "userId": 1,
    "branchCode": "BN01",
    "accountName": "sunny",
    "currencyCode": "INR",
    "balance": 100,
    "accountType": "savings"
}

### Get user details

GET http://localhost:4567/moneytfapp/accountusers/1

sample response:

{
    "id": 1,
    "firstName": "Sunny",
    "lastName": "Das",
    "emailAddress": "abc@def",
    "userName": "sunnydas",
    "dob": "",
    "address": ""
}

# Technologies used

Spark
H2
Jersey client
Java 1.8
