package io.determann.shadow.internal.annotation_processing.dsl;


import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.annotation_processing.dsl.package_.PackageAnnotateStep;
import io.determann.shadow.api.annotation_processing.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.annotation_processing.dsl.package_.PackageRenderable;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.annotation_processing.dsl.DslSupport.*;

public class PackageDsl
      implements PackageJavaDocStep,
                 PackageRenderable
{
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private String name;

   public PackageDsl() {}

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
   public PackageRenderable name(String name)
   {
      return setType(new PackageDsl(this), name, (packageDsl, s) -> packageDsl.name = s);
   }

   @Override
   public String renderPackageInfo(RenderingContext context)
   {
      if (name == null || name.isEmpty())
      {
         throw new IllegalStateException("cant render a package-info for an unnamed package");
      }

      context.addSurrounding(this);
      context.setCurrentPackageName(name);

      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.render(context));
         sb.append("\n");
      }

      renderElement(sb, annotations, "\n", context, "\n");

      sb.append("package ");
      sb.append(name);
      sb.append(';');

      return sb.toString();
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      return name;
   }
}
