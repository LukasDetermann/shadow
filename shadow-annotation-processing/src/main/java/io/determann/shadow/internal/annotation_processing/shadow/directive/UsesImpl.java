package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

public class UsesImpl extends DirectiveImpl implements Ap.Uses
{
   private final ModuleElement.UsesDirective usesDirective;

   public UsesImpl(Ap.Context context, ModuleElement.UsesDirective usesDirective)
   {
      super(context);
      this.usesDirective = usesDirective;
   }

   @Override
   public Ap.Declared getService()
   {
      return Adapters.adapt(getApi(), usesDirective.getService());
   }

   public ModuleElement.UsesDirective getMirror()
   {
      return usesDirective;
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return Dsl.uses(getService()).renderDeclaration(renderingContext);
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Uses exports &&
             Objects.equals(getService(), exports.getService());

   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getService());
   }

   @Override
   public String toString()
   {
      return "Uses{" +
             "service=" + getService() +
             '}';
   }
}
