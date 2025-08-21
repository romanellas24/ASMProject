# Acmeat

This project was generated with dotnet-cli version 9.0.104


## Build

Run `dotnet build` to build the solution. The build artifacts will be stored in the `bin/` directory of every microservice.


## Further help

To get more help on the dotnet-cli use `dotnet help` or go check out the [dotnet-cli Overview and Command Reference](https://learn.microsoft.com/en-us/dotnet/core/tools/) page.



## Expand the solution
If new domains or microservices have to be implemented to expand amceat functionalities, you must follow this procedure:

1. Create the microservice in the "acmeat-be/api" if it is an api or in "amceat-be/server" if it is a backend service

2. This project uses the DDD design pattern along with CQRS pattern for more info: [DDD-oriented microservices](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/ddd-oriented-microservice) [CQRS patter](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

3. Once created the microservice to benefit auto-build (if you are using VScode), sintax-highlight, debugger and other features you should add the microservice to the overall solution in this way:

<code> dotnet sln add path-to-csproj-file.csproj</code>
