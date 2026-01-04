package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.ModuleElement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.dsl.Dsl.exports;

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
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return exports().package_(getPackage())
                      .to(getTargetModules())
                      .renderDeclaration(renderingContext);
   }

   @Override
   public final boolean equals(Object o)
   {
      return o instanceof Ap.Exports exports &&
             Objects.equals(getPackage(), exports.getPackage()) &&
             Objects.equals(getTargetModules(), exports.getTargetModules());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getPackage(), getTargetModules());
   }

   @Override
   public String toString()
   {
      return "Exports{" +
             "package=" + getPackage() +
             ", targetModules=" + getTargetModules() +
             '}';
   }
}
