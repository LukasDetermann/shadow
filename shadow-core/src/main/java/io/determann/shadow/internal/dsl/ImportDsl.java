package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import io.determann.shadow.api.dsl.import_.ImportStaticStep;
import io.determann.shadow.api.dsl.import_.ImportTypeStep;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import static io.determann.shadow.internal.dsl.DslSupport.setType;

public class ImportDsl
      implements ImportStaticStep,
                 ImportRenderable
{
   private static final String ALL_SUFFIX = ".*";

   private boolean isStatic = false;
   private Renderable import_;

   public ImportDsl()
   {
   }

   private ImportDsl(ImportDsl other)
   {
      this.isStatic = other.isStatic;
      this.import_ = other.import_;
   }

   @Override
   public ImportTypeStep static_()
   {
      isStatic = true;
      return new ImportDsl(this);
   }

   @Override
   public ImportRenderable import_(String name)
   {
      return setType(new ImportDsl(this),
                     name,
                     (importDsl, string) -> importDsl.import_ = renderingContext -> string);
   }

   @Override
   public ImportRenderable import_(DeclaredRenderable declared)
   {
      return setType(new ImportDsl(this),
                     declared,
                     (importDsl, declaredRenderable) -> importDsl.import_ = declaredRenderable::renderQualifiedName);
   }

   @Override
   public ImportRenderable import_(MethodRenderable method)
   {
      return setType(new ImportDsl(this),
                     method,
                     (importDsl, methodRenderable) ->
                           importDsl.import_ = renderingContext -> renderPackage(renderingContext) +
                                                                   methodRenderable.renderName(renderingContext));
   }

   @Override
   public ImportRenderable import_(FieldRenderable field)
   {
      return setType(new ImportDsl(this),
                     field,
                     (importDsl, fieldRenderable) ->
                           importDsl.import_ = renderingContext -> renderPackage(renderingContext) +
                                                                   fieldRenderable.renderName(renderingContext));
   }

   private static String renderPackage(RenderingContext renderingContext)
   {
      return renderingContext.getSurrounding(PackageRenderable.class).renderQualifiedName(renderingContext);
   }

   @Override
   public ImportRenderable importAll(String cPackage)
   {
      return setType(new ImportDsl(this),
                     cPackage,
                     (importDsl, string) -> importDsl.import_ = renderingContext -> string + ALL_SUFFIX);
   }

   @Override
   public ImportRenderable importAll(PackageRenderable cPackage)
   {
      return setType(new ImportDsl(this),
                     cPackage,
                     (importDsl, packageRenderable) ->
                           importDsl.import_ = renderingContext -> packageRenderable.renderQualifiedName(renderingContext) + ".*");
   }

   @Override
   public ImportRenderable importAll(DeclaredRenderable declared)
   {
      return setType(new ImportDsl(this),
                     declared,
                     (importDsl, declaredRenderable) ->
                           importDsl.import_ = renderingContext -> declaredRenderable.renderQualifiedName(renderingContext) + ".*");
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return "import " + (isStatic ? "static " : "") + import_.render(renderingContext) + ';';
   }
}
