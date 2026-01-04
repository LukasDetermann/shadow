package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.dsl.Dsl.provides;

public class ProvidesImpl extends DirectiveImpl implements Ap.Provides
{
   private final ModuleElement.ProvidesDirective providesDirective;

   public ProvidesImpl(Ap.Context context, ModuleElement.ProvidesDirective providesDirective)
   {
      super(context);
      this.providesDirective = providesDirective;
   }

   @Override
   public Ap.Declared getService()
   {
      return Adapters.adapt(getApi(), providesDirective.getService());
   }

   @Override
   public List<Ap.Declared> getImplementations()
   {
      return providesDirective.getImplementations()
                              .stream()
                              .map(typeElement -> Adapters.<Ap.Declared>adapt(getApi(), typeElement))
                              .toList();
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return provides().service(getService())
                       .with(getImplementations())
                       .renderDeclaration(renderingContext);
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Provides provides &&
             Objects.equals(getImplementations(), provides.getImplementations()) &&
             Objects.equals(getService(), provides.getService());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getImplementations(), getService());
   }

   @Override
   public String toString()
   {
      return "Provides{" +
             "service=" + getService() +
             ", implementations=" + getImplementations() +
             '}';
   }
}
