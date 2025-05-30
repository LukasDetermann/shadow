package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.class_.*;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.internal.renderer.RenderingContextWrapper;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.*;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public class ClassDsl
      implements ClassCopyrightHeaderStep,
                 ClassImportStep,
                 ClassGenericStep
{
   /// optional and only for files
   private String copyright;
   private String package_;
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
      return addArrayRenderer(new ClassDsl(this), annotation, (renderingContext, string) -> '@' + string, classDsl -> classDsl.annotations::add);
   }

   @Override
   public ClassAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new ClassDsl(this),
                              annotation,
                              (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                              classDsl -> classDsl.annotations::add);
   }

   @Override
   public ClassAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new ClassDsl(this),
                      annotation,
                      classDsl -> classDsl.annotations::add);
   }

   @Override
   public ClassModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new ClassDsl(this), modifiers, classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep modifier(C_Modifier... modifiers)
   {
      return addArrayRenderer(new ClassDsl(this),
                              modifiers,
                              (context, modifier) -> Renderer.render(modifier).declaration(context),
                              classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep abstract_()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.ABSTRACT,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep public_()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.PUBLIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep protected_()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.PROTECTED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep private_()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.PRIVATE,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep final_()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.FINAL,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep sealed()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.SEALED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep nonSealed()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.NON_SEALED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep static_()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.STATIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassModifierStep strictfp_()
   {
      return addTypeRenderer(new ClassDsl(this),
                             C_Modifier.STRICTFP,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassGenericStep name(String name)
   {
      return setType(new ClassDsl(this), name, (classDsl, s) -> classDsl.name = s);
   }

   @Override
   public ClassGenericStep generic(String... generic)
   {
      return addArrayRenderer(new ClassDsl(this), generic, classDsl -> classDsl.generics::add);
   }

   @Override
   public ClassGenericStep generic(C_Generic... generic)
   {
      return addArrayRenderer(new ClassDsl(this),
                              generic,
                              (context, generic1) -> Renderer.render(generic1).declaration(context),
                              classDsl -> classDsl.generics::add);
   }

   @Override
   public ClassImplementsStep extends_(String aClass)
   {
      return setTypeRenderer(new ClassDsl(this), aClass, (classDsl, function) -> classDsl.extends_ = function);
   }

   @Override
   public ClassImplementsStep extends_(C_Class aClass)
   {
      return setTypeRenderer(new ClassDsl(this),
                             aClass,
                             (renderingContext, cClass) -> Renderer.render(cClass).type(renderingContext),
                             (classDsl, function) -> classDsl.extends_ = function);
   }

   @Override
   public ClassImplementsStep implements_(String... interfaces)
   {
      return addArrayRenderer(new ClassDsl(this), interfaces, classDsl -> classDsl.implements_::add);
   }

   @Override
   public ClassImplementsStep implements_(C_Interface... interfaces)
   {
      return addArrayRenderer(new ClassDsl(this),
                              interfaces,
                              (context, cInterface) -> Renderer.render(cInterface).declaration(context),
                              classDsl -> classDsl.implements_::add);
   }

   @Override
   public ClassPermitsStep permits(String... declared)
   {
      return addArrayRenderer(new ClassDsl(this), declared, classDsl -> classDsl.permits::add);
   }

   @Override
   public ClassPermitsStep permits(C_Declared... declared)
   {
      return addArrayRenderer(new ClassDsl(this),
                              declared,
                              (context, declared1) -> Renderer.render(declared1).declaration(context),
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
      return addArrayRenderer(new ClassDsl(this), fields, classDsl -> classDsl.fields::add);
   }

   @Override
   public ClassBodyStep field(C_Field... fields)
   {
      return addArrayRenderer(new ClassDsl(this),
                              fields,
                              (context, field) -> Renderer.render(field).declaration(context),
                              classDsl -> classDsl.fields::add);
   }

   @Override
   public ClassBodyStep field(FieldRenderable... fields)
   {
      return addArray(new ClassDsl(this),
                      fields,
                      classDsl -> classDsl.fields::add);
   }

   @Override
   public ClassBodyStep method(String... methods)
   {
      return addArrayRenderer(new ClassDsl(this), methods, classDsl -> classDsl.methods::add);
   }

   @Override
   public ClassBodyStep method(C_Method... methods)
   {
      return addArrayRenderer(new ClassDsl(this),
                              methods,
                              (context, method) -> Renderer.render(method).declaration(context),
                              classDsl -> classDsl.modifiers::add);
   }

   @Override
   public ClassBodyStep method(MethodRenderable... methods)
   {
      return addArray(new ClassDsl(this),
                      methods,
                      classDsl -> classDsl.methods::add);
   }

   @Override
   public ClassBodyStep inner(String... inner)
   {
      return addArrayRenderer(new ClassDsl(this), inner, classDsl -> classDsl.inner::add);
   }

   @Override
   public ClassBodyStep inner(C_Declared... inner)
   {
      return addArrayRenderer(new ClassDsl(this),
                              inner,
                              (context, declared) -> Renderer.render(declared).declaration(context),
                              classDsl -> classDsl.inner::add);
   }

   @Override
   public ClassBodyStep inner(DeclaredRenderable... inner)
   {
      return addArray(new ClassDsl(this),
                      inner,
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
      return addArrayRenderer(new ClassDsl(this), constructors, classDsl -> classDsl.constructors::add);
   }

   @Override
   public ClassBodyStep constructor(C_Constructor... constructors)
   {
      return addArrayRenderer(new ClassDsl(this),
                              constructors,
                              (context, constructor) -> Renderer.render(constructor).declaration(context),
                              classDsl -> classDsl.constructors::add);
   }

   @Override
   public ClassBodyStep constructor(ConstructorRenderable... constructors)
   {
      return addArray(new ClassDsl(this),
                      constructors,
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
                     (classDsl, string) -> classDsl.package_ = string);
   }

   @Override
   public ClassImportStep package_(C_Package aPackage)
   {
      return setType(new ClassDsl(this),
                     aPackage,
                     cPackage -> requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                     (classDsl, string) -> classDsl.package_ = string);
   }

   @Override
   public ClassImportStep import_(String... name)
   {
      return addArrayRenderer(new ClassDsl(this),
                              name,
                              classDsl -> classDsl.imports::add);
   }

   @Override
   public ClassImportStep import_(C_Declared... declared)
   {
      return addArrayRenderer(new ClassDsl(this),
                              declared,
                              (renderingContext, declared1) -> requestOrThrow(declared1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                              classDsl -> classDsl.imports::add);
   }

   @Override
   public ClassImportStep import_(C_Package... cPackages)
   {
      return addArrayRenderer(new ClassDsl(this),
                              cPackages,
                              (renderingContext, aPackage) -> requestOrThrow(aPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) + ".*",
                              classDsl -> classDsl.imports::add);
   }

   @Override
   public ClassImportStep staticImport(String... name)
   {
      return addArrayRenderer(new ClassDsl(this),
                              name,
                              (renderingContext, string) -> "static " + string,
                              classDsl -> classDsl.imports::add);
   }

   @Override
   public ClassImportStep staticImport(C_Declared... declared)
   {
      return addArrayRenderer(new ClassDsl(this),
                              declared,
                              (renderingContext, declared1) -> "static " + requestOrThrow(declared1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                              classDsl -> classDsl.imports::add);
   }

   @Override
   public ClassImportStep staticImport(C_Package... cPackages)
   {
      return addArrayRenderer(new ClassDsl(this),
                              cPackages,
                              (renderingContext, aPackage) -> "static " + requestOrThrow(aPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) + ".*",
                              classDsl -> classDsl.imports::add);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (copyright != null)
      {
         sb.append(copyright);
         sb.append('\n');
      }

      if (package_ != null)
      {
         sb.append("package ");
         sb.append(package_);
         sb.append(';');
      }

      renderElement(sb, "import ", imports, ";", renderingContext, "\n");
      if (!imports.isEmpty())
      {
         sb.append("\n\n");
      }

      if (javadoc != null)
      {
         sb.append(javadoc.render(renderingContext));
         sb.append("\n");
      }

      renderElement(sb, annotations, "\n", renderingContext, "\n");
      renderElement(sb, generics, " ", renderingContext, " ");

      sb.append("class ");
      sb.append(name);
      sb.append(' ');

      renderElement(sb, "<", generics, "> ", renderingContext, ", ");

      if (extends_ != null)
      {
         sb.append(extends_.render(renderingContext));
      }

      renderElement(sb, "implements ", implements_, " ", renderingContext, ", ");
      renderElement(sb, "permits ", permits, " ", renderingContext, ", ");

      sb.append("{\n");
      if (body != null)
      {
         sb.append(body);
      }
      else
      {
         RenderingContextWrapper forReceiver = wrap(renderingContext);
         forReceiver.setReceiverType(name);

         renderElement(sb, fields, "\n", renderingContext, "\n");
         renderElement(sb, staticInitializers, "\n\n", renderingContext, "\n");
         renderElement(sb, constructors, "\n\n", renderingContext, "\n");
         renderElement(sb, instanceInitializers, "\n\n", renderingContext, "\n");
         renderElement(sb, methods, "\n\n", forReceiver, "\n");
         renderElement(sb, inner, "\n\n", forReceiver, "\n");
      }
      sb.append('}');

      return sb.toString();
   }
}
