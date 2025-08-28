package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.query.Implementation;

public class ApImplementation
      extends Implementation
{
   private final AP.Context context;

   public ApImplementation(String name, AP.Context context)
   {
      super(name);
      this.context = context;
   }

   public AP.Context getContext()
   {
      return context;
   }
}
