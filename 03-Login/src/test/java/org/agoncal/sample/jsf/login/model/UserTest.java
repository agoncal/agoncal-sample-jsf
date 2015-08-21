package org.agoncal.sample.jsf.login.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

public class UserTest
{
   @Test
   public void digestPassword() throws Exception
   {
      String digestedPassword = new User().digestPassword("Dummy");
      Assert.assertNotEquals("Dummy", digestedPassword);
   }
}
