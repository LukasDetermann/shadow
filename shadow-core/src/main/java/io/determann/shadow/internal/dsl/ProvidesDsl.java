package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.provides.ProvidesAdditionalImplementationStep;
import io.determann.shadow.api.dsl.provides.ProvidesImplementationStep;
import io.determann.shadow.api.dsl.provides.ProvidesServiceStep;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class ProvidesDsl
      implements ProvidesServiceStep,
                 ProvidesAdditionalImplementationStep
{
   private Renderable serviceName;
   private final List<Renderable> implementations = new ArrayList<>();

   public ProvidesDsl()
   {
   }

   private ProvidesDsl(ProvidesDsl other)
   {
      this.serviceName = other.serviceName;
      this.implementations.addAll(other.implementations);
   }

   @Override
   public ProvidesImplementationStep service(String serviceName)
   {
      return setType(new ProvidesDsl(this), serviceName, (providesDsl, string) -> providesDsl.serviceName = renderingContext -> string);
   }

   @Override
   public ProvidesImplementationStep service(DeclaredRenderable service)
   {
      return setTypeRenderer(new ProvidesDsl(this),
                             service,
                             (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                             (providesDsl, s) -> providesDsl.serviceName = s);
   }

   @Override
   public ProvidesAdditionalImplementationStep with(String... implementationName)
   {
      return addArray2(new ProvidesDsl(this),
                       implementationName,
                       (providesDsl, string) -> providesDsl.implementations.add(renderingContext -> string));
   }

   @Override
   public ProvidesAdditionalImplementationStep with(List<? extends DeclaredRenderable> implementation)
   {
      return addArrayRenderer(new ProvidesDsl(this),
                              implementation,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              providesDsl -> providesDsl.implementations::add);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("provides ");
      sb.append(serviceName.render(renderingContext));

      if (implementations.size() == 1)
      {
         sb.append(" with ");
         sb.append(implementations.get(0).render(renderingContext));
         sb.append(';');
         return sb.toString();
      }
      sb.append(" with\n");
      sb.append(implementations.stream().map(renderable -> renderable.render(renderingContext)).collect(Collectors.joining(",\n")));
      sb.append(';');

      return sb.toString();
   }
}
