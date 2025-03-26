using acmeat.db.mysql;
using acmeat.server.order.dataproxy;
using acmeat.server.order.datawriter;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436

class Program
{
    static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);

        // Add services to the container.
        // Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
        builder.Services.AddControllers();
        builder.Services.AddEndpointsApiExplorer();
        builder.Services.AddLogging(builder => builder.AddConsole());
        builder.Services.AddTransient<OrderReader>();
        builder.Services.AddTransient<OrderDataWriter>();
        builder.Services.AddTransient<MysqlClient>();
        builder.Services.AddTransient<MySqlContext>();
        builder.Services.AddSwaggerGen(options=>{
            //https://github.com/domaindrivendev/Swashbuckle.AspNetCore/issues/1607#issuecomment-607170559
            options.CustomSchemaIds(type => type.ToString());
        });
    
        var app = builder.Build();

        // Configure the HTTP request pipeline.
        if (app.Environment.IsDevelopment())
        {
            app.UseSwagger();
            app.UseSwaggerUI();
        }

        app.UseAuthorization();

        app.MapControllers();


        app.Run();
    }
}

