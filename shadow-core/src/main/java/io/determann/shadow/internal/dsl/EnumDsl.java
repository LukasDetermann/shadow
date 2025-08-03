package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.enum_.*;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.renderer.RenderingContext.renderingContextBuilder;
import static io.determann.shadow.internal.dsl.DslSupport.*;

public class EnumDsl
      implements EnumCopyrightHeaderStep,
                 EnumImportStep,
                 EnumImplementsStep
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
   private final List<Renderable> implements_ = new ArrayList<>();
   private final List<Renderable> enumConstants = new ArrayList<>();
   private final List<Renderable> fields = new ArrayList<>();
   private final List<Renderable> methods = new ArrayList<>();
   private final List<Renderable> inner = new ArrayList<>();
   private final List<Renderable> instanceInitializers = new ArrayList<>();
   private final List<Renderable> staticInitializers = new ArrayList<>();
   private final List<Renderable> constructors = new ArrayList<>();
   private String body;

   public EnumDsl()
   {
   }

   private EnumDsl(EnumDsl other)
   {
      this.copyright = other.copyright;
      this.package_ = other.package_;
      this.imports.addAll(other.imports);

      this.javadoc = other.javadoc;
      this.annotations.addAll(other.annotations);
      this.modifiers.addAll(other.modifiers);
      this.name = other.name;
      this.implements_.addAll(other.implements_);
      this.enumConstants.addAll(other.enumConstants);
      this.fields.addAll(other.fields);
      this.methods.addAll(other.methods);
      this.inner.addAll(other.inner);
      this.instanceInitializers.addAll(other.instanceInitializers);
      this.staticInitializers.addAll(other.staticInitializers);
      this.constructors.addAll(other.constructors);
      this.body = other.body;
   }

   @Override
   public EnumAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new EnumDsl(this),
                             javadoc,
                             (enumDsl, function) -> enumDsl.javadoc = function);
   }

   @Override
   public EnumAnnotateStep annotate(String... annotation)
   {
      return addArray2(new EnumDsl(this), annotation, (enumDsl, string) -> enumDsl.annotations.add(renderingContext -> '@' + string));
   }

   @Override
   public EnumAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new EnumDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumDsl -> enumDsl.annotations::add);
   }

   @Override
   public EnumModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new EnumDsl(this), modifiers, enumDsl -> enumDsl.modifiers::add);
   }

   @Override
   public EnumModifierStep modifier(Set<Modifier> modifiers)
   {
      return addArray2(new EnumDsl(this),
                       modifiers,
                       (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public EnumModifierStep public_()
   {
      return setType(new EnumDsl(this),
                     Modifier.PUBLIC,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public EnumModifierStep protected_()
   {
      return setType(new EnumDsl(this),
                     Modifier.PROTECTED,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public EnumModifierStep private_()
   {
      return setType(new EnumDsl(this),
                     Modifier.PRIVATE,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public EnumModifierStep static_()
   {
      return setType(new EnumDsl(this),
                     Modifier.STATIC,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public EnumModifierStep strictfp_()
   {
      return setType(new EnumDsl(this),
                     Modifier.STRICTFP,
                     (enumDsl, modifier) -> enumDsl.modifiers.add(modifier::render));
   }

   @Override
   public EnumImplementsStep name(String name)
   {
      return setType(new EnumDsl(this), name, (enumDsl, s) -> enumDsl.name = s);
   }

   @Override
   public EnumImplementsStep implements_(String... interfaces)
   {
      return addArray2(new EnumDsl(this), interfaces, (enumDsl, string) -> enumDsl.implements_.add(renderingContext -> string));
   }

   @Override
   public EnumImplementsStep implements_(List<? extends InterfaceRenderable> interfaces)
   {
      return addArrayRenderer(new EnumDsl(this),
                              interfaces,
                              (renderingContext, renderable) -> renderable.renderType(renderingContext),
                              enumDsl -> enumDsl.implements_::add);
   }

   @Override
   public EnumRenderable body(String body)
   {
      return setType(new EnumDsl(this),
                     body, (enumDsl, function) -> enumDsl.body = function);
   }

   @Override
   public EnumBodyStep field(String... fields)
   {
      return addArray2(new EnumDsl(this), fields, (enumDsl, string) -> enumDsl.fields.add(renderingContext -> string));
   }

   @Override
   public EnumBodyStep field(List<? extends FieldRenderable> fields)
   {
      return addArrayRenderer(new EnumDsl(this),
                              fields,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumDsl -> enumDsl.fields::add);
   }

   @Override
   public EnumBodyStep method(String... methods)
   {
      return addArray2(new EnumDsl(this), methods, (enumDsl, string) -> enumDsl.methods.add(renderingContext -> string));
   }

   @Override
   public EnumBodyStep method(List<? extends MethodRenderable> methods)
   {
      return addArrayRenderer(new EnumDsl(this),
                              methods,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumDsl -> enumDsl.methods::add);
   }

   @Override
   public EnumBodyStep inner(String... inner)
   {
      return addArray2(new EnumDsl(this), inner, (enumDsl, string) -> enumDsl.inner.add(renderingContext -> string));
   }

   @Override
   public EnumBodyStep inner(List<? extends DeclaredRenderable> inner)
   {
      return addArrayRenderer(new EnumDsl(this),
                              inner,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumDsl -> enumDsl.inner::add);
   }

   @Override
   public EnumBodyStep instanceInitializer(String... instanceInitializers)
   {
      return addArrayRenderer(new EnumDsl(this), instanceInitializers, enumDsl -> enumDsl.instanceInitializers::add);
   }

   @Override
   public EnumBodyStep staticInitializer(String... staticInitializer)
   {
      return addArrayRenderer(new EnumDsl(this), staticInitializer, enumDsl -> enumDsl.staticInitializers::add);
   }

   @Override
   public EnumBodyStep constructor(String... constructors)
   {
      return addArray2(new EnumDsl(this), constructors, (enumDsl, string) -> enumDsl.constructors.add(renderingContext -> string));
   }

   @Override
   public EnumBodyStep constructor(List<? extends ConstructorRenderable> constructors)
   {
      return addArrayRenderer(new EnumDsl(this),
                              constructors,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumDsl -> enumDsl.constructors::add);
   }

   @Override
   public EnumPackageStep copyright(String copyrightHeader)
   {
      return setType(new EnumDsl(this),
                     copyrightHeader,
                     (enumDsl, string) -> enumDsl.copyright = string);
   }

   @Override
   public EnumImportStep package_(String packageName)
   {
      return setType(new EnumDsl(this),
                     packageName,
                     (enumDsl, string) -> enumDsl.package_ = renderingContext -> string);
   }

   @Override
   public EnumImportStep package_(PackageRenderable aPackage)
   {
      return setType(new EnumDsl(this),
                     aPackage,
                     (enumDsl, string) -> enumDsl.package_ = string);
   }

   @Override
   public EnumImportStep import_(String... name)
   {
      return addArray2(new EnumDsl(this),
                       name,
                       (enumDsl, string) -> enumDsl.imports.add(renderingContext -> "import " + string));
   }

   @Override
   public EnumImportStep import_(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new EnumDsl(this),
                              declared,
                              (renderingContext, renderable) -> "import " + renderable.renderQualifiedName(renderingContext),
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep importPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new EnumDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> "import " +
                                                                       packageRenderable.renderQualifiedName(renderingContext) +
                                                                       ".*",
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep staticImport(String... name)
   {
      return addArrayRenderer(new EnumDsl(this),
                              name,
                              (renderingContext, string) -> "import static " + string,
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep staticImport(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new EnumDsl(this),
                              declared,
                              (renderingContext, renderable) -> "import static " + renderable.renderQualifiedName(renderingContext),
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep staticImportPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new EnumDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> "import static " +
                                                                       packageRenderable.renderQualifiedName(renderingContext) +
                                                                       ".*",
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumEnumConstantStep enumConstant(String... enumConstant)
   {
      return addArray2(new EnumDsl(this),
                       enumConstant,
                       (enumDsl, string) -> enumDsl.enumConstants.add(renderingContext -> string));
   }

   @Override
   public EnumEnumConstantStep enumConstant(List<? extends C.EnumConstant> enumConstant)
   {
      return addArrayRenderer(new EnumDsl(this),
                              enumConstant,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumDsl -> enumDsl.imports::add);
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

      renderElement(sb, imports, ";", context, "\n");
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

      sb.append("enum ");
      sb.append(name);
      sb.append(' ');

      renderElement(sb, "implements ", implements_, " ", context, ", ");

      sb.append("{\n");
      if (body != null)
      {
         sb.append(body)
           .append('\n');
      }
      else
      {
         renderElement(sb, enumConstants, "\n", context, "\n");
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
      return renderQualifiedName(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return name;
   }
}
