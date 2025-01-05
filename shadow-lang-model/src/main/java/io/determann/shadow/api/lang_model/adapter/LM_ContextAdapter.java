package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.LM_ContextImplementation;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class LM_ContextAdapter
{
   private final LM_Context context;

   LM_ContextAdapter(LM_Context context)
   {
      this.context = context;
   }

   public Types toTypes()
   {
      return ((LM_ContextImplementation) context).getTypes();
   }

   public Elements toElements()
   {
      return ((LM_ContextImplementation) context).getElements();
   }
}
