using MySqlConnector;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddMySqlDataSource(builder.Configuration.GetConnectionString("DbConnectionString")!);
Console.WriteLine($"Connection string: {builder.Configuration.GetConnectionString("DbConnectionString")}" );
builder.Services.AddLogging(builder => builder.AddConsole());
// builder.Services.AddScoped<MySqlContext>();

// builder.Services.AddControllers();
// builder.Services.AddEndpointsApiExplorer();
// builder.Services.AddSwaggerGen();
var app = builder.Build();

// Configure the HTTP request pipeline.
// if (app.Environment.IsDevelopment())
// {
//    app.UseSwagger();
//     app.UseSwaggerUI();
// }

app.UseAuthorization();

// app.MapControllers();


app.Run();

