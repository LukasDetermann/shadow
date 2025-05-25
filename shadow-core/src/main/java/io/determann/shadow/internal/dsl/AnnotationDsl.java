package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation.*;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.internal.renderer.RenderingContextWrapper;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.dsl.DslSupport.*;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public class AnnotationDsl
      implements
      AnnotationCopyrightHeaderStep,
      AnnotationImportStep,
      AnnotationBodyStep

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
   private final List<Renderable> fields = new ArrayList<>();
   private final List<Renderable> methods = new ArrayList<>();
   private final List<Renderable> inner = new ArrayList<>();
   private String body;

   public AnnotationDsl()
   {
   }

   private AnnotationDsl(AnnotationDsl other)
   {
      this.copyright = other.copyright;
      this.package_ = other.package_;
      this.imports.addAll(other.imports);
      
      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.modifiers.addAll(other.modifiers);
      this.name = other.name;
      this.fields.addAll(other.fields);
      this.methods.addAll(other.methods);
      this.inner.addAll(other.inner);
      this.body = other.body;
   }

   @Override
   public AnnotationAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new AnnotationDsl(this),
                             javadoc,
                             (annotationDsl, function) -> annotationDsl.javadoc = function);
   }

   @Override
   public AnnotationAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new AnnotationDsl(this), annotation, annotationDsl -> annotationDsl.annotations::add);
   }

   @Override
   public AnnotationAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              annotation,
                              (context, cAnnotation) -> Renderer.render(cAnnotation).declaration(context),
                              annotationDsl -> annotationDsl.annotations::add);
   }

   @Override
   public AnnotationAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new AnnotationDsl(this),
                      annotation,
                      annotationDsl -> annotationDsl.annotations::add);
   }

   @Override
   public AnnotationRenderable body(String body)
   {
      return setType(new AnnotationDsl(this),
                     body, (annotationDsl, function) -> annotationDsl.body = function);
   }

   @Override
   public AnnotationBodyStep field(String... fields)
   {
      return addArrayRenderer(new AnnotationDsl(this), fields, annotationDsl -> annotationDsl.fields::add);
   }

   @Override
   public AnnotationBodyStep field(C_Field... fields)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              fields,
                              (context, field) -> Renderer.render(field).declaration(context),
                              annotationDsl -> annotationDsl.fields::add);
   }

   @Override
   public AnnotationBodyStep field(FieldRenderable... fields)
   {
      return addArray(new AnnotationDsl(this),
                      fields,
                      annotationDsl -> annotationDsl.fields::add);
   }

   @Override
   public AnnotationBodyStep method(String... methods)
   {
      return addArrayRenderer(new AnnotationDsl(this), methods, annotationDsl -> annotationDsl.methods::add);
   }

   @Override
   public AnnotationBodyStep method(C_Method... methods)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              methods,
                              (context, method) -> renderMethod(method, context) + ';',
                              annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationBodyStep method(C_Method method, C_AnnotationValue defaultValue)
   {
      record Pair(C_Method method,
                  C_AnnotationValue defaultValue) {}

      return setTypeRenderer(new AnnotationDsl(this),
                             new Pair(method, defaultValue),
                             (renderingContext, pair) ->
                                   renderMethod(pair.method(), renderingContext)
                                   + " default " +
                                   Renderer.render(pair.defaultValue()).declaration(renderingContext) + ';',
                             (annotationDsl, renderer) -> annotationDsl.methods.add(renderer));
   }

   /// without ';'
   private static String renderMethod(C_Method method, RenderingContext renderingContext)
   {
      C_Type returnType = requestOrThrow(method, METHOD_GET_RETURN_TYPE);
      String name = requestOrThrow(method, NAMEABLE_GET_NAME);

      return Renderer.render(returnType).type(renderingContext) +
             ' ' +
             name +
             "()";
   }

   @Override
   public AnnotationBodyStep inner(String... inner)
   {
      return addArrayRenderer(new AnnotationDsl(this), inner, annotationDsl -> annotationDsl.inner::add);
   }

   @Override
   public AnnotationBodyStep inner(C_Declared... inner)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              inner,
                              (context, declared) -> Renderer.render(declared).declaration(context),
                              annotationDsl -> annotationDsl.inner::add);
   }

   @Override
   public AnnotationBodyStep inner(DeclaredRenderable... inner)
   {
      return addArray(new AnnotationDsl(this),
                      inner,
                      annotationDsl -> annotationDsl.inner::add);
   }

   @Override
   public AnnotationModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new AnnotationDsl(this), modifiers, annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep modifier(C_Modifier... modifiers)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              modifiers,
                              (context, modifier) -> Renderer.render(modifier).declaration(context),
                              annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep public_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.PUBLIC,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep protected_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.PROTECTED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep private_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.PRIVATE,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep abstract_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.ABSTRACT,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep sealed()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.SEALED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep nonSealed()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.NON_SEALED,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep strictfp_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.STRICTFP,
                             (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationBodyStep name(String name)
   {
      return setType(new AnnotationDsl(this), name, (annotationDsl, s) -> annotationDsl.name = s);
   }

   @Override
   public AnnotationPackageStep copyright(String copyrightHeader)
   {
      return setType(new AnnotationDsl(this),
                     copyrightHeader,
                     (annotationDsl, string) -> annotationDsl.copyright = string);
   }

   @Override
   public AnnotationImportStep package_(String packageName)
   {
      return setType(new AnnotationDsl(this),
                     packageName,
                     (annotationDsl, string) -> annotationDsl.package_ = string);
   }

   @Override
   public AnnotationImportStep package_(C_Package aPackage)
   {
      return setType(new AnnotationDsl(this),
                     aPackage,
                     cPackage -> requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                     (annotationDsl, string) -> annotationDsl.package_ = string);
   }

   @Override
   public AnnotationImportStep import_(String... name)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              name,
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep import_(C_Declared... declared)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              declared,
                              (renderingContext, declared1) -> requestOrThrow(declared1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep import_(C_Package... cPackages)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              cPackages,
                              (renderingContext, aPackage) -> requestOrThrow(aPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) + ".*",
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep staticImport(String... name)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              name,
                              (renderingContext, string) -> "static " + string,
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep staticImport(C_Declared... declared)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              declared,
                              (renderingContext, declared1) -> "static " + requestOrThrow(declared1, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME),
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep staticImport(C_Package... cPackages)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              cPackages,
                              (renderingContext, aPackage) -> "static " + requestOrThrow(aPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) + ".*",
                              annotationDsl -> annotationDsl.imports::add);
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

      sb.append("@interface ");
      sb.append(name);
      sb.append(' ');

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
         renderElement(sb, methods, "\n\n", forReceiver, "\n");
         renderElement(sb, inner, "\n\n", forReceiver, "\n");
      }
      sb.append('}');

      return sb.toString();
   }
}
