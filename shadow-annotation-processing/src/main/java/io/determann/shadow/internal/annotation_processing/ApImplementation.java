package io.determann.shadow.internal.annotation_processing;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.query.Implementation;

public class ApImplementation
      extends Implementation
{
   private final Ap.Context context;

   ApImplementation(String name, Ap.Context context)
   {
      super(name);
      this.context = context;
   }

   public Ap.Context getContext()
   {
      return context;
   }
}
