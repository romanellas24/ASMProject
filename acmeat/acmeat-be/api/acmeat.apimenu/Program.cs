using acmeat.server.order.client;
using acmeat.server.menu.client;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<MenuClient>();

builder.Services.AddOptions<MenuClientOptions>().BindConfiguration(nameof(MenuClientOptions));
var app = builder.Build();

// Configure the HTTP request pipeline.
app.UsePathBase("/Menu");

app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/Menu/swagger/v1/swagger.json", "My API V1");
});

app.UseAuthorization();

app.MapControllers();


app.Run();

