package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LM_Context;

public abstract class DirectiveImpl
{
   private final LM_Context context;

   protected DirectiveImpl(LM_Context context)
   {
      this.context = context;
   }

   public LM_Context getApi()
   {
      return context;
   }
}
