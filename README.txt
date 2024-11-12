This application is a Spring Boot REST API that processes receipt data by generating a unique ID for each receipt submitted. After processing, you can use the ID to retrieve associated points, which are calculated based on the details in the receipt.

Running the Application

1. Docker:
   - Ensure Docker is installed and running.
   - Build and run the application using the following commands:
     docker build -t receiptservice .
     docker run -p 8080:8080 receiptservice
     

2. Non-Docker (Optional):
   - Ensure Maven and Java (JDK 17 or higher) are installed.
   - Run the application with:
     mvn clean install
     mvn spring-boot:run
     

API Endpoints

1. Process a Receipt (POST Request)

Endpoint: `/receipts/process`  
Method: `POST`  
Description: This endpoint processes receipt data and returns a unique ID for the receipt.

- Request:
  - URL: `http://localhost:8080/receipts/process`
  - Method: `POST`    

- Response:
  - Body: JSON with the generated ID.


- Instructions:
  - Use Postman or another REST client to send a `POST` request to `http://localhost:8080/receipts/process`.
  - The response will contain the unique ID, which you can use for the `GET` request below.

 2. Retrieve Points by Receipt ID (GET Request)

Endpoint: `/receipts/{id}/points`  
Method: `GET`  
Description: Retrieve points associated with the receipt ID generated in the `POST` request.

- Request:
  - URL: `http://localhost:8080/receipts/{id}/points`  
    Replace `{id}` with the actual ID returned from the `POST /receipts/process` request.

- Response:
  - Body: JSON with points associated with the receipt.


- Instructions:
  - Once you have the ID from the `POST` request, enter the URL `http://localhost:8080/receipts/{id}/points` in your browser or use a `GET` request in Postman with the full URL (replacing `{id}` with the actual receipt ID).
  - The response will contain the points calculated for the receipt.

 
Code Logic Overview

The application is structured in three main components:

1. Controller Layer:
   - `ReceiptController` manages HTTP requests. It defines two endpoints:
     - `POST /receipts/process`: Accepts a receipt JSON object, processes it, and returns a unique ID.
     - `GET /receipts/{id}/points`: Accepts a receipt ID and returns points associated with that ID.

2. Service Layer:
   - `ReceiptService` contains the business logic. In `processReceipt`, it generates a unique ID for each receipt based on details like the retailer name, purchase date, and item information.
   - In `getPointsById`, it calculates points based on receipt data, such as:
     - Points for each item, factoring in item price and name.
     - Additional points for specific receipt characteristics (e.g., retailer or purchase date).

3. Data Flow:
   - POST `/receipts/process`: Receives receipt details, processes them, generates an ID, and returns it.
   - GET `/receipts/{id}/points`: Uses the generated ID to look up and return points calculated based on receipt attributes.

This modular setup ensures that each component is independent, maintainable, and testable.

Docker Information:
The Dockerfile uses the OpenJDK 17 slim image to run the application in a lightweight environment. The jar file is copied from the target folder, and port 8080 is exposed, allowing access to the Spring Boot application.


Example of a receipt in JSON format:
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },{
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },{
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },{
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },{
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}

Example of id received after the POST method on Postman:
{
    "id": "85f708d7-4567-4949-b6e7-b2bb4bfe14cd"
}

Example of url after getting the id from the POST request on Postman:

http://localhost:8080/receipts/85f708d7-4567-4949-b6e7-b2bb4bfe14cd/points

Example of points displayed of the specific receipt id:
{
    "points": 28
}
