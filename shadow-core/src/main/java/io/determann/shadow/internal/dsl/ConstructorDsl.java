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

import static io.determann.shadow.internal.dsl.DslSupport.add;
import static java.util.Objects.requireNonNull;
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
      requireNonNull(javadoc);
      this.javadoc = renderingContext -> javadoc;
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorParameterStep receiver(String receiver)
   {
      requireNonNull(receiver);
      this.receiver = renderingContext -> receiver;
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorParameterStep receiver(C_Receiver receiver)
   {
      requireNonNull(receiver);
      this.receiver = renderingContext -> Renderer.render(receiver).declaration(renderingContext);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorParameterStep parameter(String... parameter)
   {
      add(this.parameters, parameter);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorParameterStep parameter(C_Parameter... parameter)
   {
      add(this.parameters, parameter, (renderingContext, cParameter) -> Renderer.render(cParameter).declaration(renderingContext));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorThrowsStep throws_(String... exception)
   {
      add(exceptions, exception);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorThrowsStep throws_(C_Class... exception)
   {
      add(exceptions, exception, (renderingContext, cClass) -> Renderer.render(cClass).type(renderingContext));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorRenderable body(String body)
   {
      requireNonNull(body);
      this.body = body;
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorAnnotateStep annotate(String... annotation)
   {
      add(annotations, annotation);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorAnnotateStep annotate(C_Annotation... annotation)
   {
      add(annotations, annotation, (renderingContext, cAnnotation) -> Renderer.render(cAnnotation).declaration(renderingContext));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorModifierStep modifier(String... modifiers)
   {
      add(this.modifiers, modifiers);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorModifierStep modifier(C_Modifier... modifiers)
   {
      add(this.modifiers, modifiers, ((renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext)));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorModifierStep public_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.PUBLIC).declaration(renderingContext));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorModifierStep protected_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.PROTECTED).declaration(renderingContext));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorModifierStep private_()
   {
      modifiers.add(renderingContext -> Renderer.render(C_Modifier.PRIVATE).declaration(renderingContext));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorGenericStep generic(String... generic)
   {
      add(generics, generic);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorGenericStep generic(C_Generic... generic)
   {
      add(generics, generic, (renderingContext, generic1) -> Renderer.render(generic1).type(renderingContext));
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorReceiverStep constructor(String type)
   {
      requireNonNull(type);
      this.result = renderingContext -> type;
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorReceiverStep constructor(C_Class type)
   {
      requireNonNull(type);
      this.result = renderingContext -> Renderer.render(type).type(renderingContext);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorReceiverStep constructor(C_Enum type)
   {
      requireNonNull(type);
      this.result = renderingContext -> Renderer.render(type).type(renderingContext);
      return new ConstructorDsl(this);
   }

   @Override
   public ConstructorReceiverStep constructor(C_Record type)
   {
      requireNonNull(type);
      this.result = renderingContext -> Renderer.render(type).type(renderingContext);
      return new ConstructorDsl(this);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(result.apply(renderingContext));
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
