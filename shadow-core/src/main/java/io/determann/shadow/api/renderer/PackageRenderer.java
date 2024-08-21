package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.structure.C_Package;

public interface PackageRenderer
{
   /**
    * {@code package my.example;}
    *
    * @throws IllegalArgumentException when the package is {@link  C_Package#isUnnamed()}
    */
   String declaration();
}
