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
      return setTypeRenderer(new EnumConstantDsl(this),
                             javadoc,
                             (classDsl, function) -> classDsl.javadoc = function);
   }

   @Override
   public EnumConstantAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new EnumConstantDsl(this), annotation, classDsl -> classDsl.annotations::add);
   }

   @Override
   public EnumConstantAnnotateStep annotate(C_Annotation... annotation)
   {
      return addArrayRenderer(new EnumConstantDsl(this),
                              annotation,
                              (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                              classDsl -> classDsl.annotations::add);
   }

   @Override
   public EnumConstantRenderable body(String body)
   {
      return setType(new EnumConstantDsl(this), body, (classDsl, function) -> classDsl.body = function);
   }

   @Override
   public EnumConstantParameterStep name(String name)
   {
      return setType(new EnumConstantDsl(this), name, (classDsl, s) -> classDsl.name = s);
   }

   @Override
   public EnumConstantParameterStep parameter(String... parameter)
   {
      return addArrayRenderer(new EnumConstantDsl(this), parameter, methodDsl -> methodDsl.parameters::add);
   }

   @Override
   public EnumConstantParameterStep parameter(C_Parameter... parameter)
   {
      return addArrayRenderer(new EnumConstantDsl(this),
                              parameter,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              constructorDsl -> constructorDsl.parameters::add);
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
