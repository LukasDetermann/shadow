package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.package_.PackageAnnotateStep;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class PackageDsl implements PackageJavaDocStep, PackageRenderable
{
   private Function<RenderingContext, String> javadoc;
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
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
      return set(new PackageDsl(this),
                 (packageDsl, function) -> packageDsl.javadoc = function,
                 javadoc);

   }

   @Override
   public PackageAnnotateStep annotate(String... annotation)
   {
      return add(new PackageDsl(this), packageDsl -> packageDsl.annotations::add, annotation);
   }

   @Override
   public PackageAnnotateStep annotate(C_Annotation... annotation)
   {
      return add(new PackageDsl(this),
                 packageDsl -> packageDsl.annotations::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 annotation);

   }

   @Override
   public PackageRenderable name(String name)
   {
      return setString(new PackageDsl(this),
                       (packageDsl, s) -> packageDsl.name = s,
                       name);

   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.apply(renderingContext));
         sb.append("\n");
      }
      if (!annotations.isEmpty())
      {
         sb.append(this.annotations.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining("\n")));
         sb.append('\n');
      }
      sb.append("package ");
      sb.append(name);
      sb.append(';');

      return sb.toString();
   }
}
