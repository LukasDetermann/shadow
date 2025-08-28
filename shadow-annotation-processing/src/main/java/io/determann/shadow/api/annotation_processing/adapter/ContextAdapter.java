package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.ApContextImpl;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ContextAdapter
{
   private final AP.Context context;

   ContextAdapter(AP.Context context)
   {
      this.context = context;
   }

   public Types toTypes()
   {
      return ((ApContextImpl) context).getTypes();
   }

   public Elements toElements()
   {
      return ((ApContextImpl) context).getElements();
   }
}
