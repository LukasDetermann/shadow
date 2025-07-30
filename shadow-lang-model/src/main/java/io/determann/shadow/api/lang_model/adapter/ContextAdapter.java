package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.ContextImplementation;
import io.determann.shadow.api.lang_model.LM;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ContextAdapter
{
   private final LM.Context context;

   ContextAdapter(LM.Context context)
   {
      this.context = context;
   }

   public Types toTypes()
   {
      return ((ContextImplementation) context).getTypes();
   }

   public Elements toElements()
   {
      return ((ContextImplementation) context).getElements();
   }
}
