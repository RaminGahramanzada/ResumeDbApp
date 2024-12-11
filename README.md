# ResumeDbApp

ResumeDbApp is a user database management application designed to manage user information efficiently. This project leverages Java and JDBC technology for robust database operations.

---

## 🚀 Features

- **User Management:** Easily add, update, delete, and list user information.
- **Database Integration:** Persistent data storage using MySQL.
- **JDBC Connectivity:** Efficient handling of database connections and SQL queries.
- **Modular Design:** Clear and maintainable architecture with separated concerns.

---

## 🛠️ Technologies Used

- **Java**: Core programming language.
- **MySQL**: Database management system.
- **JDBC**: Database connectivity.
- **JUnit**: Unit testing framework.
- **dotenv-java**: Environment variable management library.

---

## 📋 Requirements

Ensure the following tools are installed on your system:

- Java Development Kit (JDK) 21 or higher
- MySQL Server
- Maven (for dependency management and project build)

---

## ⚙️ Setup

Follow these steps to set up and run the project:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/RaminGahramanzada/ResumeDbApp.git
   cd ResumeDbApp
   ```

2. **Configure the Database:**
   - Create a `src/main/resources/config.env` file and add the following:
     ```env
     DB_URL=jdbc:mysql://localhost:3306/your_database_name
     DB_USERNAME=your_username
     DB_PASSWORD=your_password
     ```

3. **Install Dependencies:**
   ```bash
   mvn clean install
   ```

4. **Run the Project:**
   ```bash
   mvn exec:java -Dexec.mainClass="Main"
   ```

---

## 🗂️ Project Structure

- **`Main`**: Entry point of the application.
- **`repository`**: Handles database operations.
- **`service`**: Implements business logic.
- **`model`**: Defines data models.
- **`test`**: Contains unit tests.

---

## 💡 Example Usage

Here are some examples of how to use the application:

1. **List All Users:**
   ```java
   List<User> users = userRepository.getAll();
   users.forEach(System.out::println);
   ```

2. **Add a User:**
   ```java
   User user = new User(0, "John", "Doe", "john.doe@example.com");
   userRepository.add(user);
   ```

3. **Update a User:**
   ```java
   User user = userRepository.getById(1);
   user.setName("Jane");
   userRepository.update(user);
   ```

4. **Delete a User:**
   ```java
   userRepository.remove(1);
   ```

---

## 🧪 Testing

Run unit tests using the following command:
```bash
mvn test
```

---

## 🤝 Contributing

Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-name
   ```
3. Make your changes and commit:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push to the branch:
   ```bash
   git push origin feature-name
   ```
5. Submit a pull request.

---

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## 📧 Contact

For any inquiries, feel free to reach out to the author:

- **Name:** Ramin Gahramanzada
- **GitHub:** [RaminGahramanzada](https://github.com/RaminGahramanzada)

