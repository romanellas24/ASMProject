using acmeat.server.order.client;
using acmeat.server.user.client;
using Microsoft.AspNetCore.HttpOverrides;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<UserClient>();

builder.Services.AddOptions<UserClientOptions>().BindConfiguration(nameof(UserClientOptions));
var app = builder.Build();

// Configure the HTTP request pipeline.
// if (app.Environment.IsDevelopment())
// {

app.UsePathBase("/User");

app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/User/swagger/v1/swagger.json", "My API V1");
});

// }
// else
// {
//     app.UseSwagger( options =>
//     {
//         options.
//     });
//     app.UseSwaggerUI();
//  }

app.UseAuthorization();

app.MapControllers();


app.Run();

