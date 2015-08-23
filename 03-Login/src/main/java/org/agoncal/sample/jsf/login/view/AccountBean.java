package org.agoncal.sample.jsf.login.view;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

   @Inject
   private HttpServletResponse response;

   @Inject
   private HttpServletRequest request;

   @PersistenceContext(unitName = "sampleJSFLoginPU")
   private EntityManager em;

   // Logged user
   private User user = new User();

   // Checks if the user is logged in and if he/she is an administrator (UserRole.Admin)
   private boolean loggedIn;
   private boolean admin;

   private String password1;
   private String password2;
   // Remember me and cookie
   private static final String COOKIE_NAME = "JSFSample";
   private static final int COOKIE_AGE = 60; //Expires after 60 seconds or even 2_592_000 for one month

   private boolean rememberMe;

   @PostConstruct
   private void checkIfUserHasRememberMeCookie()
   {
      String coockieValue = getCookieValue();
      if (coockieValue == null)
         return;

      TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_UUID, User.class);
      query.setParameter("uuid", coockieValue);
      try
      {
         user = query.getSingleResult();
         // If the user is an administrator
         if (user.getRole().equals(UserRole.ADMIN))
            admin = true;
         // The user is now logged in
         loggedIn = true;
      }
      catch (NoResultException e)
      {
         // The user maybe has an old coockie, let's get rid of it
         removeCookie();
      }
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

   public String doSignin()
   {
      TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
      query.setParameter("login", user.getLogin());
      query.setParameter("password", user.digestPassword(user.getPassword()));
      try
      {
         user = query.getSingleResult();
         // If the user is an administrator
         if (user.getRole().equals(UserRole.ADMIN))
            admin = true;
         // If the user has clicked on remember me
         if (rememberMe)
         {
            String uuid = UUID.randomUUID().toString();
            user.setUuid(uuid);
            addCookie(uuid);
         }
         else
         {
            user.setUuid(null);
            removeCookie();
         }
         // The user is now logged in
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

   public String doLogoutAndRemoveCookie()
   {
      removeCookie();
      user.setUuid(null);
      em.merge(user);
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

   public String doSearch()
   {
      return "search";
   }

   // Cookie
   private String getCookieValue()
   {
      Cookie[] cookies = request.getCookies();
      if (cookies != null)
      {
         for (Cookie cookie : cookies)
         {
            if (COOKIE_NAME.equals(cookie.getName()))
            {
               return cookie.getValue();
            }
         }
      }
      return null;
   }

   private void addCookie(String value)
   {
      Cookie cookie = new Cookie(COOKIE_NAME, value);
      cookie.setPath("/");
      cookie.setMaxAge(COOKIE_AGE);
      response.addCookie(cookie);
   }

   private void removeCookie()
   {
      Cookie cookie = new Cookie(COOKIE_NAME, null);
      cookie.setPath("/");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
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

   public boolean isRememberMe()
   {
      return rememberMe;
   }

   public void setRememberMe(boolean rememberMe)
   {
      this.rememberMe = rememberMe;
   }
}
