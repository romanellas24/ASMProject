using System.Collections.Generic;
using acmeat.server.deliverycompany;

namespace acmeat.server.deliverycompany.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public interface DeliveryCompanyDao
  {
    public DeliveryCompany GetDeliveryCompanyById(int DeliveryCompanyId);
    public List<DeliveryCompany> GetDeliveryCompanys();
  }