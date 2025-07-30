package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.dsl.record.*;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.renderer.RenderingContext.renderingContextBuilder;
import static io.determann.shadow.api.renderer.RenderingContextOptions.RECEIVER_TYPE;
import static io.determann.shadow.internal.dsl.DslSupport.*;

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
   private String body;

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
                             (recordDsl, function) -> recordDsl.javadoc = function);
   }

   @Override
   public RecordAnnotateStep annotate(String... annotation)
   {
      return addArray2(new RecordDsl(this), annotation, (recordDsl, string) -> recordDsl.annotations.add(renderingContext -> '@' + string));
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
   public RecordModifierStep modifier(Set<C_Modifier> modifiers)
   {
      return addArray(new RecordDsl(this),
                      modifiers,
                      recordDsl -> recordDsl.modifiers::add);
   }

   @Override
   public RecordModifierStep public_()
   {
      return addTypeRenderer(new RecordDsl(this),
                             C_Modifier.PUBLIC,
                             recordDsl -> recordDsl.modifiers::add);
   }

   @Override
   public RecordModifierStep protected_()
   {
      return addTypeRenderer(new RecordDsl(this),
                             C_Modifier.PROTECTED,
                             recordDsl -> recordDsl.modifiers::add);
   }

   @Override
   public RecordModifierStep private_()
   {
      return addTypeRenderer(new RecordDsl(this),
                             C_Modifier.PRIVATE,
                             recordDsl -> recordDsl.modifiers::add);
   }

   @Override
   public RecordModifierStep final_()
   {
      return addTypeRenderer(new RecordDsl(this),
                             C_Modifier.FINAL,
                             recordDsl -> recordDsl.modifiers::add);
   }

   @Override
   public RecordModifierStep static_()
   {
      return addTypeRenderer(new RecordDsl(this),
                             C_Modifier.STATIC,
                             recordDsl -> recordDsl.modifiers::add);
   }

   @Override
   public RecordModifierStep strictfp_()
   {
      return addTypeRenderer(new RecordDsl(this),
                             C_Modifier.STRICTFP,
                             recordDsl -> recordDsl.modifiers::add);
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
   public RecordRecordComponentStep component(List<? extends C_RecordComponent> recordComponent)
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
                     body, (recordDsl, function) -> recordDsl.body = function);
   }

   @Override
   public RecordBodyStep field(String... fields)
   {
      return addArray2(new RecordDsl(this), fields, (recordDsl, string) -> recordDsl.fields.add(renderingContext -> string));
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
      return addArray2(new RecordDsl(this), methods, (recordDsl, string) -> recordDsl.methods.add(renderingContext -> string));
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
      return addArray2(new RecordDsl(this), inner, (recordDsl, string) -> recordDsl.inner.add(renderingContext -> string));
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
      return addArrayRenderer(new RecordDsl(this), staticInitializer, recordDsl -> recordDsl.staticInitializers::add);
   }

   @Override
   public RecordBodyStep constructor(String... constructors)
   {
      return addArray2(new RecordDsl(this), constructors, (recordDsl, string) -> recordDsl.constructors.add(renderingContext -> string));
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
      return addArrayRenderer(new RecordDsl(this),
                              name,
                              recordDsl -> recordDsl.imports::add);
   }

   @Override
   public RecordImportStep import_(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new RecordDsl(this),
                              declared,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              recordDsl -> recordDsl.imports::add);
   }

   @Override
   public RecordImportStep importPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new RecordDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> packageRenderable.renderQualifiedName(renderingContext) + ".*",
                              recordDsl -> recordDsl.imports::add);
   }

   @Override
   public RecordImportStep staticImport(String... name)
   {
      return addArrayRenderer(new RecordDsl(this),
                              name,
                              (renderingContext, string) -> "static " + string,
                              recordDsl -> recordDsl.imports::add);
   }

   @Override
   public RecordImportStep staticImport(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new RecordDsl(this),
                              declared,
                              (renderingContext, renderable) -> "static " + renderable.renderQualifiedName(renderingContext),
                              recordDsl -> recordDsl.imports::add);
   }

   @Override
   public RecordImportStep staticImportPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new RecordDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> "static " +
                                                                       packageRenderable.renderQualifiedName(renderingContext) +
                                                                       ".*",
                              recordDsl -> recordDsl.imports::add);
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

      renderElement(sb, annotations, "\n", renderingContext, "\n");
      renderElement(sb, modifiers, " ", renderingContext, " ");

      sb.append("record ");
      sb.append(name);
      sb.append('(');
      renderElement(sb, components, renderingContext, ", ");
      sb.append(')');
      sb.append(' ');

      renderElement(sb, "<", generics, "> ", renderingContext, ", ");

      renderElement(sb, "implements ", implements_, " ", renderingContext, ", ");

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
         renderElement(sb, staticInitializers, "\n\n", renderingContext, "\n");
         renderElement(sb, constructors, "\n\n", renderingContext, "\n");
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
      String qualifiedName = renderQualifiedName(renderingContext);
      if (generics.isEmpty())
      {
         return qualifiedName;
      }
      return qualifiedName +
             '<' +
             generics.stream().map(renderable -> renderable.render(renderingContext)).collect(Collectors.joining(", ")) +
             '>';
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return name;
   }
}
