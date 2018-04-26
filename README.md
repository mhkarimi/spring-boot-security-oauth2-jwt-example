Hi! This is the sample project to show how to use Spring Security for Oauth2. I should explain instead of username and password
I used username and title(which requires some changes in some classes). For the persistence of the tokens, I uses **RedisTokenStore** and also **JWT** to implement the given APIs. I also configured log4j2.xml file to format the output.

## Here are listed classes I've created/changed 

 - I extend ResourceOwnerPasswordTokenGranter and override getOAuth2Authentication() method 
 - add OauthSettings class to read the configuration from application.yml file
 - define new beans of type ClientDetailsService,AuthorizationServerTokenServices,OAuth2RequestFactory and JwtAccessTokenConverter 
 - enhanced token length with setting the property of AuthorizationServerEndpointsConfigurer by JwtAccessTokenConverter

## Before you start

 - You should have **redis** installed on your local machine, or via docker and within default port.
 - You should have postgresql DB installed on your local machine
 - You should be on a Linux based OS
 - You should install **jq** tools in order to prettify your json output result
 


## Postgresql configuration
```
 - #createdb -hlocalhost -p5432 -Upostgres testdb2	 
 - #psql -d test -U postgres 
 
 ```
above command is for login to database.
I should explain , all of the required data will be loaded to database **automatically**. Its on file *scripts.sql*



## Send request and get accessToken and refreshToken
**POST /oauth/token**
```
curl -X POST \
  http://localhost:8080/oauth/token \
  -H "Authorization: Basic `echo -n clientId:clientSecret | base64`" \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'username=givenUsername&title=givenTitile&grant_type=password' | jq .
```
example  (based on our database and configurations):
```
curl -X POST \
  http://localhost:8080/oauth/token \
  -H "Authorization: Basic `echo -n phonePayClient:phonePaySecret | base64`" \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'username=mh1&title=hasan&grant_type=password' | jq .
```
## Send a request to get the users list
**GET /users/user?access_token=ACCESS_TOKEN**
```
curl -X GET \
  'http://localhost:8080/users/user?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2VfaWQiXSwidXNlcl9uYW1lIjoibWgxIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNTIyNzM2OTg0LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjY4ZTFmZjBjLWUwM2EtNDJkMy1hYjdlLTg5NTA5OWYzOTk1ZCIsImNsaWVudF9pZCI6InBob25lUGF5Q2xpZW50In0.YSNdzBM_FjvutGhMhht9J7VoPbuIhEd8N78t0KrxgAE' \
  -H "Authorization: Basic `echo -n phonePayClient:phonePaySecret | base64`" \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json'  | jq .
```

## Send a request to update the user with his/her title
**POST /users/user?access_token=ACCESS_TOKEN**
```
curl -X PUT \
  'http://localhost:8080/users/user?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2VfaWQiXSwidXNlcl9uYW1lIjoibWgxIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNTIyNzM2OTg0LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjY4ZTFmZjBjLWUwM2EtNDJkMy1hYjdlLTg5NTA5OWYzOTk1ZCIsImNsaWVudF9pZCI6InBob25lUGF5Q2xpZW50In0.YSNdzBM_FjvutGhMhht9J7VoPbuIhEd8N78t0KrxgAE' \
  -H "Authorization: Basic `echo -n phonePayClient:phonePaySecret | base64`" \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d ' {
        "id": 1,
        "username": "mh2",
        "title": "hasan2"
    }' | jq .
```
Given type shall be send in the body is as follow :
```
{
        "id": Integer,
        "username": "username",
        "title": "title"
}
```
## Extra explanations 
In order to add title as a parameter of tokenRequest I changed **MyResourceOwnerPasswordTokenGranter ** class and mainly changed the  **AuthorizationServerConfig** class and added a **tokenEnhancer** class to increase the accessToken and refreshToken length. If you need more details about what I've done, I will be ready to have a discussion.
