# Java File Server

## Overview

A client-server application built with Java that allows for remote file storage and management. The server uses multithreading to handle multiple client connections simultaneously, processing requests for file operations. The client provides a simple command-line interface to interact with the server.

This project was built to demonstrate core Java networking, concurrency, and file I/O concepts.

## Key Features

* **Client-Server Architecture**: Uses Java Sockets for reliable TCP communication between the server and one or more clients.
* **Multithreaded Server**: The server runs a new thread for each client connection, allowing it to handle multiple requests concurrently.
* **REST-like File Operations**: Supports fundamental file management commands:
    * `PUT`: Upload a file to the server. The server assigns a unique ID to the file.
    * `GET`: Download a file from the server, identifiable either by its name or its unique ID.
    * `DELETE`: Remove a file from the server, identifiable either by its name or its unique ID.
* **Persistent File Index**: The server maintains an index (a map) of file IDs to filenames. This index is persisted to disk using **Java Serialization**, ensuring that file metadata is not lost when the server restarts.
* **File I/O**: Utilizes the **Java NIO** (`java.nio.file`) package for efficient and robust file handling on both the client and server side.

## Tech Stack

* **Core**: Java
* **Networking**: Java Sockets
* **Concurrency**: Java Threads
* **File I/O**: Java NIO (Files, Path)
* **Persistence**: Java Serialization
* **Build/Test**: Gradle, JUnit 5, provided by Hyperskill

## How to Run


### Running the Server

1.  Clone the repository.
2.  Open a terminal and navigate to the project root.
3.  Build the project:
    ```sh
    ./gradlew build
    ```
4.  Run the server main class:
    ```sh
    java -cp build/classes/java/main server.Main
    ```
5.  The server will start and print: `Server started!`

### Running the Client

1.  Open a **new** terminal window.
2.  Navigate to the same project root.
3.  Run the client main class:
    ```sh
    java -cp build/classes/java/main client.Main
    ```
4.  Follow the on-screen prompts to interact with the server:
    ```
    Enter action (1 - get a file, 2 - create a file, 3 - delete a file):
    ```