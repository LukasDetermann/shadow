package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.test.TestProvider.IMPLEMENTATION;
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
      assertEquals("exports org.example;", Dsl.exports("org.example").render(DEFAULT));
      //@end

      assertEquals("exports org.example to module;", Dsl.exports().package_("org.example").to("module").render(DEFAULT));

      C_Package aPackage = () -> IMPLEMENTATION;
      C_Module module = () -> IMPLEMENTATION;

      TestProvider.addValue("org.example");

      //@start region="exports-api-simple-type"
      assertEquals("exports org.example;", Dsl.exports(aPackage).render(DEFAULT));
      //@end

      TestProvider.addValue("org.example");
      TestProvider.addValue("andAnotherOne");

      //@start region="exports-api"
      assertEquals("""
                   exports org.example to
                   anotherOne,
                   andAnotherOne;""",
                   Dsl.exports().package_(aPackage)
                      .to("anotherOne")
                      .to(module)
                      .render(DEFAULT));
      //@end
   }

   @Test
   void uses()
   {
      //@start region="uses-api-string"
      assertEquals("uses org.example.MySpi;", Dsl.uses("org.example.MySpi").render(DEFAULT));
      //@end

      C_Class service = () -> IMPLEMENTATION;
      TestProvider.addValue("org.example.MySpi");

      //@start region="uses-api-type"
      assertEquals("uses org.example.MySpi;", Dsl.uses(service).render(DEFAULT));
      //@end
   }

   @Test
   void opens()
   {
      //@start region="opens-api-simple-string"
      assertEquals("opens org.example;", Dsl.opens("org.example").render(DEFAULT));
      //@end

      assertEquals("opens org.example to module;", Dsl.opens().package_("org.example").to("module").render(DEFAULT));

      C_Package aPackage = () -> IMPLEMENTATION;
      C_Module module = () -> IMPLEMENTATION;

      TestProvider.addValue("org.example");

      //@start region="opens-api-simple-type"
      assertEquals("opens org.example;", Dsl.opens(aPackage).render(DEFAULT));
      //@end

      TestProvider.addValue("org.example");
      TestProvider.addValue("andAnotherOne");

      //@start region="opens-api"
      assertEquals("""
                   opens org.example to
                   anotherOne,
                   andAnotherOne;""",
                   Dsl.opens().package_(aPackage)
                      .to("anotherOne")
                      .to(module)
                      .render(DEFAULT));
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
                      .render(DEFAULT));
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
                      .render(DEFAULT));
      //@end
   }
}
