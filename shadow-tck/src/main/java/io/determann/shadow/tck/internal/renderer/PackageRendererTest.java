package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Package;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_PACKAGE_IN_MODULE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageRendererTest
{
   @Test
   void declaration()
   {
      test(implementation ->
           {
              C_Package cPackage = requestOrThrow(implementation, GET_PACKAGE_IN_MODULE, "java.base", "java.lang");
              String actual = render(cPackage).declaration(DEFAULT);
              assertEquals("package java.lang;\n", actual);
           });
   }
}