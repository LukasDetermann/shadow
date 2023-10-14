package io.determann.shadow.impl.annotation_processing.shadow.module;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.module.Directive;

public abstract class DirectiveImpl implements Directive
{
   private final ShadowApi shadowApi;

   protected DirectiveImpl(ShadowApi shadowApi)
   {
      this.shadowApi = shadowApi;
   }

   @Override
   public ShadowApi getApi()
   {
      return shadowApi;
   }
}
