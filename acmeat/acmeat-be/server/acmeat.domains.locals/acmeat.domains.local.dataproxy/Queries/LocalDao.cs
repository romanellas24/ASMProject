using System.Collections.Generic;
using acmeat.server.local;

namespace acmeat.server.local.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public interface LocalDao
  {
    public Local GetLocalById(int LocalId);
  public Local GetLocalByUrl(string Url);
    public List<Local> GetLocals();
  }