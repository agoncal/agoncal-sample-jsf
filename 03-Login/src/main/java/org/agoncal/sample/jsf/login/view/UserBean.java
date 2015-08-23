package org.agoncal.sample.jsf.login.view;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.agoncal.sample.jsf.login.model.User;
import org.agoncal.sample.jsf.login.utils.CatchException;

/**
 * @author Antonio Goncalves - http://www.antoniogoncalves.org --
 *
 *         This request scoped JSF Backing bean deals with all the user's access database. You can see it as a stateless
 *         transactional service.
 */
@Named
@RequestScoped
@CatchException
public class UserBean
{

   // ======================================
   // = Attributes =
   // ======================================

   @PersistenceContext(unitName = "sampleJSFLoginPU")
   private EntityManager em;

   private List<User> users;

   // ======================================
   // = Business methods =
   // ======================================

   public String doFindAll()
   {
      users = em.createNamedQuery(User.FIND_ALL, User.class).getResultList();
      return "users";
   }

   // ======================================
   // = Getters & setters =
   // ======================================

   public List<User> getUsers()
   {
      return users;
   }

   public void setUsers(List<User> users)
   {
      this.users = users;
   }
}
