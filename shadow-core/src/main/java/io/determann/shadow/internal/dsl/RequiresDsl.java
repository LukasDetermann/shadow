package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.module.ModuleRenderable;
import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.requires.RequiresNameStep;
import io.determann.shadow.api.dsl.requires.RequiresRenderable;

import static io.determann.shadow.internal.dsl.DslSupport.setType;
import static io.determann.shadow.internal.dsl.DslSupport.setTypeRenderer;

public class RequiresDsl
      implements RequiresModifierStep,
                 RequiresRenderable
{
   private String modifier;
   private Renderable dependency;

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
      return setType(new RequiresDsl(this), "transitive", (requiresDsl, s) -> requiresDsl.modifier = s);
   }

   @Override
   public RequiresNameStep static_()
   {
      return setType(new RequiresDsl(this), "static", (requiresDsl, s) -> requiresDsl.modifier = s);
   }

   @Override
   public RequiresRenderable dependency(String dependency)
   {
      return setType(new RequiresDsl(this), dependency, (requiresDsl, s) -> requiresDsl.dependency = renderingContext -> s);
   }

   @Override
   public RequiresRenderable dependency(ModuleRenderable dependency)
   {
      return setTypeRenderer(new RequiresDsl(this),
                             dependency,
                             (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                             (requiresDsl, s) -> requiresDsl.dependency = s);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("requires ");
      if (modifier != null)
      {
         sb.append(modifier);
         sb.append(' ');
      }
      sb.append(dependency.render(renderingContext));
      sb.append(';');

      return sb.toString();
   }
}
