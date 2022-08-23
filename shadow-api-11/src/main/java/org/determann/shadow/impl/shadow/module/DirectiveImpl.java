package org.determann.shadow.impl.shadow.module;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.module.Directive;

public abstract class DirectiveImpl implements Directive
{
   protected final ShadowApi shadowApi;

   protected DirectiveImpl(ShadowApi shadowApi)
   {
      this.shadowApi = shadowApi;
   }
}
