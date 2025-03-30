package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.constructor.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_Receiver;
import io.determann.shadow.api.shadow.type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class ConstructorDsl implements ConstructorJavaDocStep,
                                       ConstructorReceiverStep
{
   private Function<RenderingContext, String> javadoc;
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private final List<Function<RenderingContext, String>> modifiers = new ArrayList<>();
   private final List<Function<RenderingContext, String>> generics = new ArrayList<>();
   private Function<RenderingContext, String> result;
   private Function<RenderingContext, String> receiver;
   private final List<Function<RenderingContext, String>> parameters = new ArrayList<>();
   private final List<Function<RenderingContext, String>> exceptions = new ArrayList<>();
   private String body;

   public ConstructorDsl()
   {
   }

   private ConstructorDsl(ConstructorDsl other)
   {
      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.modifiers.addAll(other.modifiers);
      this.generics.addAll(other.generics);
      this.result = other.result;
      this.receiver = other.receiver;
      this.parameters.addAll(other.parameters);
      this.exceptions.addAll(other.exceptions);
      this.body = other.body;
   }

   @Override
   public ConstructorAnnotateStep javadoc(String javadoc)
   {
      return set(new ConstructorDsl(this),
                 (constructorDsl, function) -> constructorDsl.javadoc = function,
                 javadoc);
   }

   @Override
   public ConstructorParameterStep receiver(String receiver)
   {
      return set(new ConstructorDsl(this),
                 (constructorDsl, function) -> constructorDsl.receiver = function,
                 receiver);
   }

   @Override
   public ConstructorParameterStep receiver(C_Receiver receiver)
   {
      return set(new ConstructorDsl(this),
                 (constructorDsl, function) -> constructorDsl.receiver = function,
                 (renderingContext, receiver1) -> Renderer.render(receiver1).declaration(renderingContext),
                 receiver);
   }

   @Override
   public ConstructorParameterStep parameter(String... parameter)
   {
      return add(new ConstructorDsl(this), constructorDsl -> constructorDsl.parameters::add, parameter);
   }

   @Override
   public ConstructorParameterStep parameter(C_Parameter... parameter)
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.parameters::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 parameter);
   }

   @Override
   public ConstructorThrowsStep throws_(String... exception)
   {
      return add(new ConstructorDsl(this), constructorDsl -> constructorDsl.exceptions::add, exception);
   }

   @Override
   public ConstructorThrowsStep throws_(C_Class... exception)
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.exceptions::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 exception);
   }

   @Override
   public ConstructorRenderable body(String body)
   {
      return setString(new ConstructorDsl(this), (constructorDsl, s) -> constructorDsl.body = s, body);
   }

   @Override
   public ConstructorAnnotateStep annotate(String... annotation)
   {
      return add(new ConstructorDsl(this), constructorDsl -> constructorDsl.annotations::add, annotation);
   }

   @Override
   public ConstructorAnnotateStep annotate(C_Annotation... annotation)
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.annotations::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 annotation);
   }

   @Override
   public ConstructorModifierStep modifier(String... modifiers)
   {
      return add(new ConstructorDsl(this), constructorDsl -> constructorDsl.modifiers::add, modifiers);
   }

   @Override
   public ConstructorModifierStep modifier(C_Modifier... modifiers)
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 modifiers);
   }

   @Override
   public ConstructorModifierStep public_()
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PUBLIC);
   }

   @Override
   public ConstructorModifierStep protected_()
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PROTECTED);
   }

   @Override
   public ConstructorModifierStep private_()
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PRIVATE);
   }

   @Override
   public ConstructorGenericStep generic(String... generic)
   {
      return add(new ConstructorDsl(this), constructorDsl -> constructorDsl.generics::add, generic);
   }

   @Override
   public ConstructorGenericStep generic(C_Generic... generic)
   {
      return add(new ConstructorDsl(this),
                 constructorDsl -> constructorDsl.generics::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 generic);
   }

   @Override
   public ConstructorReceiverStep type(String type)
   {
      return set(new ConstructorDsl(this),
                 (constructorDsl, function) -> constructorDsl.result = function,
                 type);
   }

   @Override
   public ConstructorReceiverStep type(C_Class type)
   {
      return set(new ConstructorDsl(this),
                 (constructorDsl, function) -> constructorDsl.result = function,
                 (renderingContext, cClass) -> Renderer.render(type).type(renderingContext),
                 type);
   }

   @Override
   public ConstructorReceiverStep type(C_Enum type)
   {
      return set(new ConstructorDsl(this),
                 (constructorDsl, function) -> constructorDsl.result = function,
                 (renderingContext, cClass) -> Renderer.render(type).type(renderingContext),
                 type);
   }

   @Override
   public ConstructorReceiverStep type(C_Record type)
   {
      return set(new ConstructorDsl(this),
                 (constructorDsl, function) -> constructorDsl.result = function,
                 (renderingContext, cClass) -> Renderer.render(type).type(renderingContext),
                 type);
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
