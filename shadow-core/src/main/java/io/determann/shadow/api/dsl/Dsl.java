package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.class_.ClassJavaDocStep;
import io.determann.shadow.api.dsl.constructor.ConstructorJavaDocStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.exports.ExportsPackageStep;
import io.determann.shadow.api.dsl.field.FieldJavaDocStep;
import io.determann.shadow.api.dsl.method.MethodJavaDocStep;
import io.determann.shadow.api.dsl.module.ModuleJavaDocStep;
import io.determann.shadow.api.dsl.opens.OpensPackageStep;
import io.determann.shadow.api.dsl.package_.PackageJavaDocStep;
import io.determann.shadow.api.dsl.provides.ProvidesServiceStep;
import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.uses.UsesServiceStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.internal.dsl.*;

public interface Dsl
{
   static ConstructorJavaDocStep constructor()
   {
      return new ConstructorDsl();
   }

   static MethodJavaDocStep method()
   {
      return new MethodDsl();
   }

   static ClassJavaDocStep class_()
   {
      return new ClassDsl();
   }

   static EnumConstantJavaDocStep enumConstant()
   {
      return new EnumConstantDsl();
   }

   static FieldJavaDocStep field()
   {
      return new FieldDsl();
   }

   static ModuleJavaDocStep moduleInfo()
   {
      return new ModuleDsl();
   }

   static ExportsPackageStep exports()
   {
      return new ExportsDsl();
   }

   static OpensPackageStep opens()
   {
      return new OpensDsl();
   }

   static RequiresModifierStep requires()
   {
      return new RequiresDsl();
   }

   static ProvidesServiceStep provides()
   {
      return new ProvidesDsl();
   }

   static UsesServiceStep uses()
   {
      return new UsesDsl();
   }

   static PackageJavaDocStep packageInfo()
   {
      return new PackageDsl();
   }

   class ExampleWrapper
   {
      static
      {
         constructor().javadoc("my doku")
                      .annotate("@AnAnnotation")
                      .public_()
                      .generic("T")
                      .type("MyType")
                      .parameter("String s")
                      .parameter("int i")
                      .throws_("IllegalStateException")
                      .body("throw null")
                      .render(RenderingContext.DEFAULT);
      }
   }
}
