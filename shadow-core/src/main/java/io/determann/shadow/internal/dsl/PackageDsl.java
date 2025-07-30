package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.package_.PackageAnnotateStep;
import io.determann.shadow.api.dsl.package_.PackageInfoRenderable;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class PackageDsl
      implements PackageJavaDocStep,
                 PackageInfoRenderable
{
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private String name;

   public PackageDsl()
   {
   }

   private PackageDsl(PackageDsl other)
   {
      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.name = other.name;
   }

   @Override
   public PackageAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new PackageDsl(this), javadoc, (packageDsl, function) -> packageDsl.javadoc = function);
   }

   @Override
   public PackageAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new PackageDsl(this),
                              annotation,
                              (renderingContext, string) -> '@' + string,
                              packageDsl -> packageDsl.annotations::add);
   }

   @Override
   public PackageAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new PackageDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              packageDsl -> packageDsl.annotations::add);
   }

   @Override
   public PackageInfoRenderable name(String name)
   {
      return setType(new PackageDsl(this), name, (packageDsl, s) -> packageDsl.name = s);
   }

   @Override
   public String renderPackageInfo(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.render(renderingContext));
         sb.append("\n");
      }

      renderElement(sb, annotations, "n", renderingContext, "\n");

      sb.append("package ");
      sb.append(name);
      sb.append(';');

      return sb.toString();
   }
}
