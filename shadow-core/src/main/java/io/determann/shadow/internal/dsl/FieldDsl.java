package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.field.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class FieldDsl
      implements FieldJavaDocStep,
                 FieldNameStep,
                 FieldInitializationStep
{
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private final List<Renderable> modifiers = new ArrayList<>();
   private Renderable type;
   private String name;
   private String initializer;

   public FieldDsl() {}

   private FieldDsl(FieldDsl other)
   {
      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.modifiers.addAll(other.modifiers);
      this.type = other.type;
      this.name = other.name;
      this.initializer = other.initializer;
   }

   @Override
   public FieldAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new FieldDsl(this),
                             javadoc,
                             (fieldDsl, function) -> fieldDsl.javadoc = function);
   }

   @Override
   public FieldModifierStep annotate(String... annotation)
   {
      return addArrayRenderer(new FieldDsl(this), annotation, (renderingContext, string) -> '@' + string, fieldDsl -> fieldDsl.annotations::add);
   }

   @Override
   public FieldModifierStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new FieldDsl(this),
                              annotation,
                              (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                              fieldDsl -> fieldDsl.annotations::add);
   }

   @Override
   public FieldModifierStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new FieldDsl(this),
                      annotation,
                      fieldDsl -> fieldDsl.annotations::add);
   }

   @Override
   public FieldAdditionalNameStep initializer(String initializer)
   {
      return setType(new Additional(this), initializer, (additional, s) -> additional.initializers.add(s));
   }

   @Override
   public FieldModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new FieldDsl(this), modifiers, fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep modifier(C_Modifier... modifiers)
   {
      return addArrayRenderer(new FieldDsl(this),
                              modifiers,
                              (context, modifier) -> Renderer.render(modifier).declaration(context),
                              fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep public_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.PUBLIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep protected_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.PROTECTED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep private_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.PRIVATE,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep final_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.FINAL,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep static_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.STATIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep strictfp_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.STRICTFP,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep transient_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.TRANSIENT,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep volatile_()
   {
      return addTypeRenderer(new FieldDsl(this),
                             C_Modifier.VOLATILE,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldInitializationStep name(String name)
   {
      return setType(new FieldDsl(this), name, (fieldDsl, s) -> fieldDsl.name = s);
   }

   @Override
   public FieldNameStep type(String type)
   {
      return setTypeRenderer(new FieldDsl(this), type, (fieldDsl, function) -> fieldDsl.type = function);
   }

   @Override
   public FieldNameStep type(C_Primitive primitive)
   {
      return setTypeRenderer(new FieldDsl(this),
                             primitive,
                             (renderingContext, primitive1) -> Renderer.render(primitive1).type(renderingContext),
                             (fieldDsl, function) -> fieldDsl.type = function);
   }

   @Override
   public FieldNameStep type(C_Array array)
   {
      return setTypeRenderer(new FieldDsl(this),
                             array,
                             (renderingContext, array1) -> Renderer.render(array1).type(renderingContext),
                             (fieldDsl, function) -> fieldDsl.type = function);
   }

   @Override
   public FieldNameStep type(C_Declared declared)
   {
      return setTypeRenderer(new FieldDsl(this),
                             declared,
                             (renderingContext, declared1) -> Renderer.render(declared1).type(renderingContext),
                             (fieldDsl, function) -> fieldDsl.type = function);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      return partialRender(renderingContext) + ';';
   }

   private String partialRender(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.render(renderingContext))
           .append("\n");
      }
      if (!annotations.isEmpty())
      {
         sb.append(this.annotations.stream().map(renderer -> renderer.render(renderingContext)).collect(joining("\n")))
           .append('\n');
      }
      if (!modifiers.isEmpty())
      {
         sb.append(modifiers.stream().map(renderer -> renderer.render(renderingContext)).collect(joining(" ")))
           .append(' ');
      }
      sb.append(type.render(renderingContext))
        .append(' ');

      sb.append(name);

      if (initializer != null)
      {
         sb.append(" = ")
           .append(initializer);
      }
      return sb.toString();
   }

   private static class Additional
         implements FieldAdditionalNameStep,
                    FieldAdditionalInitializationStep
   {
      private final List<String> initializers = new ArrayList<>();
      private final List<String> names = new ArrayList<>();

      private final FieldDsl fieldDsl;

      private Additional(FieldDsl fieldDsl)
      {
         this.fieldDsl = fieldDsl;
      }

      private Additional(Additional other)
      {
         this.fieldDsl = other.fieldDsl;
         this.initializers.addAll(other.initializers);
         this.names.addAll(other.names);
      }

      @Override
      public FieldAdditionalNameStep initializer(String initializer)
      {
         return setType(new Additional(this), initializer, (additional, s) -> additional.initializers.add(s));
      }

      @Override
      public FieldAdditionalInitializationStep name(String name)
      {
         return setType(new Additional(this), name, (additional, s) -> additional.names.add(s));
      }

      @Override
      public String render(RenderingContext renderingContext)
      {
         StringBuilder sb = new StringBuilder();

         for (int i = 0; i < initializers.size(); i++)
         {
            sb.append(", ")
              .append(names.get(i))
              .append(" = ")
              .append(initializers.get(i));
         }

         if (names.size() + 1 == initializers.size())
         {
            sb.append(", ")
              .append(names.getLast());
         }
         sb.append(';');

         return sb.toString();
      }
   }
}






















