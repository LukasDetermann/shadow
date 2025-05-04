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
      return set(new MethodDsl(this),
                 (methodDsl, function) -> methodDsl.javadoc = function,
                 javadoc);
   }

   @Override
   public MethodReceiverStep name(String name)
   {
      return setString(new MethodDsl(this),
                       (methodDsl, s) -> methodDsl.name = s,
                       name);
   }

   @Override
   public MethodParameterStep receiver(String receiver)
   {
      return set(new MethodDsl(this),
                 (methodDsl, function) -> methodDsl.receiver = function,
                 receiver);
   }

   @Override
   public MethodParameterStep receiver(C_Receiver receiver)
   {
      return set(new MethodDsl(this),
                 (methodDsl, function) -> methodDsl.receiver = function,
                 (renderingContext, receiver1) -> Renderer.render(receiver1).declaration(renderingContext),
                 receiver);
   }

   @Override
   public MethodParameterStep parameter(String... parameter)
   {
      return add(new MethodDsl(this), methodDsl -> methodDsl.parameters::add, parameter);
   }

   @Override
   public MethodParameterStep parameter(C_Parameter... parameter)
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.parameters::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 parameter);
   }

   @Override
   public MethodThrowsStep throws_(String... exception)
   {
      return add(new MethodDsl(this), methodDsl -> methodDsl.exceptions::add, exception);
   }

   @Override
   public MethodThrowsStep throws_(C_Class... exception)
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.exceptions::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 exception);
   }

   @Override
   public MethodRenderable body(String body)
   {
      return setString(new MethodDsl(this),
                       (methodDsl, s) -> methodDsl.body = s,
                       body);
   }

   @Override
   public MethodAnnotateStep annotate(String... annotation)
   {
      return add(new MethodDsl(this), methodDsl -> methodDsl.annotations::add, annotation);
   }

   @Override
   public MethodAnnotateStep annotate(C_Annotation... annotation)
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.annotations::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 annotation);
   }

   @Override
   public MethodModifierStep modifier(String... modifiers)
   {
      return add(new MethodDsl(this), methodDsl -> methodDsl.modifiers::add, modifiers);
   }

   @Override
   public MethodModifierStep modifier(C_Modifier... modifiers)
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 modifiers);
   }

   @Override
   public MethodModifierStep abstract_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.ABSTRACT);
   }

   @Override
   public MethodModifierStep public_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PUBLIC);
   }

   @Override
   public MethodModifierStep protected_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PROTECTED);
   }

   @Override
   public MethodModifierStep private_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PRIVATE);
   }

   @Override
   public MethodModifierStep default_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.DEFAULT);
   }

   @Override
   public MethodModifierStep final_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.FINAL);
   }

   @Override
   public MethodModifierStep native_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.NATIVE);
   }

   @Override
   public MethodModifierStep static_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.STATIC);
   }

   @Override
   public MethodModifierStep strictfp_()
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.STRICTFP);
   }

   @Override
   public MethodGenericStep generic(String... generic)
   {
      return add(new MethodDsl(this), methodDsl -> methodDsl.generics::add, generic);
   }

   @Override
   public MethodGenericStep generic(C_Generic... generic)
   {
      return add(new MethodDsl(this),
                 methodDsl -> methodDsl.generics::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 generic);
   }

   @Override
   public MethodNameStep result(String result)
   {
      return set(new MethodDsl(this), (methodDsl, function) -> methodDsl.result = function, result);
   }

   @Override
   public MethodNameStep result(C_Result result)
   {
      return set(new MethodDsl(this),
                 (methodDsl, function) -> methodDsl.result = function,
                 (renderingContext, result1) -> Renderer.render(result1).declaration(renderingContext),
                 result);
   }

   @Override
   public MethodNameStep resultType(String resultType)
   {
      return set(new MethodDsl(this),
                 (methodDsl, function) -> methodDsl.result = function,
                 resultType);
   }

   @Override
   public MethodNameStep resultType(C_Type resultType)
   {
      return set(new MethodDsl(this),
                 (methodDsl, function) -> methodDsl.result = function,
                 (renderingContext, result1) -> Renderer.render(result1).type(renderingContext),
                 resultType);
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
