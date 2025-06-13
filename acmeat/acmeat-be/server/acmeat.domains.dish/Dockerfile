# Learn about building .NET container images: 123MB
# https://github.com/dotnet/dotnet-docker/blob/main/samples/README.md
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build

#https://stackoverflow.com/questions/76155802/c-sharp-protobuf-project-fails-to-build-in-docker
# RUN apk add --no-cache libc6-compat
# ARG TARGETARCH
WORKDIR /source
ENV ASPNETCORE_URLS=http://+:5206
# Copy project file and restore as distinct layers. Must not copy client directory
COPY . ./
# RUN dotnet restore -a $TARGETARCH

RUN dotnet restore

# Copy source code and publish app
# COPY --link . .
# RUN dotnet publish -a $TARGETARCH -o /app
RUN dotnet build -c Release
RUN dotnet publish --no-build -c Release -p:PublishSourceLink=false -p:EnableSourceLink=false -p:IncludeSource=false -o /app


# Enable globalization and time zones:
# https://github.com/dotnet/dotnet-docker/blob/main/samples/enable-globalization.md
# Runtime stage
FROM mcr.microsoft.com/dotnet/aspnet:9.0
EXPOSE 5206
WORKDIR /app
COPY --link --from=build /app .
USER $APP_UID
ENTRYPOINT [ "./dishmanager","--server.urls", "https://+:5206"]

# CMD ["dotnet","run"]


# after build to run it : docker run -p 8081:80 -e ASPNETCORE_URLS=http://+:80 {{imagename}}

