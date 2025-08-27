package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.dsl.record.*;
import io.determann.shadow.api.dsl.record_component.RecordComponentRenderable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.dsl.RenderingContext.renderingContextBuilder;
import static io.determann.shadow.internal.dsl.DslSupport.*;
import static java.util.stream.Collectors.joining;

public class RecordDsl
      implements RecordCopyrightHeaderStep,
                 RecordImportStep,
                 RecordRecordComponentStep
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
   private final List<Renderable> components = new ArrayList<>();
   private final List<Renderable> generics = new ArrayList<>();
   private final List<Renderable> implements_ = new ArrayList<>();
   private final List<Renderable> fields = new ArrayList<>();
   private final List<Renderable> methods = new ArrayList<>();
   private final List<Renderable> inner = new ArrayList<>();
   private final List<Renderable> staticInitializers = new ArrayList<>();
   private final List<Renderable> constructors = new ArrayList<>();
   private Renderable body;

   public RecordDsl()
   {
   }

   private RecordDsl(RecordDsl other)
   {
      this.copyright = other.copyright;
      this.package_ = other.package_;
      this.imports.addAll(other.imports);

      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.modifiers.addAll(other.modifiers);
      this.name = other.name;
      this.components.addAll(other.components);
      this.generics.addAll(other.generics);
      this.implements_.addAll(other.implements_);
      this.fields.addAll(other.fields);
      this.methods.addAll(other.methods);
      this.inner.addAll(other.inner);
      this.staticInitializers.addAll(other.staticInitializers);
      this.constructors.addAll(other.constructors);
      this.body = other.body;
   }

   @Override
   public RecordAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new RecordDsl(this),
                             javadoc,
                             DslSupport::indent,
                             (recordDsl, function) -> recordDsl.javadoc = function);
   }

   @Override
   public RecordAnnotateStep annotate(String... annotation)
   {
      return addArray2(new RecordDsl(this),
                       annotation,
                       (recordDsl, string) -> recordDsl.annotations.add(context -> indent(context, '@' + string)));
   }

   @Override
   public RecordAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new RecordDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordDsl -> recordDsl.annotations::add);
   }

   @Override
   public RecordModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new RecordDsl(this), modifiers, recordDsl -> recordDsl.modifiers::add);
   }

   @Override
   public RecordModifierStep modifier(Set<Modifier> modifiers)
   {
      return addArray2(new RecordDsl(this),
                       modifiers,
                       (recordDsl, modifier) -> recordDsl.modifiers.add(modifier::render));
   }

   @Override
   public RecordModifierStep public_()
   {
      return setType(new RecordDsl(this),
                     Modifier.PUBLIC,
                     (recordDsl, modifier) -> recordDsl.modifiers.add(modifier::render));
   }

   @Override
   public RecordModifierStep protected_()
   {
      return setType(new RecordDsl(this),
                     Modifier.PROTECTED,
                     (recordDsl, modifier) -> recordDsl.modifiers.add(modifier::render));
   }

   @Override
   public RecordModifierStep private_()
   {
      return setType(new RecordDsl(this),
                     Modifier.PRIVATE,
                     (recordDsl, modifier) -> recordDsl.modifiers.add(modifier::render));
   }

   @Override
   public RecordModifierStep final_()
   {
      return setType(new RecordDsl(this),
                     Modifier.FINAL,
                     (recordDsl, modifier) -> recordDsl.modifiers.add(modifier::render));
   }

   @Override
   public RecordModifierStep static_()
   {
      return setType(new RecordDsl(this),
                     Modifier.STATIC,
                     (recordDsl, modifier) -> recordDsl.modifiers.add(modifier::render));
   }

   @Override
   public RecordModifierStep strictfp_()
   {
      return setType(new RecordDsl(this),
                     Modifier.STRICTFP,
                     (recordDsl, modifier) -> recordDsl.modifiers.add(modifier::render));
   }

   @Override
   public RecordRecordComponentStep name(String name)
   {
      return setType(new RecordDsl(this), name, (recordDsl, s) -> recordDsl.name = s);
   }

   @Override
   public RecordRecordComponentStep component(String... recordComponent)
   {
      return addArray2(new RecordDsl(this), recordComponent, (recordDsl, string) -> recordDsl.components.add(renderingContext -> string));
   }

   @Override
   public RecordRecordComponentStep component(List<? extends RecordComponentRenderable> recordComponent)
   {
      return addArrayRenderer(new RecordDsl(this),
                              recordComponent,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordDsl -> recordDsl.components::add);
   }

   @Override
   public RecordGenericStep generic(String... generic)
   {
      return addArray2(new RecordDsl(this), generic, (recordDsl, string) -> recordDsl.generics.add(renderingContext -> string));
   }

   @Override
   public RecordGenericStep generic(List<? extends GenericRenderable> generics)
   {
      return addArrayRenderer(new RecordDsl(this),
                              generics,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordDsl -> recordDsl.generics::add);
   }

   @Override
   public RecordImplementsStep implements_(String... interfaces)
   {
      return addArray2(new RecordDsl(this), interfaces, (recordDsl, string) -> recordDsl.implements_.add(renderingContext -> string));
   }

   @Override
   public RecordImplementsStep implements_(List<? extends InterfaceRenderable> interfaces)
   {
      return addArrayRenderer(new RecordDsl(this),
                              interfaces,
                              (renderingContext, renderable) -> renderable.renderType(renderingContext),
                              recordDsl -> recordDsl.implements_::add);
   }

   @Override
   public ClassRenderable body(String body)
   {
      return setType(new RecordDsl(this),
                     body, (recordDsl, function) -> recordDsl.body = context -> indent(context, function));
   }

   @Override
   public RecordBodyStep field(String... fields)
   {
      return addArray2(new RecordDsl(this), fields, (recordDsl, string) -> recordDsl.fields.add(context -> indent(context, string)));
   }

   @Override
   public RecordBodyStep field(List<? extends FieldRenderable> fields)
   {
      return addArrayRenderer(new RecordDsl(this),
                              fields,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordDsl -> recordDsl.fields::add);
   }

   @Override
   public RecordBodyStep method(String... methods)
   {
      return addArray2(new RecordDsl(this), methods, (recordDsl, string) -> recordDsl.methods.add(context -> indent(context, string)));
   }

   @Override
   public RecordBodyStep method(List<? extends MethodRenderable> methods)
   {
      return addArrayRenderer(new RecordDsl(this),
                              methods,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordDsl -> recordDsl.methods::add);
   }

   @Override
   public RecordBodyStep inner(String... inner)
   {
      return addArray2(new RecordDsl(this), inner, (recordDsl, string) -> recordDsl.inner.add(context -> indent(context, string)));
   }

   @Override
   public RecordBodyStep inner(List<? extends DeclaredRenderable> inner)
   {
      return addArrayRenderer(new RecordDsl(this),
                              inner,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordDsl -> recordDsl.inner::add);
   }

   @Override
   public RecordBodyStep staticInitializer(String... staticInitializer)
   {
      return addArrayRenderer(new RecordDsl(this), staticInitializer, DslSupport::indent, recordDsl -> recordDsl.staticInitializers::add);
   }

   @Override
   public RecordBodyStep constructor(String... constructors)
   {
      return addArray2(new RecordDsl(this),
                       constructors,
                       (recordDsl, string) -> recordDsl.constructors.add(context -> indent(context, string)));
   }

   @Override
   public RecordBodyStep constructor(List<? extends ConstructorRenderable> constructors)
   {
      return addArrayRenderer(new RecordDsl(this),
                              constructors,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              recordDsl -> recordDsl.constructors::add);
   }

   @Override
   public RecordPackageStep copyright(String copyrightHeader)
   {
      return setType(new RecordDsl(this),
                     copyrightHeader,
                     (recordDsl, string) -> recordDsl.copyright = string);
   }

   @Override
   public RecordImportStep package_(String packageName)
   {
      return setType(new RecordDsl(this),
                     packageName,
                     (recordDsl, string) -> recordDsl.package_ = renderingContext -> string);
   }

   @Override
   public RecordImportStep package_(PackageRenderable aPackage)
   {
      return setType(new RecordDsl(this),
                     aPackage,
                     (recordDsl, string) -> recordDsl.package_ = string);
   }

   @Override
   public RecordImportStep import_(String... name)
   {
      return addArray2(new RecordDsl(this),
                       name,
                       (recordDsl, string) -> recordDsl.imports.add(renderingContext -> Dsl.import_(string)
                                                                                           .renderDeclaration(renderingContext)));
   }

   @Override
   public RecordImportStep import_(List<? extends ImportRenderable> declared)
   {
      return addArray2(new RecordDsl(this),
                       declared,
                       (recordDsl, importRenderable) -> recordDsl.imports.add(importRenderable::renderDeclaration));
   }

   @Override
   public String renderDeclaration(RenderingContext context)
   {
      context = renderingContextBuilder(context)
            .withSurrounding(this)
            .build();

      if (isOuterType())
      {
         context = context.builder().withNewImportContext().build();
      }

      StringBuilder sb = new StringBuilder();
      if (javadoc != null)
      {
         sb.append(javadoc.render(context));
         sb.append("\n");
      }

      renderElement(sb, annotations, context, "\n", new Padding(null, context.getLineIndentation(), null, "\n"));
      sb.append(context.getLineIndentation());
      renderElement(sb, modifiers, context, " ", new Padding(null, null, null, " "));

      sb.append("record ")
        .append(name)
        .append('(');
      renderElement(sb, components, context, ", ");
      sb.append(')')
        .append(' ');

      renderElement(sb, "<", generics, "> ", context, ", ");

      renderElement(sb, "implements ", implements_, " ", context, ", ");

      sb.append("{\n");
      RenderingContext indented = context.builder().incrementIndentationLevel().build();
      if (body != null)
      {
         sb.append(body.render(indented))
           .append('\n');
      }
      else
      {
         renderElement(sb, fields, indented, "\n", new Padding(null, null, null, "\n"));
         renderElement(sb, staticInitializers, indented, "\n", new Padding(null, null, null, "\n\n"));
         renderElement(sb, constructors, indented, "\n", new Padding(null, null, null, "\n\n"));
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
      RenderingContext finalContext = context;
      sb.insert(0, imports.stream().map(renderable -> renderable.render(finalContext)).collect(joining("\n")));
      sb.insert(0, context.getImports().stream().map(renderable -> renderable.renderDeclaration(finalContext)).collect(joining("\n")));

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

   private boolean isOuterType()
   {
      return package_ != null || imports.isEmpty();
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      if (package_ == null)
      {
         return name;
      }
      return package_.renderQualifiedName(renderingContextBuilder(renderingContext)
                                                .withSurrounding(this)
                                                .build()) + '.' + name;
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      RenderingContext context = renderingContextBuilder(renderingContext)
            .withSurrounding(this)
            .build();

      String qualifiedName = renderQualifiedName(context);
      if (generics.isEmpty())
      {
         return qualifiedName;
      }
      return qualifiedName +
             '<' +
             generics.stream().map(renderable -> renderable.render(context)).collect(joining(", ")) +
             '>';
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      if (package_ == null)
      {
         return renderingContext.renderName(name);
      }
      return renderingContext.renderName(package_.renderQualifiedName(renderingContext), name);
   }
}
