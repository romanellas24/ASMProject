using acmeat.server.order.client;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi

builder.Services.AddLogging(builder => builder.AddConsole());
builder.Services.AddScoped<OrderClient>();

builder.Services.AddOptions<OrderClientOptions>().BindConfiguration(nameof(OrderClientOptions));
builder.Services.AddOptions<BankConnectionOptions>().BindConfiguration(nameof(BankConnectionOptions));

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
var app = builder.Build();
// Configure the HTTP request pipeline.
app.UsePathBase("/Order");

app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/Order/swagger/v1/swagger.json", "My API V1");
});

app.UseAuthorization();

app.MapControllers();


app.Run();

