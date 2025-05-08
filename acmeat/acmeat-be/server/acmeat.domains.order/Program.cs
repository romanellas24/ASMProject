using acmeat.db.mysql;
using acmeat.domains.order.Services;
using acmeat.server.local.client;
using acmeat.server.order.client;
using acmeat.server.order.dataproxy;
using acmeat.server.order.datawriter;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Server.Kestrel.Core;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

#pragma warning disable CS0436 // Type conflicts with imported type

var builder = WebApplication.CreateBuilder(args);


builder.Services.AddLogging(builder => builder.AddConsole());

builder.Services.AddOptions<DbConnectionOptions>().BindConfiguration(nameof(DbConnectionOptions));

// builder.Services.AddScoped<MySqlContext>();

builder.Services.AddScoped<OrderReader>();
builder.Services.AddScoped<OrderDataWriter>();
builder.Services.AddScoped<MysqlClient>();
builder.Services.AddScoped<LocalClient>();
builder.Services.AddOptions<LocalClientOptions>().BindConfiguration(nameof(LocalClientOptions));

builder.Services.AddEndpointsApiExplorer();


// Add services to the container. DEPENDENCIES MUST BE WRITTEN BEFORE THIS COMMENT!!!
builder.Services.AddGrpc();

var app = builder.Build();



// Configure the HTTP request pipeline.
app.MapGrpcService<GrpcOrderManagerService>();
app.MapGet("/", () => "Communication with gRPC endpoints must be made through a gRPC client. To learn how to create a client, visit: https://go.microsoft.com/fwlink/?linkid=2086909");

app.Run();
