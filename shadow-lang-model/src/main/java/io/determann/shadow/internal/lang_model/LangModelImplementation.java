package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.query.Implementation;

public class LangModelImplementation extends Implementation
{
   private final LM.Context context;

   public LangModelImplementation(String name, LM.Context context)
   {
      super(name);
      this.context = context;
   }

   public LM.Context getContext()
   {
      return context;
   }
}
