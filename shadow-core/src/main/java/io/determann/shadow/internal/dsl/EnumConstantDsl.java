package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantAnnotateStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantParameterStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantRenderable;
import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class EnumConstantDsl
      implements EnumConstantJavaDocStep,
                 EnumConstantParameterStep
{
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private String body;
   private String name;
   private final List<Renderable> parameters = new ArrayList<>();

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
      return addArrayRenderer(new EnumConstantDsl(this),
                              annotation,
                              (renderingContext, string) -> '@' + string,
                              classDsl -> classDsl.annotations::add);
   }

   @Override
   public EnumConstantAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new EnumConstantDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumConstantDsl -> enumConstantDsl.annotations::add);
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
      return addArrayRenderer(new EnumConstantDsl(this), parameter, enumConstantDsl -> enumConstantDsl.parameters::add);
   }

   @Override
   public EnumConstantParameterStep parameter(List<? extends ParameterRenderable> parameter)
   {
      return addArrayRenderer(new EnumConstantDsl(this),
                              parameter,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumConstantDsl -> enumConstantDsl.parameters::add);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.render(renderingContext));
         sb.append("\n");
      }

      renderElement(sb, annotations, "\n", renderingContext, "\n");

      sb.append(name);
      renderElement(sb, "<", parameters, ">", renderingContext, ", ");

      if (body != null)
      {
         sb.append("{\n");
         sb.append(body);
         sb.append("\n}");
      }
      return sb.toString();
   }
}
