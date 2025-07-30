package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LM;

public abstract class DirectiveImpl
{
   private final LM.Context context;

   protected DirectiveImpl(LM.Context context)
   {
      this.context = context;
   }

   public LM.Context getApi()
   {
      return context;
   }
}
