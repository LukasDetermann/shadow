package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Context;
import io.determann.shadow.api.annotation_processing.Origin;

import javax.lang.model.element.ModuleElement;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public abstract class DirectiveImpl
{
   private final Context context;
   private final ModuleElement declaringModule;

   protected DirectiveImpl(Context context, ModuleElement declaringModule)
   {
      this.context = context;
      this.declaringModule = declaringModule;
   }

   public Context getApi()
   {
      return context;
   }

   public Origin getOrigin()
   {
      return adapt(adapt(getApi()).toElements().getOrigin(declaringModule, getMirror()));
   }

   public abstract ModuleElement.Directive getMirror();

   public ModuleElement getDeclaringModule()
   {
      return declaringModule;
   }
}
