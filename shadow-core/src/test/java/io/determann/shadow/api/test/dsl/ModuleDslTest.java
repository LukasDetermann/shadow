package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModuleDslTest
{
   @Test
   void api()
   {
      //@start region="api"
      assertEquals("""
                   /// some Copyright notice
                   
                   /// some java doc
                   module my.module {
                   
                   requires transitive other.module;
                   
                   exports org.example.export_package;
                   
                   opens org.example.opens_package;
                   
                   uses org.example.Spi;
                   
                   provides org.example.Spi with org.example.SpiImplementation;
                   }""",
                   Dsl.moduleInfo()
                      .copyright("/// some Copyright notice")
                      .javadoc("/// some java doc")
                      .annotate("org.example.MyAnnotation")
                      .name("my.module")
                      .requires(Dsl.requires().transitive().dependency("other.module"))
                      .exports(Dsl.exports("org.example.export_package"))
                      .opens(Dsl.opens("org.example.opens_package"))
                      .uses(Dsl.uses("org.example.Spi"))
                      .provides(Dsl.provides().service("org.example.Spi")
                                   .with("org.example.SpiImplementation"))
                      .render(DEFAULT));
      //@end
   }
}
