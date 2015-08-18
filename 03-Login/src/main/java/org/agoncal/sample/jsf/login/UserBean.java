package org.agoncal.sample.jsf.login;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Named
@SessionScoped
public class UserBean implements Serializable
{

   @Inject
   private BeanManager beanManager;

   private boolean loggedIn;

   public String doLogin()
   {
      loggedIn = true;
      return null;
   }

   public String doLogout()
   {
      AlterableContext ctx = (AlterableContext) beanManager.getContext(SessionScoped.class);
      Bean<?> myBean = beanManager.getBeans(UserBean.class).iterator().next();
      ctx.destroy(myBean);
      return null;
   }

   public boolean isLoggedIn() {
      return loggedIn;
   }

   public void setLoggedIn(boolean loggedIn) {
      this.loggedIn = loggedIn;
   }
}
