package io.determann.shadow.api.dsl.import_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

public interface ImportTypeStep
{
   ImportRenderable import_(String name);

   ImportRenderable import_(DeclaredRenderable declared);

   /// needs to be part of any declared
   ImportRenderable import_(DeclaredRenderable declared, MethodRenderable method);

   /// needs to be part of any declared
   ImportRenderable import_(DeclaredRenderable declared, FieldRenderable field);

   ImportRenderable importAll(String cPackage);

   ImportRenderable importAll(PackageRenderable cPackage);

   ImportRenderable importAll(DeclaredRenderable declared);
}