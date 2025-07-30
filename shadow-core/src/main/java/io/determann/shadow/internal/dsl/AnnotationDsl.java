package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation.*;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.renderer.RenderingContext.renderingContextBuilder;
import static io.determann.shadow.api.renderer.RenderingContextOptions.RECEIVER_TYPE;
import static io.determann.shadow.internal.dsl.DslSupport.*;

public class AnnotationDsl
      implements
      AnnotationCopyrightHeaderStep,
      AnnotationImportStep,
      AnnotationBodyStep

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
      return addArray2(new AnnotationDsl(this),
                       annotation,
                       (annotationDsl, string) -> annotationDsl.annotations.add(renderingContext -> '@' + string));
   }


   @Override
   public AnnotationAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
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
      return addArray2(new AnnotationDsl(this), fields, (annotationDsl, s) -> annotationDsl.fields.add(renderingContext -> s));
   }

   @Override
   public AnnotationBodyStep field(List<? extends FieldRenderable> fields)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              fields,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              annotationDsl -> annotationDsl.fields::add);
   }

   @Override
   public AnnotationBodyStep method(String... methods)
   {
      return addArray2(new AnnotationDsl(this), methods, (annotationDsl, string) -> annotationDsl.methods.add(renderingContext -> string));
   }

   @Override
   public AnnotationBodyStep method(List<? extends MethodRenderable> methods)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              methods,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              annotationDsl -> annotationDsl.methods::add);
   }

   @Override
   public AnnotationBodyStep method(MethodRenderable method, AnnotationValueRenderable defaultValue)
   {
      record Pair(MethodRenderable method,
                  AnnotationValueRenderable defaultValue) {}

      return setType(new AnnotationDsl(this),
                     new Pair(method, defaultValue),
                     (annotationDsl, pair) ->
                           annotationDsl.methods.add(renderingContext -> pair.method().renderDeclaration(renderingContext)
                                                                         + " default " +
                                                                         pair.defaultValue().render(renderingContext) + ';'));
   }

   @Override
   public AnnotationBodyStep inner(String... inner)
   {
      return addArray2(new AnnotationDsl(this), inner, (annotationDsl, string) -> annotationDsl.inner.add(renderingContext -> string));
   }

   @Override
   public AnnotationBodyStep inner(List<? extends DeclaredRenderable> inner)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              inner,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              annotationDsl -> annotationDsl.inner::add);
   }

   @Override
   public AnnotationModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new AnnotationDsl(this), modifiers, annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep modifier(Set<C_Modifier> modifiers)
   {
      return addArray(new AnnotationDsl(this),
                      modifiers,
                      annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep public_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.PUBLIC,
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep protected_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.PROTECTED,
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep private_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.PRIVATE,
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep abstract_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.ABSTRACT,
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep sealed()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.SEALED,
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep nonSealed()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.NON_SEALED,
                             annotationDsl -> annotationDsl.modifiers::add);
   }

   @Override
   public AnnotationModifierStep strictfp_()
   {
      return addTypeRenderer(new AnnotationDsl(this),
                             C_Modifier.STRICTFP,
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
                     "package " + packageName + ';',
                     (annotationDsl, string) -> annotationDsl.package_ = renderingContext -> packageName);
   }

   @Override
   public AnnotationImportStep package_(PackageRenderable aPackage)
   {
      return setType(new AnnotationDsl(this),
                     aPackage,
                     (annotationDsl, packageRenderable) -> annotationDsl.package_ = packageRenderable);
   }

   @Override
   public AnnotationImportStep import_(String... name)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              name,
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep import_(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              declared,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep importPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> packageRenderable.renderQualifiedName(renderingContext) + ".*",
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
   public AnnotationImportStep staticImport(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              declared,
                              (renderingContext, renderable) -> "static " + renderable.renderQualifiedName(renderingContext),
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public AnnotationImportStep staticImportPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> "static " +
                                                                       packageRenderable.renderQualifiedName(renderingContext) +
                                                                       ".*",
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      StringBuilder sb = new StringBuilder();
      if (copyright != null)
      {
         sb.append(copyright);
         sb.append('\n');
      }

      if (package_ != null)
      {
         sb.append(package_.renderDeclaration(renderingContext));
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

      if (!annotations.isEmpty())
      {
         sb.append(annotations.stream().map(renderable -> renderable.render(renderingContext)).collect(Collectors.joining("\n")));
         sb.append('\n');
      }

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
         RenderingContext forReceiver = renderingContextBuilder(renderingContext)
               .withOption(RECEIVER_TYPE, name)
               .build();

         renderElement(sb, fields, "\n", renderingContext, "\n");
         renderElement(sb, methods, "\n\n", forReceiver, "\n");
         renderElement(sb, inner, "\n\n", forReceiver, "\n");
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
      return package_.renderQualifiedName(renderingContext) + '.' + name;
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return renderQualifiedName(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return name;
   }
}
