# Acmeat.domains.user

This project was generated with dotnet-cli version 9.0.104

## Development server

Run `dotnet watch` for a dev server. Navigate to `http://localhost:5202/`. The application will automatically reload if you change any of the source files.
If you don't see any changes press (Ctrl + R) to reload.


## Build

Run `dotnet build` to build the project. The build artifacts will be stored in the `bin/` directory.


## Further help

To get more help on the dotnet-cli use `dotnet help` or go check out the [dotnet-cli Overview and Command Reference](https://learn.microsoft.com/en-us/dotnet/core/tools/) page.

## Build With docker
Open a terminal in the "acmeat.domains.user/" folder and then run the following commands:

<code>docker build -t your-image-name .</code>

In this way you will be able to build the backend microservice in production configuration.
Ater that use:

<code> docker push you-image-name </code>

## Deploy on Kubernetes
To deploy on kubernetes just apply these two yaml files:

<code> kubectl apply -f deploy.yaml -f service.yaml </code>

or with microk8s:

<code> microk8s kubectl apply -f deploy.yaml -f service.yaml </code>


## Build the Client
To build the acmeat.domains.user.client use the following procedure:

1. Once you have modified all you needed inside the "acmeat.domains.user.client" folder you should update the client version in the "*client.csproj" file 

2. Run `dotnet pack` this will give you a nuget package that contains the updated client

3. Go inside "bin/Release" folder and then run this command: 
<code> dotnet nuget push your-package-name.nupkg --api-key [api-key] --source https://api.nuget.org/v3/index.json<code>

4. After the package has been pushed, remove the "/bin" and "/obj" folder and then run:

<code>dotnet build</code>

This prevents errors during the building phase in the outer scope of acmeat.domains.user
