package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;

public abstract class DirectiveImpl
{
   private final Ap.Context context;

   protected DirectiveImpl(Ap.Context context)
   {
      this.context = context;
   }

   public Ap.Context getApi()
   {
      return context;
   }
}
