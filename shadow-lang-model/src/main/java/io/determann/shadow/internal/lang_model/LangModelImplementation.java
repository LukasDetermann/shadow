package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;

public class LangModelImplementation extends Implementation
{
   private final LM_Context context;

   public LangModelImplementation(String name, LM_Context context)
   {
      super(name);
      this.context = context;
   }

   public LM_Context getContext()
   {
      return context;
   }
}
