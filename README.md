# Project Title

## Challenge

A successful promotional campaign can bring many advantages to businesses looking to acquire new customers, increase sales, or clear out stock. Our goal is to create a shopping cart system with a promotional campaign and differentiated pricing based on user type.

### **Promotions:**

1. **Get 3 for the Price of 2**:

   Customers who add multiple products to their cart will receive the third product for free. The free product will always be the lowest-priced item.

    - Buy 1, pay for 1.
    - Buy 2, pay for 2.
    - Buy 3, pay for 2 (lowest-priced item is free).
    - Buy 4, pay for 3, and so on.
2. **VIP Discount**:

   VIP customers enjoy a **15% discount** on all purchases. However, VIP customers **cannot combine this discount with the "Get 3 for the Price of 2" promotion**.


The system must **automatically calculate and suggest the best pricing option** (either using the VIP discount or the "Get 3 for 2" promotion) based on the user's cart contents and user type (VIP or common).

---

## **Key Requirements**

- The API should be able to **Add** and **Remove** items from the shopping cart.
- The API should identify whether the customer is **VIP** or **common**.
- The API should calculate the **total price** based on:
    - The **"Get 3 for 2" promotion** for common users.
    - The **VIP discount** (15%) for VIP customers.
    - For VIP customers, the API should decide whether it's better to apply the 15% discount or the "Get 3 for 2" promotion, and recommend the best deal.

## How to Run

### Docker

To run the project using Docker, follow these steps:

1. **Clone the Repository**:
    ```sh
    git clone https://github.com/yourusername/yourproject.git
    cd yourproject
    ```

2. **Build the Docker image and run it using docker-compose**:
    ```sh
    docker-compose up --build
    ```
   Note: Please make sure Docker engine is running on your machine before running docker-compose.

3. **Verify APIs on Swagger**: 
   Open your browser and access http://localhost:8080/api/swagger-ui/index.html

### Existing Products on DB
In the classpath of this project there is an sql script called data.sql. In there I insert a couple products to be used for testing purposes. Please use the following product skus to add a product in the cart:
```sh
id|name        |sku  |price  |
--+------------+-----+-------+
 1|Smartphone 1|PR001|3000.00|
 2|Tablet 1    |PR002|4000.00|
 3|Laptop 1    |PR003|3500.00|
 4|keyboard 1  |PR004| 200.00|
 5|smartwatch 1|PR005| 800.00|
```
### API Calls

Here are some example API calls you can make to interact with the system:

1. **Add Product to Cart**:
    ```sh
    curl --location --request PUT 'http://localhost:8080/api/v1/carts' \
    --header 'Content-Type: application/json' \
    --data '{
        "uuid": "123",
        "sku": "PR001",
        "quantity": 2
    }'
    ```
   Note: the "uuid" is Optional, if the client sends it, it will associate this product into an existing Cart with that uuid, if not, it will create a new Cart and associate the product.
   In the future, this association would need to have security protection to only allow associate a product to an existing cart with a given uuid in case it belongs to the same User.
   Also, please make sure to use a product from the list.

2. **Get Cart Details**:
    ```sh
    curl -X GET "http://localhost:8080/api/v1/carts/123"
    ```

## System Design

### Architecture

The system is designed using a microservices architecture, with the following components:

- **Controller Layer**: Manages APIs to add, remove and list products in the Cart.
- **Adapter Layer**: Isolates the Controller and Service Layer, so it encapsulates data translation and decouple both layers. 
- **Service Layer**: Manages entities, and handles all domain logic. 
- **Repository Layer**: Execute queries on DB.

For the Campaign Discount logic, I opted to use the Mediator Design Pattern as it seems to fit well its requirements. Also, all that code are not defined as Spring Beans as the mediator and its Campaigns holds state, and being in a singleton Spring bean would allow for Race Condition issues, hence why I created a Factory class(CampaignManagerFactory) for the CampaignManager class. 

### Data Flow

1. **Add Product to Cart**:
    - The client sends a PUT request to the API Gateway.
    - The API Gateway routes the request to the Cart Service.
    - The Cart Service updates the cart and saves the changes to the database.

2. **Get Cart Details**:
    - The client sends a GET request to the API Gateway.
    - The API Gateway routes the request to the Cart Service.
    - The Cart Service retrieves the cart details from the database and returns them to the client.

### Technologies Used

- **Spring Boot**: For building the microservices.
- **Docker**: For containerizing the application.
- **PostgreSQL**: As the database for storing cart and product data.
- **Testcontainers**: For integration testing with real database instances.

## What Can Improve

1. **Caching**: Introduce caching mechanisms to reduce database load and improve response times.
2. **Security**: Implement authentication and authorization to secure the API endpoints.
3. **Monitoring and Logging**: Integrate monitoring and logging tools to track system performance and diagnose issues.
4. **Automated Testing**: Increase test coverage with more unit and integration tests to ensure system reliability.
5. **Remove boilerplate code**: Remove boilerplate code using Lombok or Java Records.
6. **Do we need the Adapter Layer?**: If we want a more straight to the point design we can consider getting rid of the Adapter layer, such as CartServiceAdaptar, and just map DTO <-> Entities mapping using frameworks like ModelMapper.
---
