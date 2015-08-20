package org.agoncal.sample.jsf.login.view;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.agoncal.sample.jsf.login.model.User;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Named
@RequestScoped
public class UsersBean
{

   @PersistenceContext(unitName = "sampleJSFLoginPU")
   private EntityManager em;

   private List<User> users;

   public String doFindAll()
   {
      users = em.createNamedQuery(User.FIND_ALL, User.class).getResultList();
      return "users";
   }

   public List<User> getUsers()
   {
      return users;
   }

   public void setUsers(List<User> users)
   {
      this.users = users;
   }
}
