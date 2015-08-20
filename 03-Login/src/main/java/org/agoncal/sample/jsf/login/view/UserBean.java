package org.agoncal.sample.jsf.login.view;

import org.agoncal.sample.jsf.login.model.User;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Named
@SessionScoped
public class UserBean implements Serializable
{

   @Inject
   private BeanManager beanManager;

   @PersistenceContext(unitName = "sampleJSFLoginPU")
   private EntityManager em;

   private User user;


   private boolean loggedIn;

   public String doNothing()
   {
      return null;
   }

   public String doPersist()
   {
      em.persist(user);
      return null;
   }

   public String doLogin()
   {
      loggedIn = true;
      return "index";
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

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }
}
