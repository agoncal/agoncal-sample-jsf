package org.agoncal.sample.jsf.login.view;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class FacesProducer
{

   @Produces
   @RequestScoped
   private FacesContext produceFacesContext()
   {
      return FacesContext.getCurrentInstance();
   }
}