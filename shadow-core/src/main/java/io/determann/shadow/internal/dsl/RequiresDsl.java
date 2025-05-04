package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.requires.RequiresNameStep;
import io.determann.shadow.api.dsl.requires.RequiresRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_Module;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.setString;

public class RequiresDsl
      implements RequiresModifierStep,
                 RequiresRenderable
{
   private String modifier;
   private String dependency;

   public RequiresDsl()
   {
   }

   private RequiresDsl(RequiresDsl other)
   {
      this.modifier = other.modifier;
      this.dependency = other.dependency;
   }

   @Override
   public RequiresNameStep transitive()
   {
      return setString(new RequiresDsl(this),
                       (requiresDsl, s) -> requiresDsl.modifier = s,
                       "transitive");
   }

   @Override
   public RequiresNameStep static_()
   {
      return setString(new RequiresDsl(this),
                       (requiresDsl, s) -> requiresDsl.modifier = s,
                       "static");
   }

   @Override
   public RequiresRenderable dependency(String dependency)
   {
      return setString(new RequiresDsl(this),
                       (requiresDsl, s) -> requiresDsl.dependency = s,
                       dependency);
   }

   @Override
   public RequiresRenderable dependency(C_Module dependency)
   {
      return setString(new RequiresDsl(this),
                       (requiresDsl, s) -> requiresDsl.dependency = s,
                       module1 -> requestOrThrow(module1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                       dependency);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("requires ");
      if (modifier != null)
      {
         sb.append(modifier);
         sb.append(' ');
      }
      sb.append(dependency);
      sb.append(';');

      return sb.toString();
   }
}
