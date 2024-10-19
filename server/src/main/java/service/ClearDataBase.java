package service;

import dataaccess.DataAccessException;

public class ClearDataBase {
  public Object clearDataBase() throws DataAccessException {
    if(Boolean.TRUE){
      throw new DataAccessException("something went wrong");
    }
    /**
     * now just need to add the code that goes to dataAccess and then deletes all the game, user
     * and auth data.
     */
    Object object = "{}";
    return object;
  }

}
