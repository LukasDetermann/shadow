package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
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
                   @org.example.MyAnnotation
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
                      .renderModuleInfo(createRenderingContext()));
      //@end
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   module my.module {
                   }""",
                   Dsl.moduleInfo()
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .name("my.module")
                      .renderModuleInfo(createRenderingContext()));
   }

   @Test
   void stringApi()
   {
      assertEquals("""
                   module my.module {
                   
                   requires other.module;
                   
                   exports some.package;
                   
                   opens another.package;
                   
                   uses some.Service;
                   
                   provies an.other.Service with ServiceImpl;
                   }""",
                   Dsl.moduleInfo()
                         .name("my.module")
                         .requires("other.module")
                         .exports("some.package")
                         .opens("another.package")
                         .uses("some.Service")
                         .provides("an.other.Service with ServiceImpl")
                         .renderModuleInfo(createRenderingContext()));
   }
}
