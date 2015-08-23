package org.agoncal.sample.jsf.login.utils;

import com.thedeanda.lorem.Lorem;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceProducer
{

   @Produces
   @RequestScoped
   private FacesContext produceFacesContext()
   {
      return FacesContext.getCurrentInstance();
   }

//   @Produces
//   @RequestScoped
//   private HttpServletRequest produceRequest()
//   {
//      return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//   }

   @Produces
   @RequestScoped
   private HttpServletResponse produceResponse()
   {
      return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
   }

   @Produces
   @Named("lorem")
   private String produceLorem()
   {
      return Lorem.getParagraphs(1, 3);
   }
}