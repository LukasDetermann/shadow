package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LangModelContext;

public abstract class DirectiveImpl
{
   private final LangModelContext context;

   protected DirectiveImpl(LangModelContext context)
   {
      this.context = context;
   }

   public LangModelContext getApi()
   {
      return context;
   }
}
