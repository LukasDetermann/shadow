package com.derivandi.api.adapter;

import com.derivandi.api.processor.SimpleContext;
import com.derivandi.internal.processor.ContextImpl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ContextAdapter
{
   private final SimpleContext context;

   ContextAdapter(SimpleContext context)
   {
      this.context = context;
   }

   public Types toTypes()
   {
      return ((ContextImpl) context).getTypes();
   }

   public Elements toElements()
   {
      return ((ContextImpl) context).getElements();
   }

   public ProcessingEnvironment toProcessingEnvironment()
   {
      return ((ContextImpl) context).getProcessingEnv();
   }

   public RoundEnvironment toRoundEnvironment()
   {
      return ((ContextImpl) context).getRoundEnv();
   }
}
