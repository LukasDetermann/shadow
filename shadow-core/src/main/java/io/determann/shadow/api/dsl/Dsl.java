package io.determann.shadow.api.dsl;

import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.type.C_Class;

//todo convert to interface with static methods
public abstract class Dsl
      implements PackageStep
{
   static
   {
      Dsl dsl = null;

      C_Class body = dsl.class_("myClass")
                        .body("asldkjf");

      C_Field aField = null;

      C_Class anotherClass = dsl.package_("org.example")
                                .import_("org.example.MyClass")
                                .public_()
                                .final_()
                                .static_()
                                .class_("MyClass")
                                .generic("<T>")
                                .extends_("ExtendsClass")
                                .implements_("AnInterface")
                                .permits("PermitsClass")
                                .field("first field")
                                .field("second field", "third field")
                                .field(aField)
                                .method("my first method")
                                .inner("an inner class")
                                .instanceInitializer("a instance initializer")
                                .staticInitializer("a static initializer")
                                .constructor("an constructor");
   }
}
