package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.enum_.*;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.renderer.RenderingContext.renderingContextBuilder;
import static io.determann.shadow.api.renderer.RenderingContextOptions.RECEIVER_TYPE;
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
   public EnumModifierStep modifier(Set<C_Modifier> modifiers)
   {
      return addArray(new EnumDsl(this),
                      modifiers,
                      enumDsl -> enumDsl.modifiers::add);
   }

   @Override
   public EnumModifierStep public_()
   {
      return addTypeRenderer(new EnumDsl(this),
                             C_Modifier.PUBLIC,
                             enumDsl -> enumDsl.modifiers::add);
   }

   @Override
   public EnumModifierStep protected_()
   {
      return addTypeRenderer(new EnumDsl(this),
                             C_Modifier.PROTECTED,
                             enumDsl -> enumDsl.modifiers::add);
   }

   @Override
   public EnumModifierStep private_()
   {
      return addTypeRenderer(new EnumDsl(this),
                             C_Modifier.PRIVATE,
                             enumDsl -> enumDsl.modifiers::add);
   }

   @Override
   public EnumModifierStep static_()
   {
      return addTypeRenderer(new EnumDsl(this),
                             C_Modifier.STATIC,
                             enumDsl -> enumDsl.modifiers::add);
   }

   @Override
   public EnumModifierStep strictfp_()
   {
      return addTypeRenderer(new EnumDsl(this),
                             C_Modifier.STRICTFP,
                             enumDsl -> enumDsl.modifiers::add);
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
      return addArrayRenderer(new EnumDsl(this),
                              name,
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep import_(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new EnumDsl(this),
                              declared,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep importPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new EnumDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> packageRenderable.renderQualifiedName(renderingContext) + ".*",
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep staticImport(String... name)
   {
      return addArrayRenderer(new EnumDsl(this),
                              name,
                              (renderingContext, string) -> "static " + string,
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep staticImport(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new EnumDsl(this),
                              declared,
                              (renderingContext, renderable) -> "static " + renderable.renderQualifiedName(renderingContext),
                              enumDsl -> enumDsl.imports::add);
   }

   @Override
   public EnumImportStep staticImportPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new EnumDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> "static " +
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
   public EnumEnumConstantStep enumConstant(List<? extends C_EnumConstant> enumConstant)
   {
      return addArrayRenderer(new EnumDsl(this),
                              enumConstant,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              enumDsl -> enumDsl.imports::add);
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

      sb.append("enum ");
      sb.append(name);
      sb.append(' ');

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

         renderElement(sb, enumConstants, "\n", renderingContext, "\n");
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
