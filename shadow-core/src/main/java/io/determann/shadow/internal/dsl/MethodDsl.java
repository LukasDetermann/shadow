package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.method.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_Receiver;
import io.determann.shadow.api.shadow.structure.C_Result;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class MethodDsl
      implements MethodJavaDocStep,
                 MethodNameStep,
                 MethodReceiverStep
{
   private Function<RenderingContext, String> javadoc;
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private final List<Function<RenderingContext, String>> modifiers = new ArrayList<>();
   private final List<Function<RenderingContext, String>> generics = new ArrayList<>();
   private Function<RenderingContext, String> result;
   private String name;
   private Function<RenderingContext, String> receiver;
   private final List<Function<RenderingContext, String>> parameters = new ArrayList<>();
   private final List<Function<RenderingContext, String>> exceptions = new ArrayList<>();
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
   public MethodAnnotateStep annotate(C_Annotation... annotation)
   {
      return addArrayRenderer(new MethodDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
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
         sb.append(javadoc.apply(renderingContext));
         sb.append("\n");
      }
      if (!annotations.isEmpty())
      {
         sb.append(this.annotations.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining("\n")));
         sb.append('\n');
      }
      if (!modifiers.isEmpty())
      {
         sb.append(modifiers.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(" ")));
         sb.append(' ');
      }
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
         sb.append('>');
         sb.append(' ');
      }
      if (result != null)
      {
         sb.append(result.apply(renderingContext));
         sb.append(' ');
      }
      sb.append(name);
      sb.append('(');
      if (receiver != null)
      {
         sb.append(receiver.apply(renderingContext));
         sb.append(' ');
         if (!parameters.isEmpty())
         {
            sb.append(", ");
         }
      }
      if (!parameters.isEmpty())
      {
         sb.append(parameters.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
      }
      sb.append(')');

      if (!exceptions.isEmpty())
      {
         sb.append(exceptions.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
      }
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
