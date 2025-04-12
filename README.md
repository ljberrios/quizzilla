# Quizzilla

A fast-paced multiplayer trivia game where players compete in real-time through their command line. Test your knowledge across various categories while racing against others to answer first.

## Features

- Multiplayer support with host-client architecture
- Real-time gameplay with network synchronization
- Multiple trivia categories including US States, US Presidents, World Countries, Science, and Mathematics
- Multiple choice questions with scoring system
- Chat system for player communication
- Sound effects for game events
- Answer cooldown system to prevent spam
- Game state management (lobby, in-progress, ending)
- Score tracking and leaderboard

## Technical Details

- Built with Java using virtual threads for concurrent operations
- Uses sockets for client-server communication
- YAML configuration for game settings and questions
- Object serialization for network packet transmission
- Gradle-based build system

## Commands

- `/play <username>` - Start a new game server and join as host
- `/join <server ip> <username>` - Join an existing game server
- `/ready` - Mark yourself as ready to start the game
- `/answer <answer>` - Submit an answer to the current question
- `/chat <message>` - Send a chat message (you can also type without /chat)
- `/leave` - Leave the current game
- `/ping` - Test server connection
- `/shutdown` - Stop the client and server (host only)

## Build and Run

The project uses Gradle for build management. To build and run:

```bash
./gradlew build
java -jar build/libs/Quizzilla-1.0-SNAPSHOT.jar
```

## System Requirements

- Java 21 or higher (for virtual threads support)
- Network connection for multiplayer functionality