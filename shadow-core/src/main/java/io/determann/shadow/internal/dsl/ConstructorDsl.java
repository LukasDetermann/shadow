package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.constructor.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.structure.C_Receiver;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Record;

import java.util.ArrayList;
import java.util.List;

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
      return setTypeRenderer(new ConstructorDsl(this),
                             javadoc,
                             (constructorDsl, function) -> constructorDsl.javadoc = function);
   }

   @Override
   public ConstructorParameterStep receiver(String receiver)
   {
      return setTypeRenderer(new ConstructorDsl(this),
                             receiver,
                             (constructorDsl, function) -> constructorDsl.receiver = function);
   }

   @Override
   public ConstructorParameterStep receiver(C_Receiver receiver)
   {
      return setTypeRenderer(new ConstructorDsl(this),
                             receiver,
                             (renderingContext, receiver1) -> Renderer.render(receiver1).declaration(renderingContext),
                             (constructorDsl, function) -> constructorDsl.receiver = function);
   }

   @Override
   public ConstructorParameterStep parameter(String... parameter)
   {
      return addArrayRenderer(new ConstructorDsl(this), parameter, constructorDsl -> constructorDsl.parameters::add);
   }

   @Override
   public ConstructorParameterStep parameter(C_Parameter... parameter)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              parameter,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              constructorDsl -> constructorDsl.parameters::add);
   }

   @Override
   public ConstructorThrowsStep throws_(String... exception)
   {
      return addArrayRenderer(new ConstructorDsl(this), exception, constructorDsl -> constructorDsl.exceptions::add);
   }

   @Override
   public ConstructorThrowsStep throws_(C_Class... exception)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              exception,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              constructorDsl -> constructorDsl.exceptions::add);
   }

   @Override
   public ConstructorRenderable body(String body)
   {
      return setType(new ConstructorDsl(this), body, (constructorDsl, s) -> constructorDsl.body = s);
   }

   @Override
   public ConstructorAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new ConstructorDsl(this), annotation, (renderingContext, string) -> '@' + string,constructorDsl -> constructorDsl.annotations::add);
   }

   @Override
   public ConstructorAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              constructorDsl -> constructorDsl.annotations::add);
   }

   @Override
   public ConstructorAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new ConstructorDsl(this),
                      annotation,
                      constructorDsl -> constructorDsl.annotations::add);
   }

   @Override
   public ConstructorModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new ConstructorDsl(this), modifiers, constructorDsl -> constructorDsl.modifiers::add);
   }

   @Override
   public ConstructorModifierStep modifier(C_Modifier... modifiers)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              modifiers,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              constructorDsl -> constructorDsl.modifiers::add);
   }

   @Override
   public ConstructorModifierStep public_()
   {
      return addTypeRenderer(new ConstructorDsl(this),
                             C_Modifier.PUBLIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             constructorDsl -> constructorDsl.modifiers::add);
   }

   @Override
   public ConstructorModifierStep protected_()
   {
      return addTypeRenderer(new ConstructorDsl(this),
                             C_Modifier.PROTECTED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             constructorDsl -> constructorDsl.modifiers::add);
   }

   @Override
   public ConstructorModifierStep private_()
   {
      return addTypeRenderer(new ConstructorDsl(this),
                             C_Modifier.PRIVATE,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             constructorDsl -> constructorDsl.modifiers::add);
   }

   @Override
   public ConstructorGenericStep generic(String... generic)
   {
      return addArrayRenderer(new ConstructorDsl(this), generic, constructorDsl -> constructorDsl.generics::add);
   }

   @Override
   public ConstructorGenericStep generic(C_Generic... generic)
   {
      return addArrayRenderer(new ConstructorDsl(this),
                              generic,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              constructorDsl -> constructorDsl.generics::add);
   }

   @Override
   public ConstructorReceiverStep type(String type)
   {
      return setTypeRenderer(new ConstructorDsl(this),
                             type,
                             (constructorDsl, function) -> constructorDsl.result = function);
   }

   @Override
   public ConstructorReceiverStep type(C_Class type)
   {
      return setTypeRenderer(new ConstructorDsl(this),
                             type,
                             (renderingContext, cClass) -> Renderer.render(type).type(renderingContext),
                             (constructorDsl, function) -> constructorDsl.result = function);
   }

   @Override
   public ConstructorReceiverStep type(ClassRenderable type)
   {
      return setType(new ConstructorDsl(this),
                     type,
                     (constructorDsl, classRenderable) -> constructorDsl.result = classRenderable);
   }

   @Override
   public ConstructorReceiverStep type(C_Enum type)
   {
      return setTypeRenderer(new ConstructorDsl(this),
                             type,
                             (renderingContext, cClass) -> Renderer.render(type).type(renderingContext),
                             (constructorDsl, function) -> constructorDsl.result = function);
   }

   @Override
   public ConstructorReceiverStep type(C_Record type)
   {
      return setTypeRenderer(new ConstructorDsl(this),
                             type,
                             (renderingContext, cClass) -> Renderer.render(type).type(renderingContext),
                             (constructorDsl, function) -> constructorDsl.result = function);
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
      renderElement(sb, "<", generics, "> ", renderingContext, ", ");

      if (result != null)
      {
         sb.append(result.render(renderingContext));
         sb.append(' ');
      }
      sb.append('(');
      if (receiver != null)
      {
         sb.append(receiver.render(renderingContext));
         if (!parameters.isEmpty())
         {
            sb.append(", ");
         }
      }

      renderElement(sb, parameters, ")", renderingContext, ", ");
      renderElement(sb, " throws ", exceptions, renderingContext, ", ");

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
