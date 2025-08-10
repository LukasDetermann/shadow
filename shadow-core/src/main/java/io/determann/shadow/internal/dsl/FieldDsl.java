package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.VariableTypeRenderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.field.*;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
   public FieldAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new FieldDsl(this), annotation, (renderingContext, string) -> '@' + string, fieldDsl -> fieldDsl.annotations::add);
   }

   @Override
   public FieldAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new FieldDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              fieldDsl -> fieldDsl.annotations::add);
   }

   @Override
   public FieldAdditionalNameStep initializer(String initializer)
   {
      // in this special the initializer needs to be added to the base field dsl to mach the count of names and initializers
      return setType(new Additional(this), initializer, (additional, s) -> this.initializer = s);
   }

   @Override
   public FieldModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new FieldDsl(this), modifiers, fieldDsl -> fieldDsl.modifiers::add);
   }

   @Override
   public FieldModifierStep modifier(Set<Modifier> modifiers)
   {
      return addArray2(new FieldDsl(this),
                       modifiers,
                       (fieldDsl, modifier) -> fieldDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep public_()
   {
      return setType(new FieldDsl(this),
                     Modifier.PUBLIC,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep protected_()
   {
      return setType(new FieldDsl(this),
                     Modifier.PROTECTED,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep private_()
   {
      return setType(new FieldDsl(this),
                     Modifier.PRIVATE,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep final_()
   {
      return setType(new FieldDsl(this),
                     Modifier.FINAL,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep static_()
   {
      return setType(new FieldDsl(this),
                     Modifier.STATIC,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep strictfp_()
   {
      return setType(new FieldDsl(this),
                     Modifier.STRICTFP,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep transient_()
   {
      return setType(new FieldDsl(this),
                     Modifier.TRANSIENT,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldModifierStep volatile_()
   {
      return setType(new FieldDsl(this),
                     Modifier.VOLATILE,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public FieldInitializationStep name(String name)
   {
      return setType(new FieldDsl(this), name, (fieldDsl, s) -> fieldDsl.name = s);
   }

   @Override
   public FieldNameStep type(String type)
   {
      return setType(new FieldDsl(this), type, (fieldDsl, string) -> fieldDsl.type = renderingContext -> string);
   }

   @Override
   public FieldNameStep type(VariableTypeRenderable type)
   {
      return setTypeRenderer(new FieldDsl(this),
                             type,
                             (renderingContext, variableTypeRenderable) -> variableTypeRenderable.renderType(renderingContext),
                             (fieldDsl, function) -> fieldDsl.type = function);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
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

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return name;
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
      public String renderDeclaration(RenderingContext renderingContext)
      {
         StringBuilder sb = new StringBuilder();

         sb.append(fieldDsl.partialRender(renderingContext));

         for (int i = 0; i < initializers.size(); i++)
         {
            sb.append(", ")
              .append(names.get(i))
              .append(" = ")
              .append(initializers.get(i));
         }

         if (names.size() == initializers.size() + 1)
         {
            sb.append(", ")
              .append(names.getLast());
         }
         sb.append(';');

         return sb.toString();
      }

      @Override
      public String renderName(RenderingContext renderingContext)
      {
         return fieldDsl.name;
      }
   }
}
