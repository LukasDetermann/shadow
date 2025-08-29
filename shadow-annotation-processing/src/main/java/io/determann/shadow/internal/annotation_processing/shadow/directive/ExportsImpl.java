package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.shadow.directive.ExportsSupport;

import javax.lang.model.element.ModuleElement;
import java.util.Collections;
import java.util.List;

public class ExportsImpl extends DirectiveImpl implements Ap.Exports
{

   private final ModuleElement.ExportsDirective exportsDirective;

   public ExportsImpl(Ap.Context context, ModuleElement.ExportsDirective exportsDirective)
   {
      super(context);
      this.exportsDirective = exportsDirective;
   }

   @Override
   public Ap.Package getPackage()
   {
      return Adapters.adapt(getApi(), exportsDirective.getPackage());
   }

   @Override
   public List<Ap.Module> getTargetModules()
   {
      return exportsDirective.getTargetModules() == null ?
             Collections.emptyList() :
             exportsDirective.getTargetModules()
                             .stream()
                             .map(moduleElement -> Adapters.adapt(getApi(), moduleElement))
                             .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   public ModuleElement.ExportsDirective getMirror()
   {
      return exportsDirective;
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }

   @Override
   public boolean equals(Object other)
   {
      return ExportsSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ExportsSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ExportsSupport.toString(this);
   }
}
