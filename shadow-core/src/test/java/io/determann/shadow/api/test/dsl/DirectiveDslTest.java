package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.C;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.test.TestFactory;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectiveDslTest
{
   @AfterEach
   void reset()
   {
      TestProvider.reset();
   }

   @Test
   void exports()
   {
      //@start region="exports-api-simple-string"
      assertEquals("exports org.example;", Dsl.exports("org.example").renderDeclaration(createRenderingContext()));
      //@end

      assertEquals("exports org.example to module;", Dsl.exports().package_("org.example").to("module").renderDeclaration(createRenderingContext()));

      C.Package aPackage = TestFactory.create(C.Package.class, "renderQualifiedName", "org.example");
      C.Module module = TestFactory.create(C.Module.class, "renderQualifiedName", "andAnotherOne");

      //@start region="exports-api-simple-type"
      assertEquals("exports org.example;", Dsl.exports(aPackage).renderDeclaration(createRenderingContext()));
      //@end

      //@start region="exports-api"
      assertEquals("""
                   exports org.example to
                   anotherOne,
                   andAnotherOne;""",
                   Dsl.exports().package_(aPackage)
                      .to("anotherOne")
                      .to(module)
                      .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void uses()
   {
      //@start region="uses-api-string"
      assertEquals("uses org.example.MySpi;", Dsl.uses("org.example.MySpi").renderDeclaration(createRenderingContext()));
      //@end

      C.Class service = TestFactory.create(C.Class.class);
      TestProvider.addValue("org.example.MySpi");

      //@start region="uses-api-type"
      assertEquals("uses org.example.MySpi;", Dsl.uses(service).renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void opens()
   {
      //@start region="opens-api-simple-string"
      assertEquals("opens org.example;", Dsl.opens("org.example").renderDeclaration(createRenderingContext()));
      //@end

      assertEquals("opens org.example to module;", Dsl.opens().package_("org.example").to("module").renderDeclaration(createRenderingContext()));

      C.Package aPackage = TestFactory.create(C.Package.class, "renderQualifiedName", "org.example");
      C.Module module = TestFactory.create(C.Module.class, "renderQualifiedName", "andAnotherOne");

      //@start region="opens-api-simple-type"
      assertEquals("opens org.example;", Dsl.opens(aPackage).renderDeclaration(createRenderingContext()));
      //@end

      //@start region="opens-api"
      assertEquals("""
                   opens org.example to
                   anotherOne,
                   andAnotherOne;""",
                   Dsl.opens().package_(aPackage)
                      .to("anotherOne")
                      .to(module)
                      .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void requires()
   {
      //@start region="requires-api"
      assertEquals("requires transitive org.example;",
                   Dsl.requires()
                      .transitive()
                      .dependency("org.example")
                      .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void provides()
   {
      //@start region="provides-api"
      assertEquals("""
                   provides org.example.Spi with
                   org.example.Implementation1,
                   org.example.Implementation2;""",
                   Dsl.provides()
                      .service("org.example.Spi")
                      .with("org.example.Implementation1")
                      .with("org.example.Implementation2")
                      .renderDeclaration(createRenderingContext()));
      //@end
   }
}
