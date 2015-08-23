package org.agoncal.sample.jsf.login.utils;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author Antonio Goncalves - http://www.antoniogoncalves.org --
 * 
 *         This interceptor catches exception and displayes them in a JSF page
 */
@Interceptor
@CatchException
public class CatchExceptionInterceptor implements Serializable
{

   // ======================================
   // = Attributes =
   // ======================================

   @Inject
   private FacesContext context;

   // ======================================
   // = Business methods =
   // ======================================

   @AroundInvoke
   public Object catchException(InvocationContext ic) throws Exception
   {
      try
      {
         return ic.proceed();
      }
      catch (Throwable e)
      {
         context.addMessage(null, new FacesMessage(SEVERITY_ERROR, e.getMessage(), null));
         return null;
      }
   }
}
