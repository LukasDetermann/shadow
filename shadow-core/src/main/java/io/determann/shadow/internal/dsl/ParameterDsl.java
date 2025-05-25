package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.parameter.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class ParameterDsl
      implements ParameterAnnotateStep,
                 ParameterNameStep,
                 ParameterVarargsStep
{
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private boolean isFinal = false;
   private Function<RenderingContext, String> type;
   private String name;
   private boolean isVarArgs = false;

   public ParameterDsl()
   {
   }

   private ParameterDsl(ParameterDsl other)
   {
      this.annotations.addAll(other.annotations);
      this.isFinal = other.isFinal;
      this.type = other.type;
      this.name = other.name;
      this.isVarArgs = other.isVarArgs;
   }

   @Override
   public ParameterAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new ParameterDsl(this), annotation, parameterDsl -> parameterDsl.annotations::add);
   }

   @Override
   public ParameterAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new ParameterDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              parameterDsl -> parameterDsl.annotations::add);
   }

   @Override
   public ParameterTypeStep final_()
   {
      return setType(new ParameterDsl(this), true, (parameterDsl, isFinal) -> parameterDsl.isFinal = isFinal);
   }

   @Override
   public ParameterNameStep type(String type)
   {
      return setTypeRenderer(new ParameterDsl(this), type, (parameterDsl, renderer) -> parameterDsl.type = renderer);
   }

   @Override
   public ParameterNameStep type(C_Array array)
   {
      return setTypeRenderer(new ParameterDsl(this),
                             array,
                             (renderingContext, cArray) -> Renderer.render(cArray).type(renderingContext),
                             (parameterDsl, renderer) -> parameterDsl.type = renderer);
   }

   @Override
   public ParameterNameStep type(C_Primitive primitive)
   {
      return setTypeRenderer(new ParameterDsl(this),
                             primitive,
                             (renderingContext, cPrimitive) -> Renderer.render(cPrimitive).type(renderingContext),
                             (parameterDsl, renderer) -> parameterDsl.type = renderer);
   }

   @Override
   public ParameterNameStep type(C_Declared declared)
   {
      return setTypeRenderer(new ParameterDsl(this),
                             declared,
                             (renderingContext, cDeclared) -> Renderer.render(cDeclared).type(renderingContext),
                             (parameterDsl, renderer) -> parameterDsl.type = renderer);
   }

   @Override
   public ParameterNameStep type(C_Generic generic)
   {
      return setTypeRenderer(new ParameterDsl(this),
                             generic,
                             (renderingContext, cGeneric) -> Renderer.render(cGeneric).type(renderingContext),
                             (parameterDsl, renderer) -> parameterDsl.type = renderer);
   }

   @Override
   public ParameterVarargsStep name(String name)
   {
      return setType(new ParameterDsl(this), name, (parameterDsl, s) -> parameterDsl.name = s);
   }

   @Override
   public ParameterRenderable varArgs()
   {
      return setType(new ParameterDsl(this), true, (parameterDsl, isVarargs) -> parameterDsl.isVarArgs = isVarargs);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, " ");

      if (isFinal)
      {
         sb.append("final ");
      }
      sb.append(type.apply(renderingContext));
      if (isVarArgs)
      {
         sb.append("...");
      }
      sb.append(" ");
      sb.append(name);

      return sb.toString();
   }
}
