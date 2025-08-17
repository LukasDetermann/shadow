package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.exports.ExportsPackageStep;
import io.determann.shadow.api.dsl.exports.ExportsTargetStep;
import io.determann.shadow.api.dsl.module.ModuleNameRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class ExportsDsl
      implements ExportsPackageStep,
                 ExportsTargetStep
{
   private Renderable packageName;
   private final List<Renderable> to = new ArrayList<>();

   public ExportsDsl()
   {
   }

   private ExportsDsl(ExportsDsl other)
   {
      this.packageName = other.packageName;
      this.to.addAll(other.to);
   }

   @Override
   public ExportsTargetStep package_(String packageName)
   {
      return setType(new ExportsDsl(this), packageName, (exportsDsl, s) -> exportsDsl.packageName = renderingContext -> s);
   }

   @Override
   public ExportsTargetStep package_(PackageRenderable aPackage)
   {
      return setTypeRenderer(new ExportsDsl(this),
                             aPackage,
                             (renderingContext, packageRenderable) -> packageRenderable.renderQualifiedName(renderingContext),
                             (exportsDsl, s) -> exportsDsl.packageName = s);
   }

   @Override
   public ExportsTargetStep to(String... moduleNames)
   {
      return addArray2(new ExportsDsl(this), moduleNames, (exportsDsl, string) -> exportsDsl.to.add(renderingContext -> string));
   }

   @Override
   public ExportsTargetStep to(List<? extends ModuleNameRenderable> modules)
   {
      return addArrayRenderer(new ExportsDsl(this),
                              modules,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              exportsDsl -> exportsDsl.to::add);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("exports ");
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
      sb.append(to.stream().map(renderable -> renderable.render(renderingContext)).collect(joining(",\n")));
      sb.append(';');

      return sb.toString();
   }
}
