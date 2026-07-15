# Film Forum

Film Forum is a web forum application focused on movies and cinema discussions. Users can register, log in, create posts, browse other users' posts, comment on discussions, and like content they enjoy. The platform has public pages for anonymous visitors, private pages for authenticated users, and administrative features for managing users and posts. The project is built with Java 17, Spring Boot, Hibernate/JPA, MariaDB, Thymeleaf, and a layered architecture with controllers, services, repositories, and models.

The application supports core forum features such as user profiles, post filtering and sorting, top recent posts, and most commented posts. Admin users can promote users, block or unblock accounts, and manage forum content. The database schema and test data are included in the repository, together with a database diagram. The REST API is intended for external consumers and testing through Swagger, while the MVC layer provides browser pages rendered with Thymeleaf.

## Swagger Documentation

After running the application, open Swagger here:

```text
http://localhost:8080/swagger-ui.html
```
