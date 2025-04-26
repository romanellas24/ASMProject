using System.Collections.Generic;
using acmeat.server.menu;

namespace acmeat.server.menu.dataproxy;

//https://stackoverflow.com/questions/14962066/cs0436-type-conflicts-with-the-imported-type
#pragma warning disable 0436
public interface MenuDao
  {
    public Menu GetMenuById(int MenuId);
    public List<Menu> GetMenus();
  }