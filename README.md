# Virtual Store App

This project is a Spring Boot-based application that contains a set of APIs for managing a store's products and orders.

## Features

- The app was deployed on AWS using AWS Elastic Beanstalk
- It uses a PostgreSQL database hosted on AWS RDS
- The app includes a CI/CD pipeline that runs a script on each push to the ```master``` branch. The pipeline builds the app, runs tests, and deploys it to AWS.
- Agile methodology is simulated by creating Issues for every significant implementation.
- Each feature is developed on a separate branch, which is later merged into ```master``` via a pull request.
## Tech Stack

- Java 17
- Maven
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- JUnit 5 for testing

## API Endpoints

<i>
*Find examples on calling them after this table
</i>

<table>
<tr>
    <th>HTTP method</th>
    <th>URL</th>
    <th>Description</th>
    <th>Callable only by ADMIN?</th>
    <th>Request body</th>
</tr>
<tr>
    <td>POST</td>
    <td>/api/auth/register</td>
    <td>Register a new user</td>
    <td>No</td>
    <td>AuthenticationRequestDTO</td>
</tr>
<tr>
    <td>POST</td>
    <td>/api/auth/login</td>
    <td>Login as an existing user</td>
    <td>No</td>
    <td>AuthenticationRequestDTO</td>
</tr>
<tr>
    <td>GET</td>
    <td>/api/products/{id}</td>
    <td>Get a specific product</td>
    <td>No</td>
    <td>-</td>
</tr>
<tr>
    <td>POST</td>
    <td>/api/products</td>
    <td>Create a new product</td>
    <td>Yes</td>
    <td>ProductDTO</td>
</tr>
<tr>
    <td>DELETE</td>
    <td>/api/products/{id}</td>
    <td>Delete a product</td>
    <td>Yes</td>
    <td>-</td>
</tr>
<tr>
    <td>POST</td>
    <td>/api/orders</td>
    <td>Create a new order</td>
    <td>No</td>
    <td>OrderDTO</td>
</tr>
<tr>
    <td>GET</td>
    <td>/api/orders</td>
    <td>List all orders</td>
    <td>Yes</td>
    <td>-</td>
</tr>
</table>

## Test the APIs

- The application is deployed live on AWS, so you can directly interact with the APIs using cURL or Postman.
- This is the base URL of the app: ```http://virtual-store-1.eu-north-1.elasticbeanstalk.com```
- First, you need to authenticate in order to call any API. There are two roles: ```ADMIN``` and ```USER```.
- If you register a user using the ```/api/auth/register``` API, the registered user will have the role ```USER``` by default
- In order to login as an ```ADMIN```, you can use these credentials:

<table>
    <tr>
        <th>Username</th>
        <td>admin</td>
    </tr>
    <tr>
        <th>Password</th>
        <td>admin</td>
    </tr>
</table>

- With the username and password you can call the ```/api/auth/login``` API. If everything went well, you should receive a JWT token as response from the API. You can then use this JWT token in the header of the HTTP request to other APIs as a Bearer token.
- As a USER you can find Products by ID and place Orders
- As an ADMIN you can add or delete Products and see all the Orders

<i>*Note: Sharing credentials for an admin account in a README file is against best practices. However, for this demo application, we will not overcomplicate things.
<br>The application.properties file also contains hardcoded values, which should normally be kept in an environment file.
</i>

### <u>cURL Examples</u>

#### POST /api/auth/register
```
curl --location 'http://virtual-store-1.eu-north-1.elasticbeanstalk.com/api/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "username": "user",
    "password": "pass"
}'
```
#### POST /api/auth/login
```
curl --location 'http://virtual-store-1.eu-north-1.elasticbeanstalk.com/api/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "admin",
    "password": "admin"
}'
```
#### GET /api/products/{id}
```
curl --location 'http://virtual-store-1.eu-north-1.elasticbeanstalk.com/api/products/1' \
--header 'Authorization: Bearer {{your_token_here}}'
```
#### POST /api/products
```
curl --location 'http://virtual-store-1.eu-north-1.elasticbeanstalk.com/api/products' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {{your_token_here}}' \
--data '{
    "name": "Test Product",
    "itemsOnStock": 10,
    "itemsSold": 0
}'
```
#### DELETE /api/products/{id}
```
curl --location --request DELETE 'http://virtual-store-1.eu-north-1.elasticbeanstalk.com/api/products/1' \
--header 'Authorization: Bearer {{your_token_here}}'
```
#### POST /api/orders
```
curl --location 'http://virtual-store-1.eu-north-1.elasticbeanstalk.com/api/orders' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {{your_token_here}}' \
--data '{
    "orderItems": [
        {
            "productId": 1,
            "quantity": 2
        },
        {
            "productId": 3,
            "quantity": 2
        }
    ],
    "orderDate":"2024-10-10T12:45:00"
}'
```
#### GET /api/orders
```
curl --location 'http://virtual-store-1.eu-north-1.elasticbeanstalk.com/api/orders' \
--header 'Authorization: Bearer {{your_token_here}}'
```
## Local setup

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run`
4. The APIs will be available at `http://localhost:5000`

## Running Tests

Run `mvn test` to execute the unit tests.