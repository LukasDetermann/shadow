package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getPackageOrThrow("java.base", "java.lang"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getPackageShadow("java.lang"))
                     .test(aPackage -> assertEquals("package java.lang;\n", render(DEFAULT, aPackage).declaration()));
   }
}