# online-store REST API
RESTful API was created for learning purposes which simulates the shopping process with Products and Orders.

## Features

- RESTful routing
- Models with proper relationships (one-to-many, one-to-one)
- Custom Exception handling
- RESTful errors
- H2 database
- Lombok
- MapStruct
- JUnit, Mockito

## List of endpoints

`all endpoints are preceded by /api/v1/`
### Products

| Method   | URI                               | Description                                             
|----------|-----------------------------------|---------------------------------------------------------|
| `GET`    | `products`                        | `Get list of products`   
| `GET`    | `products/{productId}`            | `Get single product`    
| `POST`   | `products`                        | `Create product` 
| `PUT`    | `products/{productId}`            | `Update product details` 
| `DELETE` | `products/{productId}`            | `Delete product`       

### Orders

| Method  | URI                                | Description                                             
|---------|------------------------------------|---------------------------------------------------------|
| `GET`   | `orders`                                     | `Get list of orders`   
| `GET`   | `orders/{orderId}`                           | `Get single order`    
| `PUT`   | `orders/{orderId}/clear`                     | `Clear single order` 
| `DELETE`| `orders/{orderId}`                           | `Delete order` 
| `POST`  | `orders`                                     | `Create order`       
| `POST`  | `orders/add-product`                         | `Insert product to specified order`       
| `DELETE`| `orders/{orderId/delete-product/{productId}` | `Delete product from specified order`       
| `PUT`   | `orders/change-product-quantity`             | `Update product quantity in specified order`       