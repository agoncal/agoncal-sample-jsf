package org.agoncal.sample.jsf.login.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sun.misc.BASE64Encoder;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Entity
@NamedQueries({
         @NamedQuery(name = User.FIND_BY_LOGIN, query = "SELECT u FROM User u WHERE u.login = :login"),
         @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.login = :login AND u.password = :password"),
         @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u")
})
public class User implements Serializable
{

   // ======================================
   // = Attributes =
   // ======================================

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column(length = 10, nullable = false)
   private String login;

   @Column(length = 256, nullable = false)
   @NotNull
   @Size(min = 1, max = 256)
   private String password;

   @Column(length = 50, name = "first_name", nullable = false)
   @NotNull
   @Size(min = 2, max = 50)
   private String firstName;

   @Column(length = 50, name = "last_name", nullable = false)
   @NotNull
   @Size(min = 2, max = 50)
   private String lastName;

   private String email;

   private UserRole role;

   // ======================================
   // = Constants =
   // ======================================

   public static final String FIND_BY_LOGIN = "User.findByLogin";
   public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";
   public static final String FIND_ALL = "User.findAll";

   // ======================================
   // = Business methods =
   // ======================================

   /**
    * Digest password with <code>SHA-256</code> then encode it with Base64.
    *
    * @param plainTextPassword the password to digest and encode
    * @return digested password
    * @throws RuntimeException if password could not be digested
    */
   public String digestPassword(String plainTextPassword)
   {
      try
      {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         md.update(plainTextPassword.getBytes("UTF-8"));
         byte[] passwordDigest = md.digest();
         return new BASE64Encoder().encode(passwordDigest);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Exception encoding password", e);
      }
   }

   /**
    * Given a password, this method then checks if it matches the user
    *
    * @param pwd Password
    * @throws RuntimeException thrown if the password is empty or different than the one store in database
    */
   public void matchPassword(String pwd)
   {
      if (pwd == null || "".equals(pwd))
         throw new RuntimeException("Invalid password");
      String digestedPwd = digestPassword(pwd);

      // The password entered by the customer is not the same stored in database
      if (!digestedPwd.equals(password))
         throw new RuntimeException("Passwords don't match");
   }

   // ======================================
   // = Getters & setters =
   // ======================================

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   public String getLogin()
   {
      return login;
   }

   public void setLogin(String login)
   {
      this.login = login;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public String getFirstName()
   {
      return firstName;
   }

   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }

   public String getLastName()
   {
      return lastName;
   }

   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public UserRole getRole() {
      return role;
   }

   public void setRole(UserRole role) {
      this.role = role;
   }

   // ======================================
   // = Methods hash, equals, toString =
   // ======================================

   @Override
   public final boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof User))
         return false;
      User customer = (User) o;
      return Objects.equals(login, customer.login);
   }

   @Override
   public final int hashCode()
   {
      return Objects.hash(login);
   }

   @Override
   public String toString()
   {
      return firstName + ' ' + lastName + " (" + login + ")";
   }
}
