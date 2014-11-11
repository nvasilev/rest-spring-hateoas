# README #


## Vote ##

### Get vote ###
```
GET http://127.0.0.1:8080/polls/1/alternatives/1/votes/1
Accept:application/hal+json
```
```
#!json
{
    "voteId": 1,
    "email": "a@b.com",
    "_links": {
        "self": {
            "href": "http://127.0.0.1:8080/polls/1/alternatives/1/votes/1"
        },
        "votesCount": {
            "href": "http://127.0.0.1:8080/polls/1/alternatives/1/votes/count"
        }
    }
}
```

### Create vote ###

Request
```
POST http://127.0.0.1:8080/polls/1/alternatives/1/votes/1
Content-Type:application/json
Accept:application/hal+json
```
```
#!json
{
  "email" : "a@b.com"
}
```
Response: 201 CREATED
```
#!json
{
    "voteId": 1,
    "email": "a@b.com",
    "_links": {
        "self": {
            "href": "http://127.0.0.1:8080/polls/1/alternatives/1/votes/1"
        },
        "votesCount": {
            "href": "http://127.0.0.1:8080/polls/1/alternatives/1/votes/count"
        }
    }
}
```

### Votes Count ###

```
GET http://127.0.0.1:8080/polls/1/alternatives/1/votes/count
```
Response: 200 OK
```
3
```


This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact