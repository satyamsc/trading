# Trading Application

This is a sample trading application that processes signals and executes trading actions based on the signals received. The application is built using Spring Boot and Java.

## Classes

### `SignalProcessor` (com.bank.trading.service.SignalProcessor)

This class is responsible for processing incoming signals and executing trading actions based on the signals. It implements the `SignalHandler` interface. The `handleSignal` method is used to process signals. It reads the signal configuration from a JSON file, checks if the signal exists in the configuration, and executes the corresponding trading actions. If the signal is not found in the configuration, it cancels any ongoing trades. The class also contains helper methods for executing trading actions and handling exceptions.

### `AlgoMethodInvoker` (com.bank.trading.algo.client.AlgoMethodInvoker)

This class provides methods for invoking methods on an `Algo` object using reflection. It has two main methods: `invokeMethodNoArgs` for invoking a method with no arguments, and `invokeMethodWithArgs` for invoking a method with arguments. The class also contains helper methods for finding the appropriate method to invoke based on the method name and arguments, and for converting primitive types to their corresponding wrapper types.

### `LoadConfig` (com.bank.trading.com.bank.trading.config.LoadConfig)

This class is a Spring configuration class that creates beans for the `Algo` object and reads the signal configuration from a JSON file. It uses the `ResourceLoader` to load the JSON file from the resource folder and creates an `ObjectMapper` to parse the JSON data into a `JsonNode`. The `Algo` object and the parsed `JsonNode` are then used as beans that can be injected into other components.

### `TradingController` (com.bank.trading.controller.TradingController)

This class is a Spring REST controller that handles incoming signal requests. It takes the signal ID as a path variable and passes it to the `SignalHandler` (i.e., `SignalProcessor`) to process the signal. The `SignalHandler` then executes the appropriate trading actions based on the signal ID. The controller returns a `SignalResponse` object indicating that the signal has been processed.

### `GlobalExceptionHandler` (com.bank.trading.exception.GlobalExceptionHandler)

This class is a Spring controller advice that handles exceptions globally for the trading application. It provides exception handling methods for specific exception types (e.g., `ResourceNotFoundException`) and general exceptions. The methods return JSON maps containing the error message extracted from the thrown exceptions.

### `Configuration File`

The `signal_config.json` file contains the configuration for various trading signals and their corresponding actions. Modify this file to add or update signals and actions.

Example `signal_config.json`:
```json
{
  "signals": [
    {
      "id": 1,
      "actions": [
        { "method": "setUp" },
        { "method": "setAlgoParam", "params": [1, 60] },
        { "method": "performCalc" },
        { "method": "submitToMarket" }
      ]
    },
    {
      "id": 2,
      "actions": [
        { "method": "reverse" },
        { "method": "setAlgoParam", "params": [1, 80] },
        { "method": "submitToMarket" }
      ]
    },
    // Add more signals and actions as needed
  ]
}
```

## Installation and Setup

1. Clone the repository to your local machine.

```
git clone https://github.com/your-username/trading-signal-processor.git
```

2. Navigate to the project directory.

```
cd trading-signal-processor
```

3. Build the application using Maven.

```
mvn clean package
```

### `Running the Application`

### `Option 1: Running as a Spring Boot Application`

To run the Trading Signal Processor as a Spring Boot application, use the following command:

```
mvn spring-boot:run
```

The application will start, and you can access it at `http://localhost:8080`.

### `Option 2: Running with Docker`

To run the application using Docker, follow these steps:

1. Build the Docker image:

```
docker build -t trading-signal-processor .
```

2. Run the Docker container:

```
docker run -d -p 8080:8080 trading-signal-processor
```

The application will be accessible at `http://localhost:8080`.

### `Endpoints`

- `POST /signal/{signalId}`: Submit a trading signal with the given `signalId`.


### `Docker Commands`

- Build Docker Image:

```
docker build -t trading-signal-processor .
```

- Run Docker Container:

```
docker run -d -p 8080:8080 trading-signal-processor
```
- Test Application:
```
$ CURL --request POST 'http://localhost:8080/signal/3'
```