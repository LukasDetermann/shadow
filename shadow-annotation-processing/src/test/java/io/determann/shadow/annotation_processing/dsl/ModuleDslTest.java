package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
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
                   JavaDsl.moduleInfo()
                          .copyright("/// some Copyright notice")
                          .javadoc("/// some java doc")
                          .annotate("org.example.MyAnnotation")
                          .name("my.module")
                          .requires(JavaDsl.requires().transitive().dependency("other.module"))
                          .exports(JavaDsl.exports("org.example.export_package"))
                          .opens(JavaDsl.opens("org.example.opens_package"))
                          .uses(JavaDsl.uses("org.example.Spi"))
                          .provides(JavaDsl.provides().service("org.example.Spi")
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
                   JavaDsl.moduleInfo()
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation"))
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
                   JavaDsl.moduleInfo()
                          .name("my.module")
                          .requires("other.module")
                          .exports("some.package")
                          .opens("another.package")
                          .uses("some.Service")
                          .provides("an.other.Service with ServiceImpl")
                          .renderModuleInfo(createRenderingContext()));
   }
}
