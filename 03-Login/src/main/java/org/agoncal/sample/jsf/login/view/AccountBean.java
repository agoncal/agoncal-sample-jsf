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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.agoncal.sample.jsf.login.model.User;
import org.agoncal.sample.jsf.login.model.UserRole;

import com.thedeanda.lorem.Lorem;

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
   private boolean admin;
   private String password1;
   private String password2;

   public String doNothing()
   {
      return null;
   }

   public String doSignup()
   {
      user.setPassword(password1);
      em.persist(user);
      resetPasswords();
      facesContext.addMessage(null,
               new FacesMessage("Successful", "Hi " + user.getFirstName() + ", welcome to this website"));
      loggedIn = true;
      if (user.getRole().equals(UserRole.ADMIN))
         admin = true;
      return "index";
   }

   public String doUpdateProfile()
   {
      if (password1 != null && !password1.isEmpty())
         user.setPassword(user.digestPassword(password1));
      em.merge(user);
      resetPasswords();
      facesContext.addMessage(null,
               new FacesMessage("Successful", "Profile has been updated for " + user.getFirstName()));
      return null;
   }

   public String doLogin()
   {
      TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
      query.setParameter("login", user.getLogin());
      query.setParameter("password", user.digestPassword(user.getPassword()));
      try
      {
         user = query.getSingleResult();
         if (user.getRole().equals(UserRole.ADMIN))
            admin = true;
         loggedIn = true;
         return "index";
      }
      catch (NoResultException e)
      {
         facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Wrong user/password",
                  "Check your inputs or ask for a new password"));
         return null;
      }
   }

   public String doLogout()
   {
      AlterableContext ctx = (AlterableContext) beanManager.getContext(SessionScoped.class);
      Bean<?> myBean = beanManager.getBeans(AccountBean.class).iterator().next();
      ctx.destroy(myBean);
      return "index";
   }

   public String doForgotPassword()
   {
      TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_EMAIL, User.class);
      query.setParameter("email", user.getEmail());
      try
      {
         user = query.getSingleResult();
         String temporaryPassword = Lorem.getWords(1);
         user.setPassword(user.digestPassword(temporaryPassword));
         em.merge(user);
         facesContext.addMessage(null, new FacesMessage("Email sent",
                  "An email has been sent to " + user.getEmail() + " with temporary password :" + temporaryPassword));
         // send an email with the password "dummyPassword"
         return doLogout();
      }
      catch (NoResultException e)
      {
         facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unknown email",
                  "This email address is unknonw in our system"));
         return null;
      }
   }

   private void resetPasswords()
   {
      password1 = null;
      password2 = null;
   }

   public boolean isLoggedIn()
   {
      return loggedIn;
   }

   public void setLoggedIn(boolean loggedIn)
   {
      this.loggedIn = loggedIn;
   }

   public boolean isAdmin()
   {
      return admin;
   }

   public void setAdmin(boolean admin)
   {
      this.admin = admin;
   }

   public User getUser()
   {
      return user;
   }

   public void setUser(User user)
   {
      this.user = user;
   }

   public String getPassword1()
   {
      return password1;
   }

   public void setPassword1(String password1)
   {
      this.password1 = password1;
   }

   public String getPassword2()
   {
      return password2;
   }

   public void setPassword2(String password2)
   {
      this.password2 = password2;
   }

   public UserRole[] getRoles()
   {
      return UserRole.values();
   }
}
