package com.ai.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class VectorDbInitializer {

//	ONE TIME 
//    @Bean
    CommandLineRunner loadJavaSpringDocs(VectorStore vectorStore) {
        return args -> {
            List<Document> docs = List.of(
                new Document("Spring Boot simplifies Java development by providing auto-configuration and embedded servers."),
                new Document("Dependency Injection in Spring Boot allows better modularity and easier testing."),
                new Document("The @SpringBootApplication annotation combines @Configuration, @EnableAutoConfiguration, and @ComponentScan."),
                new Document("Spring Boot starters are curated dependency sets that simplify project setup."),
                new Document("The application.yml or application.properties file is used to configure Spring Boot applications."),
                new Document("REST APIs in Spring Boot can be built easily using @RestController and @RequestMapping."),
                new Document("Spring Boot Actuator provides production-ready features like health checks and metrics."),
                new Document("The Spring Data JPA module simplifies database operations using repositories and entities."),
                new Document("The @Autowired annotation automatically injects beans into dependent components."),
                new Document("Profiles in Spring Boot allow configuring environments like dev, test, and prod."),
                new Document("You can secure APIs in Spring Boot using Spring Security and JWT authentication."),
                new Document("Spring Boot DevTools automatically restarts the application on code changes."),
                new Document("The CommandLineRunner interface can be used to run code after the application starts."),
                new Document("Spring Boot supports externalized configuration using environment variables and config servers."),
                new Document("Microservices in Spring Boot can communicate through REST APIs or message brokers like Kafka."),
                new Document("You can containerize Spring Boot apps easily using Docker and deploy them to Kubernetes."),
                new Document("The @Value annotation is used to inject property values into Spring-managed beans."),
                new Document("Spring Boot Test utilities make it easy to write unit and integration tests."),
                new Document("Spring Boot integrates seamlessly with databases like MySQL, PostgreSQL, and MongoDB."),
                new Document("The Spring AI project enables integrating AI models like OpenAI or Ollama directly in Spring Boot applications."),
                new Document("If user asks about code examples, provide only java code."),
                new Document("Don't provide any other language code except java."),
                new Document("You can answer any personal questions which were informed by user in previous conversation."),
                new Document("Java is an object-oriented programming language that supports concepts like inheritance, encapsulation, and polymorphism."),
                new Document("The Java Virtual Machine, or JVM, executes compiled bytecode and provides platform independence."),
                new Document("The JDK includes tools like the compiler, debugger, and libraries required for Java development."),
                new Document("Garbage Collection in Java automatically manages memory by reclaiming unused objects."),
                new Document("Java supports multithreading, allowing concurrent execution of multiple parts of a program."),
                new Document("In Java, the 'final' keyword can be used with variables, methods, and classes to prevent modification."),
                new Document("Interfaces in Java define a contract of methods that implementing classes must fulfill."),
                new Document("Abstract classes in Java can have both abstract methods and concrete method implementations."),
                new Document("Java Collections Framework provides classes and interfaces for working with data structures like lists, sets, and maps."),
                new Document("Generics in Java enable type safety and eliminate the need for explicit casting in collections."),
                new Document("Java Streams API allows functional-style operations on collections such as filter, map, and reduce."),
                new Document("Exception handling in Java uses try, catch, finally, throw, and throws keywords."),
                new Document("Java 8 introduced lambda expressions and functional interfaces to support functional programming."),
                new Document("Java uses access modifiers like public, private, protected, and default to control visibility of classes and members."),
                new Document("Serialization in Java allows converting an object into a byte stream for storage or transmission."),
                new Document("Immutable classes in Java, such as String, cannot have their state changed after creation."),
                new Document("Java uses synchronized blocks and methods to handle thread safety in concurrent programs."),
                new Document("The 'volatile' keyword in Java ensures visibility of changes to variables across threads."),
                new Document("Java's Reflection API allows runtime inspection and modification of classes, methods, and fields."),
                new Document("Enums in Java represent a fixed set of constants and can include fields and methods.")
            );

            vectorStore.add(docs);

            System.out.println("Successfully added " + docs.size() + " Spring Boot documents to Vector DB.");
        };
    }
}

