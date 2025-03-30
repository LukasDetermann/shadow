package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.class_.*;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class ClassDsl
      implements ClassJavaDocStep,
                 ClassGenericStep
{
   private Function<RenderingContext, String> javadoc;
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();
   private final List<Function<RenderingContext, String>> modifiers = new ArrayList<>();
   private String name;
   private final List<Function<RenderingContext, String>> generics = new ArrayList<>();
   private Function<RenderingContext, String> extends_;
   private final List<Function<RenderingContext, String>> implements_ = new ArrayList<>();
   private final List<Function<RenderingContext, String>> permits = new ArrayList<>();
   private final List<Function<RenderingContext, String>> fields = new ArrayList<>();
   private final List<Function<RenderingContext, String>> methods = new ArrayList<>();
   private final List<Function<RenderingContext, String>> inner = new ArrayList<>();
   private final List<Function<RenderingContext, String>> instanceInitializers = new ArrayList<>();
   private final List<Function<RenderingContext, String>> staticInitializers = new ArrayList<>();
   private final List<Function<RenderingContext, String>> constructors = new ArrayList<>();
   private String body;

   public ClassDsl()
   {
   }

   private ClassDsl(ClassDsl other)
   {
      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.modifiers.addAll(other.modifiers);
      this.name = other.name;
      this.generics.addAll(other.generics);
      this.extends_ = other.extends_;
      this.implements_.addAll(other.implements_);
      this.permits.addAll(other.permits);
      this.fields.addAll(other.fields);
      this.methods.addAll(other.methods);
      this.inner.addAll(other.inner);
      this.instanceInitializers.addAll(other.instanceInitializers);
      this.staticInitializers.addAll(other.staticInitializers);
      this.constructors.addAll(other.constructors);
      this.body = other.body;
   }

   @Override
   public ClassAnnotateStep javadoc(String javadoc)
   {
      return set(new ClassDsl(this),
                 (classDsl, function) -> classDsl.javadoc = function,
                 javadoc);
   }

   @Override
   public ClassAnnotateStep annotate(String... annotation)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.annotations::add, annotation);
   }

   @Override
   public ClassAnnotateStep annotate(C_Annotation... annotation)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.annotations::add,
                 (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                 annotation);
   }

   @Override
   public ClassModifierStep modifier(String... modifiers)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.modifiers::add, modifiers);
   }

   @Override
   public ClassModifierStep modifier(C_Modifier... modifiers)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (context, modifier) -> Renderer.render(modifier).declaration(context),
                 modifiers);
   }

   @Override
   public ClassModifierStep abstract_()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.ABSTRACT);
   }

   @Override
   public ClassModifierStep public_()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PUBLIC);
   }

   @Override
   public ClassModifierStep protected_()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PROTECTED);
   }

   @Override
   public ClassModifierStep private_()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.PRIVATE);
   }

   @Override
   public ClassModifierStep final_()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.FINAL);
   }

   @Override
   public ClassModifierStep sealed()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.SEALED);
   }

   @Override
   public ClassModifierStep nonSealed()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.NON_SEALED);
   }

   @Override
   public ClassModifierStep static_()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.STATIC);
   }

   @Override
   public ClassModifierStep strictfp_()
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                 C_Modifier.STRICTFP);
   }

   @Override
   public ClassGenericStep name(String name)
   {
      return setString(new ClassDsl(this),
                       (classDsl, s) -> classDsl.name = s,
                       name);
   }

   @Override
   public ClassGenericStep generic(String... generic)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.generics::add, generic);
   }

   @Override
   public ClassGenericStep generic(C_Generic... generic)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.generics::add,
                 (context, generic1) -> Renderer.render(generic1).declaration(context),
                 generic);
   }

   @Override
   public ClassImplementsStep extends_(String aClass)
   {
      return set(new ClassDsl(this), (classDsl, function) -> classDsl.extends_ = function, aClass);
   }

   @Override
   public ClassImplementsStep extends_(C_Class aClass)
   {
      return set(new ClassDsl(this),
                 (classDsl, function) -> classDsl.extends_ = function,
                 ((renderingContext, cClass) -> Renderer.render(cClass).type(renderingContext)),
                 aClass);
   }

   @Override
   public ClassImplementsStep implements_(String... interfaces)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.implements_::add, interfaces);
   }

   @Override
   public ClassImplementsStep implements_(C_Interface... interfaces)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.implements_::add,
                 (context, cInterface) -> Renderer.render(cInterface).declaration(context),
                 interfaces);
   }

   @Override
   public ClassPermitsStep permits(String... declared)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.permits::add, declared);
   }

   @Override
   public ClassPermitsStep permits(C_Declared... declared)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.permits::add,
                 (context, declared1) -> Renderer.render(declared1).declaration(context),
                 declared);
   }

   @Override
   public ClassRenderable body(String body)
   {
      return setString(new ClassDsl(this),
                       (classDsl, function) -> classDsl.body = function,
                       body);
   }

   @Override
   public ClassBodyStep field(String... fields)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.fields::add, fields);
   }

   @Override
   public ClassBodyStep field(C_Field... fields)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.fields::add,
                 (context, field) -> Renderer.render(field).declaration(context),
                 fields);
   }

   @Override
   public ClassBodyStep method(String... methods)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.methods::add, methods);
   }

   @Override
   public ClassBodyStep method(C_Method... methods)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.modifiers::add,
                 (context, method) -> Renderer.render(method).declaration(context),
                 methods);
   }

   @Override
   public ClassBodyStep inner(String... inner)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.inner::add, inner);
   }

   @Override
   public ClassBodyStep inner(C_Declared... inner)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.inner::add,
                 (context, declared) -> Renderer.render(declared).declaration(context),
                 inner);
   }

   @Override
   public ClassBodyStep instanceInitializer(String... instanceInitializers)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.instanceInitializers::add, instanceInitializers);
   }

   @Override
   public ClassBodyStep staticInitializer(String... staticInitializer)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.staticInitializers::add, staticInitializer);
   }

   @Override
   public ClassBodyStep constructor(String... constructors)
   {
      return add(new ClassDsl(this), classDsl -> classDsl.constructors::add, constructors);
   }

   @Override
   public ClassBodyStep constructor(C_Constructor... constructors)
   {
      return add(new ClassDsl(this),
                 classDsl -> classDsl.constructors::add,
                 (context, constructor) -> Renderer.render(constructor).declaration(context),
                 constructors);
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
      sb.append("class ");
      sb.append(name);
      sb.append(' ');

      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
         sb.append('>');
         sb.append(' ');
      }
      if (extends_ != null)
      {
         sb.append(extends_.apply(renderingContext));
      }
      if (!implements_.isEmpty())
      {
         sb.append("implements ");
         sb.append(implements_.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
         sb.append(' ');
      }
      if (!permits.isEmpty())
      {
         sb.append("permits ");
         sb.append(permits.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
         sb.append(' ');
      }
      sb.append('{');
      if (body != null)
      {
         sb.append(body);
      }
      sb.append('{');

      return sb.toString();
   }
}
