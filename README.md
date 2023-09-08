<h1 align="center">
  📘 Your little library 👩🏾
</h1>


## 🔍 About the project

<p>
    Your Little Library is a web application designed to help you manage your collection of books and authors. It allows you to create, organize, and track books and authors with a user-friendly interface.
</p>

## 🖥️ Technologies used

- **Spring & Spring MVC** - to set up the web application.

- **H2 Database** - to store the data in in-memory database.

- **Spring Data JPA** - to access the data from h2 database easily.

- **Thymeleaf** - for server-side rendering of web pages.

- **Bootstrap & Bootstrap Icons** - to make the application responsive and nice looking.

- **JUnit 5 & Mockito** - for unit testing.

- **JaCoCo** - to generate code coverage reports.

- **Docker** - to create a docker image, so application can be easily run.

## 🐳 Run the application with docker

1. **Download the image**:

   ```
   docker pull globuss23/your-little-library
   ```

2. **Run the container**:

   ```
   docker run -d -p 2137:8080 --name your-little-library globuss23/your-little-library
   ```

3. **Access the application**:

   Now you should have application running. Open your web browser and go to `http://localhost:2137` to access Your Little Library.