package io.determann.shadow.internal.annotation_processing.dsl;


import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.annotation_processing.dsl.field.FieldRenderable;
import io.determann.shadow.api.annotation_processing.dsl.import_.ImportRenderable;
import io.determann.shadow.api.annotation_processing.dsl.import_.ImportStaticStep;
import io.determann.shadow.api.annotation_processing.dsl.import_.StaticImportTypeStep;
import io.determann.shadow.api.annotation_processing.dsl.method.MethodRenderable;
import io.determann.shadow.api.annotation_processing.dsl.package_.PackageRenderable;

import static io.determann.shadow.internal.annotation_processing.dsl.DslSupport.setType;

public class ImportDsl
      implements ImportStaticStep,
                 ImportRenderable
{
   private static final String ALL_SUFFIX = ".*";

   private Renderable import_;

   public ImportDsl() {}

   private ImportDsl(ImportDsl other)
   {
      this.import_ = other.import_;
   }

   @Override
   public StaticImportTypeStep static_()
   {
      return new StaticImportDsl();
   }

   @Override
   public ImportRenderable declared(String name)
   {
      return setType(new ImportDsl(this),
                     name,
                     (importDsl, string) -> importDsl.import_ = renderingContext -> string);
   }

   @Override
   public ImportRenderable declared(DeclaredRenderable declared)
   {
      return setType(new ImportDsl(this),
                     declared,
                     (importDsl, declaredRenderable) ->
                           importDsl.import_ = declaredRenderable::renderQualifiedName);
   }

   @Override
   public ImportRenderable package_(String cPackage)
   {
      return setType(new ImportDsl(this),
                     cPackage,
                     (importDsl, string) -> importDsl.import_ = renderingContext -> string + ALL_SUFFIX);
   }

   @Override
   public ImportRenderable package_(PackageRenderable cPackage)
   {
      return setType(new ImportDsl(this),
                     cPackage,
                     (importDsl, packageRenderable) ->
                           importDsl.import_ = renderingContext -> packageRenderable.renderQualifiedName(renderingContext) + ALL_SUFFIX);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return "import " + import_.render(renderingContext) + ';';
   }

   private static class StaticImportDsl
         implements StaticImportTypeStep,
                    ImportRenderable
   {
      private Renderable import_;

      private StaticImportDsl() {}

      private StaticImportDsl(StaticImportDsl other)
      {
         this.import_ = other.import_;
      }

      @Override
      public ImportRenderable declared(String name)
      {
         return setType(new StaticImportDsl(this),
                        name,
                        (importDsl, string) -> importDsl.import_ = renderingContext -> string + ALL_SUFFIX);
      }

      @Override
      public ImportRenderable declared(DeclaredRenderable declared)
      {
         return setType(new StaticImportDsl(this),
                        declared,
                        (importDsl, declaredRenderable) ->
                              importDsl.import_ = renderingContext -> declaredRenderable.renderQualifiedName(renderingContext) + ALL_SUFFIX);
      }

      @Override
      public ImportRenderable method(String declared, String method)
      {
         return setType(new StaticImportDsl(this),
                        declared + '.' + method,
                        (importDsl, methodImport) -> importDsl.import_ = renderingContext -> methodImport);
      }

      @Override
      public ImportRenderable method(DeclaredRenderable declared, MethodRenderable method)
      {
         record Pair(DeclaredRenderable declared,
                     MethodRenderable method) {}

         return setType(new StaticImportDsl(this),
                        new Pair(declared, method),
                        (importDsl, pair) ->
                              importDsl.import_ = renderingContext ->
                                    pair.declared().renderQualifiedName(renderingContext) + '.' + pair.method().renderName(renderingContext));
      }

      @Override
      public ImportRenderable field(String declared, String field)
      {
         return setType(new StaticImportDsl(this),
                        declared + '.' + field,
                        (importDsl, methodImport) -> importDsl.import_ = renderingContext -> methodImport);
      }

      @Override
      public ImportRenderable field(DeclaredRenderable declared, FieldRenderable field)
      {
         record Pair(DeclaredRenderable declared,
                     FieldRenderable field) {}

         return setType(new StaticImportDsl(this),
                        new Pair(declared, field),
                        (importDsl, pair) ->
                              importDsl.import_ = renderingContext ->
                                    pair.declared().renderQualifiedName(renderingContext) + '.' + pair.field().renderName(renderingContext));
      }

      @Override
      public String renderDeclaration(RenderingContext renderingContext)
      {
         return "import static " + import_.render(renderingContext) + ';';
      }
   }
}
