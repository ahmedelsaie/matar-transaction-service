Running the app
Docker
$ docker-compose up

mysql port 3309
kafka port 29092
app   port 8080


open swagger on http://localhost:8080/swagger-ui/index.html
* register user using http://localhost:8080/auth/signup
* login with the registered user on http://localhost:8080/auth/login
* use authorize in swagger with the bearer token returned

api is divided into admi 
* user-platform apis
* admin-platform apis

System Description
system consisits mainly of transactions, each transaction is associated with user id, and domain id (which refer to order id).
system has a cron job that do the task of changing transaction state.
