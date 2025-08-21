# Acmeat.db

This project was generated with dotnet-cli version 9.0.104


## Build

Run `dotnet build` to build the project. The build artifacts will be stored in the `bin/` directory.


## Further help

To get more help on the dotnet-cli use `dotnet help` or go check out the [dotnet-cli Overview and Command Reference](https://learn.microsoft.com/en-us/dotnet/core/tools/) page.



## Build the Client
To build the acmeat.domains.user.client use the following procedure:

1. Once you have modified all you needed inside the "acmeat.domains.user.client" folder you should update the client version in the "*client.csproj" file 

2. Run `dotnet pack` this will give you a nuget package that contains the updated client

3. Go inside "bin/Release" folder and then run this command: 
<code> dotnet nuget push your-package-name.nupkg --api-key [api-key] --source https://api.nuget.org/v3/index.json<code>


