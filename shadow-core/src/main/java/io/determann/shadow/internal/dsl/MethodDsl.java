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

import static io.determann.shadow.internal.dsl.DslSupport.add;
import static java.util.Objects.requireNonNull;
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
      requireNonNull(javadoc);
      this.javadoc = renderingContext -> javadoc;
      return new MethodDsl(this);
   }

   @Override
   public MethodReceiverStep name(String name)
   {
      requireNonNull(name);
      this.name = name;
      return new MethodDsl(this);
   }

   @Override
   public MethodParameterStep receiver(String receiver)
   {
      requireNonNull(receiver);
      this.receiver = renderingContext -> receiver;
      return new MethodDsl(this);
   }

   @Override
   public MethodParameterStep receiver(C_Receiver receiver)
   {
      requireNonNull(receiver);
      this.receiver = renderingContext -> Renderer.render(receiver).declaration(renderingContext);
      return new MethodDsl(this);
   }

   @Override
   public MethodParameterStep parameter(String... parameter)
   {
      add(parameters, parameter);
      return new MethodDsl(this);
   }

   @Override
   public MethodParameterStep parameter(C_Parameter... parameter)
   {
      add(parameters, parameter, (renderingContext, c_parameter) -> Renderer.render(c_parameter).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodThrowsStep throws_(String... exception)
   {
      add(exceptions, exception);
      return new MethodDsl(this);
   }

   @Override
   public MethodThrowsStep throws_(C_Class... exception)
   {
      add(exceptions, exception, (renderingContext, cClass) -> Renderer.render(cClass).type(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodRenderable body(String body)
   {
      requireNonNull(body);
      this.body = body;
      return new MethodDsl(this);
   }

   @Override
   public MethodAnnotateStep annotate(String... annotation)
   {
      add(annotations, annotation);
      return new MethodDsl(this);
   }

   @Override
   public MethodAnnotateStep annotate(C_Annotation... annotation)
   {
      add(annotations, annotation, (renderingContext, cAnnotation) -> Renderer.render(cAnnotation).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep modifier(String... modifiers)
   {
      add(this.modifiers, modifiers);
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep modifier(C_Modifier... modifiers)
   {
      add(this.modifiers, modifiers, (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep abstract_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.ABSTRACT).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep public_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.PUBLIC).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep protected_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.PROTECTED).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep private_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.PRIVATE).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep default_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.DEFAULT).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep final_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.FINAL).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep native_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.NATIVE).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep static_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.STATIC).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodModifierStep strictfp_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.STRICTFP).declaration(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodGenericStep generic(String... generic)
   {
      add(generics, generic);
      return new MethodDsl(this);
   }

   @Override
   public MethodGenericStep generic(C_Generic... generic)
   {
      add(generics, generic, (renderingContext, generic1) -> Renderer.render(generic1).type(renderingContext));
      return new MethodDsl(this);
   }

   @Override
   public MethodNameStep result(String result)
   {
      requireNonNull(result);
      this.result = renderingContext -> result;
      return new MethodDsl(this);
   }

   @Override
   public MethodNameStep result(C_Result result)
   {
      this.result = renderingContext -> Renderer.render(result).declaration(renderingContext);
      return new MethodDsl(this);
   }

   @Override
   public MethodNameStep resultType(String resultType)
   {
      requireNonNull(resultType);
      this.result = renderingContext -> resultType;
      return new MethodDsl(this);
   }

   @Override
   public MethodNameStep resultType(C_Type resultType)
   {
      requireNonNull(resultType);
      this.result = renderingContext -> Renderer.render(resultType).type(renderingContext);
      return new MethodDsl(this);
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
