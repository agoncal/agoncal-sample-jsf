package org.agoncal.sample.jsf.login.view;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.agoncal.sample.jsf.login.model.User;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Named
@SessionScoped
@Transactional
public class AccountBean implements Serializable
{

   @Inject
   private BeanManager beanManager;

   @Inject
   private FacesContext facesContext;

   @PersistenceContext(unitName = "sampleJSFLoginPU")
   private EntityManager em;

   private User user = new User();

   private boolean loggedIn;

   public String doNothing()
   {
      return null;
   }

   public String doPersistOrMerge()
   {
      if (user.getId() == null)
      {
         em.persist(user);
         facesContext.addMessage(null, new FacesMessage("Successful", "User created " + user.getFirstName()));
      }
      else
      {
         em.merge(user);
         facesContext.addMessage(null, new FacesMessage("Successful", "User updated " + user.getFirstName()));
      }
      loggedIn = true;
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
      Bean<?> myBean = beanManager.getBeans(AccountBean.class).iterator().next();
      ctx.destroy(myBean);
      return "index";
   }

   public boolean isLoggedIn()
   {
      return loggedIn;
   }

   public void setLoggedIn(boolean loggedIn)
   {
      this.loggedIn = loggedIn;
   }

   public User getUser()
   {
      return user;
   }

   public void setUser(User user)
   {
      this.user = user;
   }
}
