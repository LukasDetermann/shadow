package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.method.*;
import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;
import io.determann.shadow.api.dsl.result.ResultRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_Receiver;
import io.determann.shadow.api.shadow.structure.C_Result;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;

public class MethodDsl
      implements MethodJavaDocStep,
                 MethodNameStep,
                 MethodReceiverStep
{
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private final List<Renderable> modifiers = new ArrayList<>();
   private final List<Renderable> generics = new ArrayList<>();
   private Renderable result;
   private String name;
   private Renderable receiver;
   private final List<Renderable> parameters = new ArrayList<>();
   private final List<Renderable> exceptions = new ArrayList<>();
   private String body;

   public MethodDsl()
   {
   }

   private MethodDsl(MethodDsl other)
   {
      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.modifiers.addAll(other.modifiers);
      this.generics.addAll(other.generics);
      this.result = other.result;
      this.name = other.name;
      this.receiver = other.receiver;
      this.parameters.addAll(other.parameters);
      this.exceptions.addAll(other.exceptions);
      this.body = other.body;
   }

   @Override
   public MethodAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new MethodDsl(this), javadoc, (methodDsl, function) -> methodDsl.javadoc = function);
   }

   @Override
   public MethodReceiverStep name(String name)
   {
      return setType(new MethodDsl(this), name, (methodDsl, s) -> methodDsl.name = s);
   }

   @Override
   public MethodParameterStep receiver(String receiver)
   {
      return setTypeRenderer(new MethodDsl(this), receiver, (methodDsl, function) -> methodDsl.receiver = function);
   }

   @Override
   public MethodParameterStep receiver(C_Receiver receiver)
   {
      return setTypeRenderer(new MethodDsl(this),
                             receiver,
                             (renderingContext, receiver1) -> Renderer.render(receiver1).declaration(renderingContext),
                             (methodDsl, function) -> methodDsl.receiver = function);
   }

   @Override
   public MethodParameterStep receiver(ReceiverRenderable receiver)
   {
      return setType(new MethodDsl(this),
                     receiver,
                     (methodDsl, receiverRenderable) -> methodDsl.receiver = receiverRenderable);
   }

   @Override
   public MethodParameterStep parameter(String... parameter)
   {
      return addArrayRenderer(new MethodDsl(this), parameter, methodDsl -> methodDsl.parameters::add);
   }

   @Override
   public MethodParameterStep parameter(C_Parameter... parameter)
   {
      return addArrayRenderer(new MethodDsl(this),
                              parameter,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              methodDsl -> methodDsl.parameters::add);
   }

   @Override
   public MethodParameterStep parameter(ParameterRenderable... parameter)
   {
      return addArray(new MethodDsl(this),
                      parameter,
                      methodDsl -> methodDsl.parameters::add);
   }

   @Override
   public MethodThrowsStep throws_(String... exception)
   {
      return addArrayRenderer(new MethodDsl(this), exception, methodDsl -> methodDsl.exceptions::add);
   }

   @Override
   public MethodThrowsStep throws_(C_Class... exception)
   {
      return addArrayRenderer(new MethodDsl(this),
                              exception,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              methodDsl -> methodDsl.exceptions::add);
   }

   @Override
   public MethodRenderable body(String body)
   {
      return setType(new MethodDsl(this), body, (methodDsl, s) -> methodDsl.body = s);
   }

   @Override
   public MethodAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new MethodDsl(this), annotation, methodDsl -> methodDsl.annotations::add);
   }

   @Override
   public MethodAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new MethodDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              methodDsl -> methodDsl.annotations::add);
   }

   @Override
   public MethodAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new MethodDsl(this),
                      annotation,
                      methodDsl -> methodDsl.annotations::add);
   }

   @Override
   public MethodModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new MethodDsl(this), modifiers, methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep modifier(C_Modifier... modifiers)
   {
      return addArrayRenderer(new MethodDsl(this),
                              modifiers,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep abstract_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.ABSTRACT,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep public_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.PUBLIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep protected_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.PROTECTED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep private_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.PRIVATE,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep default_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.DEFAULT,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep final_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.FINAL,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep native_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.NATIVE,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep static_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.STATIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodModifierStep strictfp_()
   {
      return addTypeRenderer(new MethodDsl(this),
                             C_Modifier.STRICTFP,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             methodDsl -> methodDsl.modifiers::add);
   }

   @Override
   public MethodGenericStep generic(String... generic)
   {
      return addArrayRenderer(new MethodDsl(this), generic, methodDsl -> methodDsl.generics::add);
   }

   @Override
   public MethodGenericStep generic(C_Generic... generic)
   {
      return addArrayRenderer(new MethodDsl(this),
                              generic,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              methodDsl -> methodDsl.generics::add);
   }

   @Override
   public MethodNameStep result(String result)
   {
      return setTypeRenderer(new MethodDsl(this), result, (methodDsl, function) -> methodDsl.result = function);
   }

   @Override
   public MethodNameStep result(C_Result result)
   {
      return setTypeRenderer(new MethodDsl(this),
                             result,
                             (renderingContext, result1) -> Renderer.render(result1).declaration(renderingContext),
                             (methodDsl, function) -> methodDsl.result = function);
   }

   @Override
   public MethodNameStep result(ResultRenderable result)
   {
      return setType(new MethodDsl(this),
                     result,
                     (methodDsl, resultRenderable) -> methodDsl.result = result);
   }

   @Override
   public MethodNameStep resultType(String resultType)
   {
      return setTypeRenderer(new MethodDsl(this), resultType, (methodDsl, function) -> methodDsl.result = function);
   }

   @Override
   public MethodNameStep resultType(C_Type resultType)
   {
      return setTypeRenderer(new MethodDsl(this),
                             resultType,
                             (renderingContext, result1) -> Renderer.render(result1).type(renderingContext),
                             (methodDsl, function) -> methodDsl.result = function);
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

      renderElement(sb, annotations, "\n", renderingContext, "\n");
      renderElement(sb, modifiers, " ", renderingContext, " ");
      renderElement(sb,"<", generics, "> ", renderingContext, ", ");

      if (result != null)
      {
         sb.append(result.render(renderingContext));
         sb.append(' ');
      }
      sb.append(name);
      sb.append('(');
      if (receiver != null)
      {
         sb.append(receiver.render(renderingContext));
         sb.append(' ');
         if (!parameters.isEmpty())
         {
            sb.append(", ");
         }
      }

      renderElement(sb, parameters, renderingContext, ", ");
      sb.append(')');

      renderElement(sb, " throws " ,exceptions, renderingContext, ", ");

      sb.append(" {");
      if (body != null)
      {
         sb.append('\n');
         sb.append(body);
         sb.append('\n');
      }
      sb.append("}");
      return sb.toString();
   }
}
