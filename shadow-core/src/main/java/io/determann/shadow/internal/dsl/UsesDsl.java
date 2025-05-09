package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.dsl.uses.UsesServiceStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Declared;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.setType;

public class UsesDsl
      implements UsesServiceStep,
                 UsesRenderable
{
   private String serviceName;

   public UsesDsl()
   {
   }

   private UsesDsl(UsesDsl other) {this.serviceName = other.serviceName;}

   @Override
   public UsesRenderable service(String serviceName)
   {
      return setType(new UsesDsl(this), serviceName, (usesDsl, s) -> usesDsl.serviceName = s);
   }

   @Override
   public UsesRenderable service(C_Declared service)
   {
      return setType(new UsesDsl(this),
                     service,
                     (cPackage) -> requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                     (exportsDsl, s) -> exportsDsl.serviceName = s);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      return "uses " + serviceName + ';';
   }
}
