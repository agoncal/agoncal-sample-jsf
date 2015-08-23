package org.agoncal.sample.jsf.login.view;

import static org.junit.Assert.assertNotEquals;

import javax.inject.Inject;

import org.agoncal.sample.jsf.login.model.User;
import org.agoncal.sample.jsf.login.model.UserRole;
import org.agoncal.sample.jsf.login.utils.CatchException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AccountBeanIT
{

   @Inject
   private AccountBean accountBean;

   // @Deployment
   // public static JavaArchive createDeployment()
   // {
   // return ShrinkWrap
   // .create(JavaArchive.class)
   // .addPackage(User.class.getPackage())
   // .addPackage(CatchException.class.getPackage())
   // .addPackage(AccountBean.class.getPackage())
   // .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
   // .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   // }

   @Deployment
   public static WebArchive createDeployment()
   {
      return ShrinkWrap.create(WebArchive.class)
               .addPackage(User.class.getPackage())
               .addPackage(CatchException.class.getPackage())
               .addPackage(AccountBean.class.getPackage())
               .addAsManifestResource("META-INF/persistence.xml", "META-INF/persistence.xml")
               .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
               .addAsWebInfResource(
                        new StringAsset("<faces-config version=\"2.2\"/>"),
                        "faces-config.xml");
   }

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(accountBean);
   }

   @Test
   public void should_signup_a_user_and_check_passoword_is_diggested()
   {
      User user = new User("login", "firstName", "lastName", "email", UserRole.USER);
      accountBean.setUser(user);
      accountBean.setPassword1("password");
      accountBean.doSignup();
      assertNotEquals("password", accountBean.getUser().getPassword());
   }
}
