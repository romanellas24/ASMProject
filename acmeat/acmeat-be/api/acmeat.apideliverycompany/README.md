# Acmeat.apideliverycompany

This project was generated with dotnet-cli version 9.0.104

## Development server

Run `dotnet watch` for a dev server. Navigate to `http://localhost:5268/`. The application will automatically reload if you change any of the source files.
If you don't see any changes press (Ctrl + R) to reload.


## Build

Run `dotnet build` to build the project. The build artifacts will be stored in the `bin/` directory.


## Further help

To get more help on the dotnet-cli use `dotnet help` or go check out the [dotnet-cli Overview and Command Reference](https://learn.microsoft.com/en-us/dotnet/core/tools/) page.

## Build With docker
Open a terminal in the "acmeat.apideliverycompany/" folder and then run the following commands:

<code>docker build -t your-image-name .</code>

In this way you will be able to build the backend microservice in production configuration.
Ater that use:

<code> docker push you-image-name </code>

## Deploy on Kubernetes
To deploy on kubernetes just apply these two yaml files:

<code> kubectl apply -f deploy.yaml -f service.yaml </code>

or with microk8s:

<code> microk8s kubectl apply -f deploy.yaml -f service.yaml </code>
