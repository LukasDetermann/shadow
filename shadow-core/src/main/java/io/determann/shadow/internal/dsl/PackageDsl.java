package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.package_.PackageAnnotateStep;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class PackageDsl
      implements PackageJavaDocStep,
                 PackageRenderable
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
      return addArrayRenderer(new PackageDsl(this), annotation, packageDsl -> packageDsl.annotations::add);
   }

   @Override
   public PackageAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new PackageDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              packageDsl -> packageDsl.annotations::add);
   }

   @Override
   public PackageAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new PackageDsl(this),
                      annotation,
                      packageDsl -> packageDsl.annotations::add);
   }

   @Override
   public PackageRenderable name(String name)
   {
      return setType(new PackageDsl(this), name, (packageDsl, s) -> packageDsl.name = s);
   }

   @Override
   public String render(RenderingContext renderingContext)
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
