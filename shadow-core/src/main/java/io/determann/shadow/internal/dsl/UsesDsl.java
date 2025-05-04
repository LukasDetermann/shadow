package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.dsl.uses.UsesServiceStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Declared;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.setString;

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
      return setString(new UsesDsl(this),
                       (usesDsl, s) -> usesDsl.serviceName = s,
                       serviceName);

   }

   @Override
   public UsesRenderable service(C_Declared service)
   {
      return setString(new UsesDsl(this),
                       (exportsDsl, s) -> exportsDsl.serviceName = s,
                       (cPackage) -> requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                       service);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      return "uses " + serviceName + ';';
   }
}
