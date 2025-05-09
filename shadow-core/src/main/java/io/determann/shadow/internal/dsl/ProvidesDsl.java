package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.provides.ProvidesAdditionalImplementationStep;
import io.determann.shadow.api.dsl.provides.ProvidesImplementationStep;
import io.determann.shadow.api.dsl.provides.ProvidesServiceStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Declared;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.addArray;
import static io.determann.shadow.internal.dsl.DslSupport.setType;

public class ProvidesDsl
      implements ProvidesServiceStep,
                 ProvidesImplementationStep,
                 ProvidesAdditionalImplementationStep
{
   private String serviceName;
   private final List<String> implementations = new ArrayList<>();

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
      return setType(new ProvidesDsl(this), serviceName, (providesDsl, s) -> providesDsl.serviceName = s);
   }

   @Override
   public ProvidesImplementationStep service(C_Declared service)
   {
      return setType(new ProvidesDsl(this),
                     service,
                     (cInterface) -> requestOrThrow(cInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                     (providesDsl, s) -> providesDsl.serviceName = s);
   }

   @Override
   public ProvidesAdditionalImplementationStep with(String... implementationName)
   {
      return addArray(new ProvidesDsl(this), implementationName, providesDsl -> providesDsl.implementations::add);
   }

   @Override
   public ProvidesAdditionalImplementationStep with(C_Declared... implementation)
   {
      return addArray(new ProvidesDsl(this),
                      implementation,
                      cClass -> requestOrThrow(cClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                      providesDsl -> providesDsl.implementations::add);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("provides ");
      sb.append(serviceName);

      if (implementations.size() == 1)
      {
         sb.append(" with ");
         sb.append(implementations.get(0));
         sb.append(';');
         return sb.toString();
      }
      sb.append(" with\n");
      sb.append(String.join("\n", implementations));
      sb.append(';');
      return sb.toString();
   }
}
