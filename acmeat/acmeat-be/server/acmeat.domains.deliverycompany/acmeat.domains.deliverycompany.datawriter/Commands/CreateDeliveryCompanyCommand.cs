using System;

namespace acmeat.server.deliverycompany.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class CreateNewDeliveryCompanyCommand : ICommand
{

    public CreateNewDeliveryCompanyCommand(DeliveryCompany deliverycompany){
        this.Id = Guid.NewGuid();
        this.deliverycompany =deliverycompany;
    }
    public Guid Id {get;set;}
    public DeliveryCompany deliverycompany {get;set;}
}