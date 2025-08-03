package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.dsl.interface_.*;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.renderer.RenderingContext.renderingContextBuilder;
import static io.determann.shadow.internal.dsl.DslSupport.*;

public class InterfaceDsl
      implements InterfaceCopyrightHeaderStep,
                 InterfaceImportStep,
                 InterfaceGenericStep
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
   private List<Renderable> extends_ = new ArrayList<>();
   private final List<Renderable> permits = new ArrayList<>();
   private final List<Renderable> fields = new ArrayList<>();
   private final List<Renderable> methods = new ArrayList<>();
   private final List<Renderable> inner = new ArrayList<>();
   private String body;

   public InterfaceDsl()
   {
   }

   private InterfaceDsl(InterfaceDsl other)
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
      this.permits.addAll(other.permits);
      this.fields.addAll(other.fields);
      this.methods.addAll(other.methods);
      this.inner.addAll(other.inner);
      this.body = other.body;
   }

   @Override
   public InterfaceAnnotateStep javadoc(String javadoc)
   {
      return setTypeRenderer(new InterfaceDsl(this),
                             javadoc,
                             (interfaceDsl, function) -> interfaceDsl.javadoc = function);
   }

   @Override
   public InterfaceAnnotateStep annotate(String... annotation)
   {
      return addArray2(new InterfaceDsl(this),
                       annotation,
                       (interfaceDsl, string) -> interfaceDsl.annotations.add(renderingContext -> '@' + string));
   }

   @Override
   public InterfaceAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              interfaceDsl -> interfaceDsl.annotations::add);
   }

   @Override
   public InterfaceModifierStep modifier(String... modifiers)
   {
      return addArrayRenderer(new InterfaceDsl(this), modifiers, interfaceDsl -> interfaceDsl.modifiers::add);
   }

   @Override
   public InterfaceModifierStep modifier(Set<Modifier> modifiers)
   {
      return addArray2(new InterfaceDsl(this),
                       modifiers,
                       (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep abstract_()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.ABSTRACT,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep public_()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.PUBLIC,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep protected_()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.PROTECTED,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep private_()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.PRIVATE,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep sealed()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.SEALED,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep nonSealed()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.NON_SEALED,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep static_()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.STATIC,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceModifierStep strictfp_()
   {
      return setType(new InterfaceDsl(this),
                     Modifier.STRICTFP,
                     (interfaceDsl, modifier) -> interfaceDsl.modifiers.add(modifier::render));
   }

   @Override
   public InterfaceGenericStep name(String name)
   {
      return setType(new InterfaceDsl(this), name, (interfaceDsl, s) -> interfaceDsl.name = s);
   }

   @Override
   public InterfaceGenericStep generic(String... generic)
   {
      return addArray2(new InterfaceDsl(this), generic, (interfaceDsl, string) -> interfaceDsl.generics.add(renderingContext -> string));
   }

   @Override
   public InterfaceGenericStep generic(List<? extends GenericRenderable> generics)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              generics,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              interfaceDsl -> interfaceDsl.generics::add);
   }

   @Override
   public InterfacePermitsStep permits(String... declared)
   {
      return addArray2(new InterfaceDsl(this), declared, (interfaceDsl, string) -> interfaceDsl.permits.add(renderingContext -> string));
   }

   @Override
   public InterfacePermitsStep permits(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              declared,
                              (renderingContext, renderable) -> renderable.renderQualifiedName(renderingContext),
                              interfaceDsl -> interfaceDsl.permits::add);
   }

   @Override
   public InterfaceRenderable body(String body)
   {
      return setType(new InterfaceDsl(this),
                     body, (interfaceDsl, function) -> interfaceDsl.body = function);
   }

   @Override
   public InterfaceBodyStep field(String... fields)
   {
      return addArray2(new InterfaceDsl(this), fields, (interfaceDsl, string) -> interfaceDsl.fields.add(renderingContext -> string));
   }

   @Override
   public InterfaceBodyStep field(List<? extends FieldRenderable> fields)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              fields,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              interfaceDsl -> interfaceDsl.fields::add);
   }

   @Override
   public InterfaceBodyStep method(String... methods)
   {
      return addArray2(new InterfaceDsl(this), methods, (interfaceDsl, string) -> interfaceDsl.methods.add(renderingContext -> string));
   }

   @Override
   public InterfaceBodyStep method(List<? extends MethodRenderable> methods)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              methods,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              interfaceDsl -> interfaceDsl.methods::add);
   }

   @Override
   public InterfaceBodyStep inner(String... inner)
   {
      return addArray2(new InterfaceDsl(this), inner, (interfaceDsl, string) -> interfaceDsl.inner.add(renderingContext -> string));
   }

   @Override
   public InterfaceBodyStep inner(List<? extends DeclaredRenderable> inner)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              inner,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              interfaceDsl -> interfaceDsl.inner::add);
   }

   @Override
   public InterfacePackageStep copyright(String copyrightHeader)
   {
      return setType(new InterfaceDsl(this),
                     copyrightHeader,
                     (interfaceDsl, string) -> interfaceDsl.copyright = string);
   }

   @Override
   public InterfaceImportStep package_(String packageName)
   {
      return setType(new InterfaceDsl(this),
                     packageName,
                     (interfaceDsl, string) -> interfaceDsl.package_ = renderingContext -> string);
   }

   @Override
   public InterfaceImportStep package_(PackageRenderable aPackage)
   {
      return setType(new InterfaceDsl(this),
                     aPackage,
                     (interfaceDsl, string) -> interfaceDsl.package_ = string);
   }

   @Override
   public InterfaceImportStep import_(String... name)
   {
      return addArray2(new InterfaceDsl(this),
                       name,
                       (interfaceDsl, string) -> interfaceDsl.imports.add(renderingContext -> "import " + string));
   }

   @Override
   public InterfaceImportStep import_(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              declared,
                              (renderingContext, renderable) -> "import " + renderable.renderQualifiedName(renderingContext),
                              interfaceDsl -> interfaceDsl.imports::add);
   }

   @Override
   public InterfaceImportStep importPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> "import " +
                                                                       packageRenderable.renderQualifiedName(renderingContext) +
                                                                       ".*",
                              interfaceDsl -> interfaceDsl.imports::add);
   }

   @Override
   public InterfaceImportStep staticImport(String... name)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              name,
                              (renderingContext, string) -> "import static " + string,
                              interfaceDsl -> interfaceDsl.imports::add);
   }

   @Override
   public InterfaceImportStep staticImport(List<? extends DeclaredRenderable> declared)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              declared,
                              (renderingContext, renderable) -> "import static " + renderable.renderQualifiedName(renderingContext),
                              interfaceDsl -> interfaceDsl.imports::add);
   }

   @Override
   public InterfaceImportStep staticImportPackage(List<? extends PackageRenderable> cPackages)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              cPackages,
                              (renderingContext, packageRenderable) -> "import static " +
                                                                       packageRenderable.renderQualifiedName(renderingContext) +
                                                                       ".*",
                              interfaceDsl -> interfaceDsl.imports::add);
   }

   @Override
   public InterfaceExtendsStep extends_(String... interfaces)
   {
      return addArray2(new InterfaceDsl(this),
                       interfaces,
                       (interfaceDsl, string) -> interfaceDsl.extends_.add(renderingContext -> string));
   }

   @Override
   public InterfaceExtendsStep extends_(List<? extends InterfaceRenderable> interfaces)
   {
      return addArrayRenderer(new InterfaceDsl(this),
                              interfaces,
                              (renderingContext, interfaceRenderable) -> interfaceRenderable.renderQualifiedName(renderingContext),
                              interfaceDsl -> interfaceDsl.extends_::add);
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

      sb.append("interface ");
      sb.append(name);
      sb.append(' ');

      renderElement(sb, "<", generics, "> ", context, ", ");

      renderElement(sb, "extends ", extends_, " ", context, ", ");
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
