package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.field.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class FieldDsl
      implements FieldJavaDocStep,
                 FieldNameStep,
                 FieldInitializationStep
{
   private Function<RenderingContext, String> javadoc;
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private final List<Function<RenderingContext, String>> modifiers = new ArrayList<>();
   private Function<RenderingContext, String> type;
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
      return set(new FieldDsl(this),
                 (fieldDsl, function) -> fieldDsl.javadoc = function,
                 javadoc);
   }

   @Override
   public FieldModifierStep annotate(String... annotation)
   {
      return add(new FieldDsl(this), fieldDsl -> fieldDsl.annotations::add, annotation);
   }

   @Override
   public FieldModifierStep annotate(C_Annotation... annotation)
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.annotations::add,
                 (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                 annotation);
   }

   @Override
   public FieldAdditionalNameStep initializer(String initializer)
   {
      return setString(new Additional(this),
                       (additional, s) -> additional.initializers.add(s),
                       initializer);
   }

   @Override
   public FieldModifierStep modifier(String... modifiers)
   {
      return add(new FieldDsl(this), fieldDsl -> fieldDsl.modifiers::add, modifiers);
   }

   @Override
   public FieldModifierStep modifier(C_Modifier... modifiers)
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (context, modifier) -> Renderer.render(modifier).declaration(context),
                 modifiers);
   }

   @Override
   public FieldModifierStep public_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PUBLIC);
   }

   @Override
   public FieldModifierStep protected_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PROTECTED);
   }

   @Override
   public FieldModifierStep private_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PRIVATE);
   }

   @Override
   public FieldModifierStep final_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.FINAL);
   }

   @Override
   public FieldModifierStep static_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.STATIC);
   }

   @Override
   public FieldModifierStep strictfp_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.STRICTFP);
   }

   @Override
   public FieldModifierStep transient_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.TRANSIENT);
   }

   @Override
   public FieldModifierStep volatile_()
   {
      return add(new FieldDsl(this),
                 fieldDsl -> fieldDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.VOLATILE);
   }

   @Override
   public FieldInitializationStep name(String name)
   {
      return setString(new FieldDsl(this),
                       (fieldDsl, s) -> fieldDsl.name = s,
                       name);
   }

   @Override
   public FieldNameStep type(String type)
   {
      return set(new FieldDsl(this),
                 (fieldDsl, function) -> this.type = function,
                 type);
   }

   @Override
   public FieldNameStep type(C_Primitive primitive)
   {
      return set(new FieldDsl(this),
                 (fieldDsl, function) -> this.type = function,
                 (renderingContext, primitive1) -> Renderer.render(primitive1).type(renderingContext),
                 primitive);
   }

   @Override
   public FieldNameStep type(C_Array array)
   {
      return set(new FieldDsl(this),
                 (fieldDsl, function) -> this.type = function,
                 (renderingContext, array1) -> Renderer.render(array1).type(renderingContext),
                 array);
   }

   @Override
   public FieldNameStep type(C_Declared declared)
   {
      return set(new FieldDsl(this),
                 (fieldDsl, function) -> this.type = function,
                 (renderingContext, declared1) -> Renderer.render(declared1).type(renderingContext),
                 declared);
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
         sb.append(javadoc.apply(renderingContext))
           .append("\n");
      }
      if (!annotations.isEmpty())
      {
         sb.append(this.annotations.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining("\n")))
           .append('\n');
      }
      if (!modifiers.isEmpty())
      {
         sb.append(modifiers.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(" ")))
           .append(' ');
      }
      sb.append(type.apply(renderingContext))
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
         return setString(new Additional(this), (additional, s) -> additional.initializers.add(s), initializer);
      }

      @Override
      public FieldAdditionalInitializationStep name(String name)
      {
         return setString(new Additional(this), (additional, s) -> additional.names.add(s), name);
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






















