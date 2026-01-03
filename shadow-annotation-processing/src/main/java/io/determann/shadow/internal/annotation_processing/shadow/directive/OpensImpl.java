package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

public class OpensImpl extends DirectiveImpl implements Ap.Opens
{
   private final ModuleElement.OpensDirective opensDirective;

   public OpensImpl(Ap.Context langModelContext, ModuleElement.OpensDirective opensDirective)
   {
      super(langModelContext);
      this.opensDirective = opensDirective;
   }

   @Override
   public Ap.Package getPackage()
   {
      return Adapters.adapt(getApi(), opensDirective.getPackage());
   }

   @Override
   public List<Ap.Module> getTargetModules()
   {
      return opensDirective.getTargetModules()
                           .stream()
                           .map(moduleElement -> Adapters.adapt(getApi(), moduleElement))
                           .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   public ModuleElement.OpensDirective getMirror()
   {
      return opensDirective;
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }

   @Override
   public final boolean equals(Object o)
   {
      return o instanceof Ap.Opens opens &&
             Objects.equals(getPackage(), opens.getPackage()) &&
             Objects.equals(getTargetModules(), opens.getTargetModules());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getPackage(), getTargetModules());
   }

   @Override
   public String toString()
   {
      return "Opens{" +
             "package=" + getPackage() +
             ", targetModules=" + getTargetModules() +
             '}';
   }
}
