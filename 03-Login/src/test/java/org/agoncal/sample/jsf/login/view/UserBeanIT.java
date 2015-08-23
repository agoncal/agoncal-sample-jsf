package org.agoncal.sample.jsf.login.view;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.agoncal.sample.jsf.login.model.User;
import org.agoncal.sample.jsf.login.model.UserRole;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserBeanIT
{

   @Inject
   private UserBean userBean;

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap
               .create(JavaArchive.class)
               .addClass(User.class)
               .addClass(UserRole.class)
               .addClass(UserBean.class)
               .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
               .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void should_be_deployed()
   {
      assertNotNull(userBean);
   }

   @Test
   public void should_find_users()
   {
      assertNull(userBean.getUsers());
      userBean.doFindAll();
      assertNotNull(userBean.getUsers());
      assertEquals(0, userBean.getUsers().size());
   }
}
