package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.constructor.*;
import io.determann.shadow.api.dsl.enum_.EnumRenderable;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.parameter.ParameterRenderable;
import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;
import io.determann.shadow.api.dsl.record.RecordRenderable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static io.determann.shadow.internal.dsl.DslSupport.*;

public class ConstructorDsl
      implements ConstructorJavaDocStep,
                 ConstructorReceiverStep
{
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private final List<Renderable> modifiers = new ArrayList<>();
   private final List<Renderable> generics = new ArrayList<>();
   private Renderable result;
   private Renderable receiver;
   private final List<Renderable> parameters = new ArrayList<>();
   private final List<Renderable> exceptions = new ArrayList<>();
   private Renderable body;

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
      return setTypeRenderer(new ConstructorDsl(this),
                             javadoc,
                             DslSupport::indent,
                             (constructorDsl, function) -> constructorDsl.javadoc = function);
   }

   @Override
   public ConstructorParameterStep receiver(String receiver)
   {
      return setType(new ConstructorDsl(this),
                     receiver,
                     (constructorDsl, string) -> constructorDsl.receiver = renderingContext -> receiver);
   }

   @Override
   public ConstructorParameterStep receiver(ReceiverRenderable receiver)
   {
      return setTypeRenderer(new ConstructorDsl(this),
                             receiver,
                             (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                             (constructorDsl, function) -> constructorDsl.receiver = function);
   }

   @Override
   public ConstructorParameterStep parameter(String... parameter)
   {
      return addArray2(new ConstructorDsl(this),
                       parameter,
                       (constructorDsl, string) -> constructorDsl.parameters.add(renderingContext -> string));
   }

   @Override
   public ConstructorParameterStep parameter(List<? extends ParameterRenderable> parameter)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              parameter,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              constructorDsl -> constructorDsl.parameters::add);
   }

   @Override
   public ConstructorThrowsStep throws_(String... exception)
   {
      return addArray2(new ConstructorDsl(this),
                       exception,
                       (constructorDsl, string) -> constructorDsl.exceptions.add(context -> context.renderName(string)));
   }

   @Override
   public ConstructorThrowsStep throws_(List<? extends ClassRenderable> exception)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              exception,
                              (renderingContext, classRenderable) -> classRenderable.renderName(renderingContext),
                              constructorDsl -> constructorDsl.exceptions::add);
   }

   @Override
   public ConstructorRenderable body(String body)
   {
      return setType(new ConstructorDsl(this), body, (constructorDsl, s) -> constructorDsl.body = context -> indent(context, s));
   }

   @Override
   public ConstructorAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              annotation,
                              (context, string) -> indent(context, '@' + context.renderName(string)),
                              constructorDsl -> constructorDsl.annotations::add);
   }

   @Override
   public ConstructorAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              annotation,
                              (renderingContext, annotationUsageRenderable) -> annotationUsageRenderable.renderDeclaration(renderingContext),
                              constructorDsl -> constructorDsl.annotations::add);
   }

   @Override
   public ConstructorModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new ConstructorDsl(this), modifiers, constructorDsl -> constructorDsl.modifiers::add);
   }

   @Override
   public ConstructorModifierStep modifier(Set<Modifier> modifiers)
   {
      return addArray2(new ConstructorDsl(this),
                       modifiers,
                       (constructorDsl, modifier) -> constructorDsl.modifiers.add(modifier::render));
   }

   @Override
   public ConstructorModifierStep public_()
   {
      return setType(new ConstructorDsl(this),
                     Modifier.PUBLIC,
                     (constructorDsl, modifier) -> constructorDsl.modifiers.add(modifier::render));
   }

   @Override
   public ConstructorModifierStep protected_()
   {
      return setType(new ConstructorDsl(this),
                     Modifier.PROTECTED,
                     (constructorDsl, modifier) -> constructorDsl.modifiers.add(modifier::render));
   }

   @Override
   public ConstructorModifierStep private_()
   {
      return setType(new ConstructorDsl(this),
                     Modifier.PRIVATE,
                     (constructorDsl, modifier) -> constructorDsl.modifiers.add(modifier::render));
   }

   @Override
   public ConstructorGenericStep generic(String... generic)
   {
      return addArray2(new ConstructorDsl(this), generic, (constructorDsl, string) -> constructorDsl.generics.add(renderingContext -> string));
   }

   @Override
   public ConstructorGenericStep generic(List<? extends GenericRenderable> generic)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              generic,
                              (renderingContext, genericRenderable) -> genericRenderable.renderDeclaration(renderingContext),
                              constructorDsl -> constructorDsl.generics::add);
   }

   @Override
   public ConstructorReceiverStep type(String type)
   {
      return setType(new ConstructorDsl(this),
                     type,
                     (constructorDsl, string) -> constructorDsl.result = renderingContext -> string);
   }

   @Override
   public ConstructorReceiverStep type(ClassRenderable type)
   {
      return setType(new ConstructorDsl(this),
                     type,
                     (constructorDsl, classRenderable) -> constructorDsl.result = classRenderable::renderQualifiedName);
   }

   @Override
   public ConstructorReceiverStep type(EnumRenderable type)
   {
      return setType(new ConstructorDsl(this),
                     type,
                     (constructorDsl, function) -> constructorDsl.result = function::renderQualifiedName);
   }

   @Override
   public ConstructorReceiverStep type(RecordRenderable type)
   {
      return setType(new ConstructorDsl(this),
                     type,
                     (constructorDsl, function) -> constructorDsl.result = function::renderQualifiedName);
   }

   @Override
   public String renderDeclaration(RenderingContext context)
   {
      context.addSurrounding(this);

      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.render(context));
         sb.append("\n");
      }

      renderElement(sb, annotations, context, "\n", new Padding(null, context.getLineIndentation(), null, "\n"));
      sb.append(context.getLineIndentation());
      renderElement(sb, modifiers, context, " ", new Padding(null, null, null, " "));
      renderElement(sb, generics, context, ", ", new Padding("<", null, null, "> "));

      sb.append(result.render(context));
      sb.append('(');
      if (receiver != null)
      {
         sb.append(receiver.render(context));
         if (!parameters.isEmpty())
         {
            sb.append(", ");
         }
      }
      renderElement(sb, parameters, context, ", ");
      sb.append(')');

      renderElement(sb, " throws ", exceptions, context, ", ");

      sb.append(" {");
      RenderingContext indented = createRenderingContext(context);
      indented.incrementIndentationLevel();
      if (body != null)
      {
         sb.append('\n')
           .append(body.render(indented))
           .append(context.getLineIndentation())
           .append('\n');
      }
      return sb.append("}")
               .toString();
   }
}
