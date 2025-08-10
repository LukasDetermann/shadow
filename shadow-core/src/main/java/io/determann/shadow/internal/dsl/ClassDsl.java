package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.class_.*;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.renderer.RenderingContext.renderingContextBuilder;
import static io.determann.shadow.internal.dsl.DslSupport.*;

public class ClassDsl
      implements ClassCopyrightHeaderStep,
                 ClassImportStep,
                 ClassGenericStep
{
   /// optional and only for files
   private String copyright;
   private PackageRenderable package_;
   private final List<Renderable> imports = new ArrayList<>();

   /// for all
   private Renderable javadoc;
   private final List<Renderable> annotations = new ArrayList<>();
   private final List<Renderable> modifiers = new ArrayList<>();
   private String name;
   private final List<Renderable> generics = new ArrayList<>();
   private Renderable extends_;
   private final List<Renderable> implements_ = new ArrayList<>();
   private final List<Renderable> permits = new ArrayList<>();
   private final List<Renderable> fields = new ArrayList<>();
   private final List<Renderable> methods = new ArrayList<>();
   private final List<Renderable> inner = new ArrayList<>();
   private final List<Renderable> instanceInitializers = new ArrayList<>();
   private final List<Renderable> staticInitializers = new ArrayList<>();
   private final List<Renderable> constructors = new ArrayList<>();
   private String body;

   public ClassDsl()
   {
   }

   private ClassDsl(ClassDsl other)
   {
      this.copyright = other.copyright;
      this.package_ = other.package_;
      this.imports.addAll(other.imports);

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
      return setTypeRenderer(new ClassDsl(this),
                             javadoc,
                             (classDsl, function) -> classDsl.javadoc = function);
   }

   @Override
   public ClassAnnotateStep annotate(String... annotation)
   {
      return addArray2(new ClassDsl(this), annotation, (classDsl, string) -> classDsl.annotations.add(renderingContext -> '@' + string));
   }

   @Override
   public ClassAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new ClassDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              classDsl -> classDsl.annotations::add);
   }

   @Override
   public ClassModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new ClassDsl(this), modifiers, classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep modifier(Set<Modifier> modifiers)
   {
      return addArray2(new ClassDsl(this),
                       modifiers,
                       (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep abstract_()
   {
      return setType(new ClassDsl(this),
                     Modifier.ABSTRACT,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep public_()
   {
      return setType(new ClassDsl(this),
                     Modifier.PUBLIC,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep protected_()
   {
      return setType(new ClassDsl(this),
                     Modifier.PROTECTED,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep private_()
   {
      return setType(new ClassDsl(this),
                     Modifier.PRIVATE,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep final_()
   {
      return setType(new ClassDsl(this),
                     Modifier.FINAL,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep sealed()
   {
      return setType(new ClassDsl(this),
                     Modifier.SEALED,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep nonSealed()
   {
      return setType(new ClassDsl(this),
                     Modifier.NON_SEALED,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep static_()
   {
      return setType(new ClassDsl(this),
                     Modifier.STATIC,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassModifierStep strictfp_()
   {
      return setType(new ClassDsl(this),
                     Modifier.STRICTFP,
                     (classDsl, modifier) -> classDsl.modifiers.add(modifier::render));
   }

   @Override
   public ClassGenericStep name(String name)
   {
      return setType(new ClassDsl(this), name, (classDsl, s) -> classDsl.name = s);
   }

   @Override
   public ClassGenericStep generic(String... generic)
   {
      return addArray2(new ClassDsl(this), generic, (classDsl, string) -> classDsl.generics.add(renderingContext -> string));
   }

   @Override
   public ClassGenericStep generic(List<? extends GenericRenderable> generics)
   {
      return addArrayRenderer(new ClassDsl(this),
                              generics,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              classDsl -> classDsl.generics::add);
   }

   @Override
   public ClassImplementsStep extends_(String aClass)
   {
      return setType(new ClassDsl(this), aClass, (classDsl, string) -> classDsl.extends_ = renderingContext -> string);
   }

   @Override
   public ClassImplementsStep extends_(ClassRenderable aClass)
   {
      return setTypeRenderer(new ClassDsl(this),
                             aClass,
                             (renderingContext, renderable) -> renderable.renderType(renderingContext),
                             (classDsl, function) -> classDsl.extends_ = function);
   }

   @Override
   public ClassImplementsStep implements_(String... interfaces)
   {
      return addArray2(new ClassDsl(this), interfaces, (classDsl, string) -> classDsl.implements_.add(renderingContext -> string));
   }

   @Override
   public ClassImplementsStep implements_(List<? extends InterfaceRenderable> interfaces)
   {
      return addArrayRenderer(new ClassDsl(this),
                              interfaces,
                              (renderingContext, renderable) -> renderable.renderType(renderingContext),
                              classDsl -> classDsl.implements_::add);
   }

   @Override
   public ClassPermitsStep permits(String... declared)
   {
      return addArray2(new ClassDsl(this), declared, (classDsl, string) -> classDsl.permits.add(renderingContext -> string));
   }

   @Override
   public ClassPermitsStep permits(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new ClassDsl(this),
                              declared,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              classDsl -> classDsl.permits::add);
   }

   @Override
   public ClassRenderable body(String body)
   {
      return setType(new ClassDsl(this),
                     body, (classDsl, function) -> classDsl.body = function);
   }

   @Override
   public ClassBodyStep field(String... fields)
   {
      return addArray2(new ClassDsl(this), fields, (classDsl, string) -> classDsl.fields.add(renderingContext -> string));
   }

   @Override
   public ClassBodyStep field(List<? extends FieldRenderable> fields)
   {
      return addArrayRenderer(new ClassDsl(this),
                              fields,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              classDsl -> classDsl.fields::add);
   }

   @Override
   public ClassBodyStep method(String... methods)
   {
      return addArray2(new ClassDsl(this), methods, (classDsl, string) -> classDsl.methods.add(renderingContext -> string));
   }

   @Override
   public ClassBodyStep method(List<? extends MethodRenderable> methods)
   {
      return addArrayRenderer(new ClassDsl(this),
                              methods,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              classDsl -> classDsl.methods::add);
   }

   @Override
   public ClassBodyStep inner(String... inner)
   {
      return addArray2(new ClassDsl(this), inner, (classDsl, string) -> classDsl.inner.add(renderingContext -> string));
   }

   @Override
   public ClassBodyStep inner(List<? extends DeclaredRenderable> inner)
   {
      return addArrayRenderer(new ClassDsl(this),
                              inner,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              classDsl -> classDsl.inner::add);
   }

   @Override
   public ClassBodyStep instanceInitializer(String... instanceInitializers)
   {
      return addArrayRenderer(new ClassDsl(this), instanceInitializers, classDsl -> classDsl.instanceInitializers::add);
   }

   @Override
   public ClassBodyStep staticInitializer(String... staticInitializer)
   {
      return addArrayRenderer(new ClassDsl(this), staticInitializer, classDsl -> classDsl.staticInitializers::add);
   }

   @Override
   public ClassBodyStep constructor(String... constructors)
   {
      return addArray2(new ClassDsl(this), constructors, (classDsl, string) -> classDsl.constructors.add(renderingContext -> string));
   }

   @Override
   public ClassBodyStep constructor(List<? extends ConstructorRenderable> constructors)
   {
      return addArrayRenderer(new ClassDsl(this),
                              constructors,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              classDsl -> classDsl.constructors::add);
   }

   @Override
   public ClassPackageStep copyright(String copyrightHeader)
   {
      return setType(new ClassDsl(this),
                     copyrightHeader,
                     (classDsl, string) -> classDsl.copyright = string);
   }

   @Override
   public ClassImportStep package_(String packageName)
   {
      return setType(new ClassDsl(this),
                     packageName,
                     (classDsl, string) -> classDsl.package_ = renderingContext -> string);
   }

   @Override
   public ClassImportStep package_(PackageRenderable aPackage)
   {
      return setType(new ClassDsl(this),
                     aPackage,
                     (classDsl, string) -> classDsl.package_ = string);
   }

   @Override
   public ClassImportStep import_(String... name)
   {
      return addArray2(new ClassDsl(this),
                       name,
                       (classDsl, string) -> classDsl.imports.add(renderingContext -> Dsl.import_(string).renderDeclaration(renderingContext)));
   }

   @Override
   public ClassImportStep import_(List<? extends ImportRenderable> declared)
   {
      return addArray2(new ClassDsl(this),
                       declared,
                       (classDsl, importRenderable) -> classDsl.imports.add(importRenderable::renderDeclaration));
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      RenderingContext context = renderingContextBuilder(renderingContext)
            .addSurrounding(this)
            .build();

      StringBuilder sb = new StringBuilder();
      if (copyright != null)
      {
         sb.append(copyright);
         sb.append('\n');
      }

      if (package_ != null)
      {
         sb.append(package_.renderDeclaration(context))
           .append("\n\n");
      }

      renderElement(sb, imports, context, "\n");
      if (!imports.isEmpty())
      {
         sb.append("\n\n");
      }

      if (javadoc != null)
      {
         sb.append(javadoc.render(context));
         sb.append("\n");
      }

      renderElement(sb, annotations, "\n", context, "\n");
      renderElement(sb, modifiers, " ", context, " ");

      sb.append("class ");
      sb.append(name);
      sb.append(' ');

      renderElement(sb, "<", generics, "> ", context, ", ");

      if (extends_ != null)
      {
         sb.append("extends ")
           .append(extends_.render(context))
           .append(' ');
      }

      renderElement(sb, "implements ", implements_, " ", context, ", ");
      renderElement(sb, "permits ", permits, " ", context, ", ");

      sb.append("{\n");
      if (body != null)
      {
         sb.append(body)
           .append('\n');
      }
      else
      {
         renderElement(sb, fields, "\n", context, "\n");
         renderElement(sb, staticInitializers, "\n\n", context, "\n");
         renderElement(sb, constructors, "\n\n", context, "\n");
         renderElement(sb, instanceInitializers, "\n\n", context, "\n");
         renderElement(sb, methods, "\n\n", context, "\n");
         renderElement(sb, inner, "\n\n", context, "\n");
      }
      sb.append('}');

      return sb.toString();
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      if (package_ == null)
      {
         return name;
      }
      return package_.renderQualifiedName(renderingContextBuilder(renderingContext)
                                                .addSurrounding(this)
                                                .build()) + '.' + name;
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      RenderingContext context = renderingContextBuilder(renderingContext)
            .addSurrounding(this)
            .build();

      String qualifiedName = renderQualifiedName(context);
      if (generics.isEmpty())
      {
         return qualifiedName;
      }
      return qualifiedName +
             '<' +
             generics.stream().map(renderable -> renderable.render(context)).collect(Collectors.joining(", ")) +
             '>';
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return name;
   }
}
