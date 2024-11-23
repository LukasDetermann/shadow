package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Package;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.determann.shadow.api.Operations.GET_PACKAGE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.Tck.TCK;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageRendererTest
{
   @Test
   void declaration()
   {
      TCK.test(Collections.emptyList(), implementation ->
      {
         C_Package cPackage = requestOrThrow(implementation, GET_PACKAGE, "java.base", "java.lang");
         String actual = render(DEFAULT, cPackage).declaration();
         assertEquals("package java.lang;\n", actual);
      });
   }
}