using acmeat.server.order.client;
using acmeat.server.local.client;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<LocalClient>();

builder.Services.AddOptions<LocalClientOptions>().BindConfiguration(nameof(LocalClientOptions));
var app = builder.Build();

// Configure the HTTP request pipeline.
app.UsePathBase("/Local");

app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/Local/swagger/v1/swagger.json", "My API V1");
});

app.UseAuthorization();

app.MapControllers();


app.Run();

