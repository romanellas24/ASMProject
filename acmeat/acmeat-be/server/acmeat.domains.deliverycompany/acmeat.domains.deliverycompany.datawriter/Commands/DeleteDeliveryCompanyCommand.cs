using System;

namespace acmeat.server.deliverycompany.datawriter;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public class DeleteNewDeliveryCompanyCommand : ICommand
{

    public DeleteNewDeliveryCompanyCommand(DeliveryCompany deliverycompany){
        this.Id = Guid.NewGuid();
        this.deliverycompany =deliverycompany;
    }

    public DeleteNewDeliveryCompanyCommand(int DeliveryCompanyId){
        this.Id = Guid.NewGuid();
        this.DeliveryCompanyId =DeliveryCompanyId;
    }
    public Guid Id {get;set;}
    public int DeliveryCompanyId{get;set;}
    public DeliveryCompany? deliverycompany{get;set;}
}