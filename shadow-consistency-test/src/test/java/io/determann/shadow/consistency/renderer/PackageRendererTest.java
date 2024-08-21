package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Package>compileTime(context -> context.getPackageOrThrow("java.base", "java.lang"))
                     .runtime(stringClassFunction -> R_Adapter.getPackage("java.lang"))
                     .test(aPackage -> assertEquals("package java.lang;\n", render(DEFAULT, aPackage).declaration()));
   }
}