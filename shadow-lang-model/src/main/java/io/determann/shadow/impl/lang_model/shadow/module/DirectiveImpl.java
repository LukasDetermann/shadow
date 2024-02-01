package io.determann.shadow.impl.lang_model.shadow.module;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.module.Directive;

public abstract class DirectiveImpl implements Directive
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
