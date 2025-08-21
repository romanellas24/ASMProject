# Acmeat.domains.deliverycompany

This project was generated with dotnet-cli version 9.0.104

## Development server

Run `dotnet watch` for a dev server. Navigate to `http://localhost:5205/`. The application will automatically reload if you change any of the source files.
If you don't see any changes press (Ctrl + R) to reload.


## Build

Run `dotnet build` to build the project. The build artifacts will be stored in the `bin/` directory.


## Further help

To get more help on the dotnet-cli use `dotnet help` or go check out the [dotnet-cli Overview and Command Reference](https://learn.microsoft.com/en-us/dotnet/core/tools/) page.

## Build With docker
Open a terminal in the "acmeat.domains.deliverycompany/" folder and then run the following commands:

`docker build -t your-image-name .`

In this way you will be able to build the backend microservice in production configuration.
Ater that use:

` docker push you-image-name `

## Deploy on Kubernetes
To deploy on kubernetes just apply these two yaml files:

` kubectl apply -f deploy.yaml -f service.yaml `

or with microk8s:

` microk8s kubectl apply -f deploy.yaml -f service.yaml `


## Build the Client
To build the acmeat.domains.deliverycompany.client use the following procedure:

1. Once you have modified all you needed inside the "acmeat.domains.deliverycompany.client" folder you should update the client version in the "*client.csproj" file 

2. Run `dotnet pack` this will give you a nuget package that contains the updated client

3. Go inside "bin/Release" folder and then run this command: 
` dotnet nuget push your-package-name.nupkg --api-key [api-key] --source https://api.nuget.org/v3/index.json`

4. After the package has been pushed, remove the "/bin" and "/obj" folder and then run:

`dotnet build`

This prevents errors during the building phase in the outer scope of acmeat.domains.deliverycompany
