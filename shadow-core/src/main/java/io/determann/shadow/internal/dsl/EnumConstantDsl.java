package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.enum_constant.EnumConstantAnnotateStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantParameterStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class EnumConstantDsl
      implements EnumConstantJavaDocStep,
                 EnumConstantParameterStep
{
   private Function<RenderingContext, String> javadoc;
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private String body;
   private String name;
   private final List<Function<RenderingContext, String>> parameters = new ArrayList<>();

   public EnumConstantDsl()
   {
   }

   private EnumConstantDsl(EnumConstantDsl other)
   {
      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.body = other.body;
      this.name = other.name;
      this.parameters.addAll(other.parameters);
   }

   @Override
   public EnumConstantAnnotateStep javadoc(String javadoc)
   {
      return set(new EnumConstantDsl(this),
                 (classDsl, function) -> classDsl.javadoc = function,
                 javadoc);
   }

   @Override
   public EnumConstantAnnotateStep annotate(String... annotation)
   {
      return add(new EnumConstantDsl(this), classDsl -> classDsl.annotations::add, annotation);
   }

   @Override
   public EnumConstantAnnotateStep annotate(C_Annotation... annotation)
   {
      return add(new EnumConstantDsl(this),
                 classDsl -> classDsl.annotations::add,
                 (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                 annotation);
   }

   @Override
   public EnumConstantRenderable body(String body)
   {
      return setString(new EnumConstantDsl(this),
                       (classDsl, function) -> classDsl.body = function,
                       body);
   }

   @Override
   public EnumConstantParameterStep name(String name)
   {
      return setString(new EnumConstantDsl(this),
                       (classDsl, s) -> classDsl.name = s,
                       name);
   }

   @Override
   public EnumConstantParameterStep parameter(String... parameter)
   {
      return add(new EnumConstantDsl(this), methodDsl -> methodDsl.parameters::add, parameter);
   }

   @Override
   public EnumConstantParameterStep parameter(C_Parameter... parameter)
   {
      return add(new EnumConstantDsl(this),
                 constructorDsl -> constructorDsl.parameters::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 parameter);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.apply(renderingContext));
         sb.append("\s");
      }
      if (!annotations.isEmpty())
      {
         sb.append(this.annotations.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining("\n")));
         sb.append('\n');
      }
      sb.append(name);
      if (!parameters.isEmpty())
      {
         sb.append('(');
         sb.append(parameters.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
         sb.append(')');
      }

      if (body != null)
      {
         sb.append("{\n");
         sb.append(body);
         sb.append("\n}");
      }
      return sb.toString();
   }
}
