using acmeat.server.order.client;
using acmeat.server.dish.client;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<DishClient>();

builder.Services.AddOptions<DishClientOptions>().BindConfiguration(nameof(DishClientOptions));
var app = builder.Build();

// Configure the HTTP request pipeline.
app.UsePathBase("/Dish");

app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/Dish/swagger/v1/swagger.json", "My API V1");
});

app.UseAuthorization();

app.MapControllers();


app.Run();

