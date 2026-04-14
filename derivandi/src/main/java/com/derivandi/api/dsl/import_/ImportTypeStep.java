package com.derivandi.api.dsl.import_;

import com.derivandi.api.dsl.declared.DeclaredRenderable;
import com.derivandi.api.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface ImportTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ImportRenderable declared(String name);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable declared(DeclaredRenderable declared);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable package_(String cPackage);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable package_(PackageRenderable cPackage);
}