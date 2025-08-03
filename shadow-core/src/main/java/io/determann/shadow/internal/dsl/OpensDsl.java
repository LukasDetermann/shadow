package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.module.ModuleNameRenderable;
import io.determann.shadow.api.dsl.opens.OpensPackageStep;
import io.determann.shadow.api.dsl.opens.OpensTargetStep;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class OpensDsl
      implements OpensPackageStep,
                 OpensTargetStep
{
   private Renderable packageName;
   private final List<Renderable> to = new ArrayList<>();

   public OpensDsl()
   {
   }

   private OpensDsl(OpensDsl other)
   {
      this.packageName = other.packageName;
      this.to.addAll(other.to);
   }

   @Override
   public OpensTargetStep package_(String packageName)
   {
      return setType(new OpensDsl(this), packageName, (opensDsl, s) -> opensDsl.packageName = renderingContext -> s);
   }

   @Override
   public OpensTargetStep package_(PackageRenderable aPackage)
   {
      return setTypeRenderer(new OpensDsl(this),
                             aPackage,
                             (renderingContext, packageRenderable) -> packageRenderable.renderQualifiedName(renderingContext),
                             (opensDsl, s) -> opensDsl.packageName = s);
   }

   @Override
   public OpensTargetStep to(String... moduleNames)
   {
      return addArray2(new OpensDsl(this), moduleNames, (opensDsl, string) -> opensDsl.to.add(renderingContext -> string));
   }

   @Override
   public OpensTargetStep to(List<? extends ModuleNameRenderable> modules)
   {
      return addArrayRenderer(new OpensDsl(this),
                              modules,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              opensDsl -> opensDsl.to::add);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("opens ");
      sb.append(packageName.render(renderingContext));

      if (to.isEmpty())
      {
         sb.append(';');
         return sb.toString();
      }
      if (to.size() == 1)
      {
         sb.append(" to ");
         sb.append(to.get(0).render(renderingContext));
         sb.append(';');
         return sb.toString();
      }
      sb.append(" to\n");
      sb.append(to.stream().map(renderable -> renderable.render(renderingContext)).collect(Collectors.joining(",\n")));
      sb.append(';');

      return sb.toString();
   }
}
