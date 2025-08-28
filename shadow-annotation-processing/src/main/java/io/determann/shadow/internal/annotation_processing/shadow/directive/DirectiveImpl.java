package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.AP;

public abstract class DirectiveImpl
{
   private final AP.Context context;

   protected DirectiveImpl(AP.Context context)
   {
      this.context = context;
   }

   public AP.Context getApi()
   {
      return context;
   }
}
