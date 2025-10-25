package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.annotation.*;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class AnnotationDsl
      implements
      AnnotationCopyrightHeaderStep,
      AnnotationImportStep,
      AnnotationBodyStep,
      AnnotationOuterStep

{
   /// optional only for inner
   private Renderable outerType;

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
   private Renderable body;

   public AnnotationDsl()
   {
   }

   private AnnotationDsl(AnnotationDsl other)
   {
      this.outerType = other.outerType;

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
   public AnnotationJavaDocStep outer(String qualifiedOuterType)
   {
      return setTypeRenderer(new AnnotationDsl(this), qualifiedOuterType, (classDsl, s) -> classDsl.outerType = s);
   }

   @Override
   public AnnotationJavaDocStep outer(DeclaredRenderable outerType)
   {
      return setTypeRenderer(new AnnotationDsl(this),
                             outerType,
                             (renderingContext, declaredRenderable) -> declaredRenderable.renderQualifiedName(renderingContext),
                             (classDsl, s) -> classDsl.outerType = s);
   }

   @Override
   public AnnotationAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new AnnotationDsl(this),
                             javadoc,
                             DslSupport::indent,
                             (annotationDsl, renderable) -> annotationDsl.javadoc = renderable);
   }

   @Override
   public AnnotationAnnotateStep annotate(String... annotation)
   {
      return addArray2(new AnnotationDsl(this),
                       annotation,
                       (annotationDsl, string) -> annotationDsl.annotations.add(renderingContext -> indent(renderingContext,
                                                                                                           '@' +
                                                                                                           renderingContext.renderName(string))));
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
                     body, (annotationDsl, s) -> annotationDsl.body = context -> indent(context, s));
   }

   @Override
   public AnnotationBodyStep field(String... fields)
   {
      return addArray2(new AnnotationDsl(this),
                       fields,
                       (annotationDsl, s) -> annotationDsl.fields.add(renderingContext -> indent(renderingContext, s)));
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
      return addArray2(new AnnotationDsl(this),
                       methods,
                       (annotationDsl, string) -> annotationDsl.methods.add(renderingContext -> indent(renderingContext, string)));
   }

   @Override
   public AnnotationBodyStep method(List<? extends MethodRenderable> methods)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              methods,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext) + ';',
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
      return addArray2(new AnnotationDsl(this),
                       inner,
                       (annotationDsl, string) -> annotationDsl.inner.add(renderingContext -> indent(renderingContext, string)));
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
   public AnnotationModifierStep modifier(Set<Modifier> modifiers)
   {
      return addArray2(new AnnotationDsl(this),
                       modifiers,
                       (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
   }

   @Override
   public AnnotationModifierStep public_()
   {
      return setType(new AnnotationDsl(this),
                     Modifier.PUBLIC,
                     (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
   }

   @Override
   public AnnotationModifierStep protected_()
   {
      return setType(new AnnotationDsl(this),
                     Modifier.PROTECTED,
                     (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
   }

   @Override
   public AnnotationModifierStep private_()
   {
      return setType(new AnnotationDsl(this),
                     Modifier.PRIVATE,
                     (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
   }

   @Override
   public AnnotationModifierStep abstract_()
   {
      return setType(new AnnotationDsl(this),
                     Modifier.ABSTRACT,
                     (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
   }

   @Override
   public AnnotationModifierStep sealed()
   {
      return setType(new AnnotationDsl(this),
                     Modifier.SEALED,
                     (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
   }

   @Override
   public AnnotationModifierStep nonSealed()
   {
      return setType(new AnnotationDsl(this),
                     Modifier.NON_SEALED,
                     (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
   }

   @Override
   public AnnotationModifierStep strictfp_()
   {
      return setType(new AnnotationDsl(this),
                     Modifier.STRICTFP,
                     (annotationDsl, modifier) -> annotationDsl.modifiers.add(modifier::render));
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
                     (annotationDsl, string) -> annotationDsl.package_ = new PackageRenderable()
                     {
                        @Override
                        public String renderQualifiedName(RenderingContext renderingContext)
                        {
                           return string;
                        }

                        @Override
                        public String renderPackageInfo(RenderingContext renderingContext)
                        {
                           throw new IllegalStateException();
                        }
                     });
   }

   @Override
   public AnnotationImportStep package_(PackageRenderable aPackage)
   {
      return setType(new AnnotationDsl(this),
                     aPackage,
                     (annotationDsl, packageRenderable) -> annotationDsl.package_ = packageRenderable);
   }

   @Override
   public AnnotationImportStep noPackage()
   {
      return new AnnotationDsl(this);
   }

   @Override
   public AnnotationImportStep import_(String... name)
   {
      return addArray2(new AnnotationDsl(this),
                       name,
                       (annotationDsl, string) -> annotationDsl.imports.add(renderingContext -> Dsl.import_(string)
                                                                                                   .renderDeclaration(renderingContext)));
   }

   @Override
   public AnnotationImportStep import_(List<? extends ImportRenderable> declared)
   {
      return addArrayRenderer(new AnnotationDsl(this),
                              declared,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              annotationDsl -> annotationDsl.imports::add);
   }

   @Override
   public String renderDeclaration(RenderingContext context)
   {
      context.addSurrounding(this);
      if (package_ != null)
      {
         context.setCurrentPackageName(package_.renderQualifiedName(context));
      }

      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.render(context))
           .append("\n");
      }

      renderElement(sb, annotations, context, "\n", new Padding(null, context.getLineIndentation(), null, "\n"));
      sb.append(context.getLineIndentation());
      renderElement(sb, modifiers, context, " ", new Padding(null, null, null, " "));

      sb.append("@interface ")
        .append(name)
        .append(' ');

      sb.append("{\n");
      RenderingContext indented = createRenderingContext(context);
      indented.incrementIndentationLevel();
      if (body != null)
      {
         sb.append(body.render(indented))
           .append('\n');
      }
      else
      {
         renderElement(sb, fields, indented, "\n", new Padding(null, null, null, "\n"));
         renderElement(sb, methods, indented, "\n", new Padding(null, null, null, "\n\n"));
         renderElement(sb, inner, indented, "\n", new Padding(null, null, null, "\n\n"));
      }
      sb.append(context.getLineIndentation())
        .append('}');
      
      //render Header

      if (!imports.isEmpty() || !context.getImports().isEmpty())
      {
         sb.insert(0, "\n\n");
      }
      sb.insert(0, imports.stream().map(renderable -> renderable.render(context)).collect(joining("\n")));
      sb.insert(0, context.getImports().stream().map(renderable -> renderable.renderDeclaration(context)).collect(joining("\n")));

      if (package_ != null)
      {
         sb.insert(0, "\n\n")
           .insert(0, package_.renderDeclaration(context));
      }

      if (copyright != null)
      {
         sb.insert(0, '\n')
           .insert(0, copyright);
      }

      return sb.toString();
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      renderingContext.addSurrounding(this);

      StringBuilder sb = new StringBuilder();
      if (package_ != null)
      {
         sb.append(package_.renderQualifiedName(renderingContext))
           .append('.');
      }
      if (outerType != null)
      {
         sb.append(outerType.render(renderingContext))
           .append('.');
      }
      sb.append(name);

      return sb.toString();
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return renderQualifiedName(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      String name = this.name;

      if (outerType != null)
      {
         name = outerType.render(renderingContext) + '.' + name;
      }
      if (package_ == null)
      {
         return renderingContext.renderName(name);
      }
      return renderingContext.renderName(package_.renderQualifiedName(renderingContext), name);
   }
}
