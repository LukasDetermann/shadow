package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.structure.Package;

public interface PackageRenderer
{
   /**
    * {@code package my.example;}
    *
    * @throws IllegalArgumentException when the package is {@link  Package#isUnnamed()}
    */
   String declaration();
}
