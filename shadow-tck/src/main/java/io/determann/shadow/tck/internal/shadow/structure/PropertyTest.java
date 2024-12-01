package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.C_Nameable;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Property;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class PropertyTest
{
   @Test
   void id()
   {
      withSource("Pojo", """
            class Pojo {
               private Long id;
               public Long getId() {return id;}
               public void setId(Long id) {this.id = id;}
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Pojo");
                     List<? extends C_Property> properties = requestOrThrow(cClass, CLASS_GET_PROPERTIES);

                     assertEquals(2, properties.size());
                     assertEquals("class", requestOrThrow(properties.get(0), NAMEABLE_GET_NAME));

                     assertEquals("id", requestOrThrow(properties.get(1), NAMEABLE_GET_NAME));
                     assertTrue(requestOrThrow(properties.get(1), PROPERTY_IS_MUTABLE));
                     C_Nameable type = ((C_Nameable) requestOrThrow(properties.get(1), PROPERTY_GET_TYPE));
                     assertEquals("Long", requestOrThrow(type, NAMEABLE_GET_NAME));
                     C_Field field = requestOrThrow(properties.get(1), PROPERTY_GET_FIELD);
                     assertEquals("id", requestOrThrow(field, NAMEABLE_GET_NAME));
                     C_Method getter = requestOrThrow(properties.get(1), PROPERTY_GET_GETTER);
                     assertEquals("getId", requestOrThrow(getter, NAMEABLE_GET_NAME));
                     C_Method setter = requestOrThrow(properties.get(1), PROPERTY_GET_SETTER);
                     assertEquals("setId", requestOrThrow(setter, NAMEABLE_GET_NAME));
                  });
   }

   @Test
   void isAndSet()
   {
      withSource("Pojo", """
            class Pojo {
               public boolean isValid() {return false;}
               public boolean getValid() {return false;}
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Pojo");
                     List<? extends C_Property> properties = requestOrThrow(cClass, CLASS_GET_PROPERTIES);

                     assertEquals(2, properties.size());
                     assertEquals("class", requestOrThrow(properties.get(0), NAMEABLE_GET_NAME));

                     assertEquals("valid", requestOrThrow(properties.get(1), NAMEABLE_GET_NAME));
                     assertFalse(requestOrThrow(properties.get(1), PROPERTY_IS_MUTABLE));
                     C_Nameable type = ((C_Nameable) requestOrThrow(properties.get(1), PROPERTY_GET_TYPE));
                     assertEquals("boolean", requestOrThrow(type, NAMEABLE_GET_NAME));
                     assertTrue(requestOrEmpty(properties.get(1), PROPERTY_GET_FIELD).isEmpty());
                     C_Method getter = requestOrThrow(properties.get(1), PROPERTY_GET_GETTER);
                     assertEquals("isValid", requestOrThrow(getter, NAMEABLE_GET_NAME));
                     assertTrue(requestOrEmpty(properties.get(1), PROPERTY_GET_SETTER).isEmpty());
                  });
   }

   @Test
   void generic()
   {
      withSource("Pojo", """
            class Pojo {
               private java.util.List<String> values;
               public java.util.List<String> getValues() {return values;}
               public void setValues(java.util.List<String> values) {this.values = values;}
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Pojo");
                     List<? extends C_Property> properties = requestOrThrow(cClass, CLASS_GET_PROPERTIES);

                     assertEquals(2, properties.size());
                     assertEquals("class", requestOrThrow(properties.get(0), NAMEABLE_GET_NAME));

                     assertEquals("values", requestOrThrow(properties.get(1), NAMEABLE_GET_NAME));
                     assertTrue(requestOrThrow(properties.get(1), PROPERTY_IS_MUTABLE));
                     C_Nameable type = ((C_Nameable) requestOrThrow(properties.get(1), PROPERTY_GET_TYPE));
                     assertEquals("List", requestOrThrow(type, NAMEABLE_GET_NAME));
                     C_Field field = requestOrThrow(properties.get(1), PROPERTY_GET_FIELD);
                     assertEquals("values", requestOrThrow(field, NAMEABLE_GET_NAME));
                     C_Method getter = requestOrThrow(properties.get(1), PROPERTY_GET_GETTER);
                     assertEquals("getValues", requestOrThrow(getter, NAMEABLE_GET_NAME));
                     C_Method setter = requestOrThrow(properties.get(1), PROPERTY_GET_SETTER);
                     assertEquals("setValues", requestOrThrow(setter, NAMEABLE_GET_NAME));
                  });
   }

   @Test
   void sTrangeCase()
   {
      withSource("Pojo", """
            class Pojo {
               private String sTrangeCase;
               public String getsTrangeCase() {return sTrangeCase;}
               public void setsTrangeCase(String sTrangeCase) {this.sTrangeCase = sTrangeCase;}
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Pojo");
                     List<? extends C_Property> properties = requestOrThrow(cClass, CLASS_GET_PROPERTIES);

                     assertEquals(2, properties.size());
                     assertEquals("class", requestOrThrow(properties.get(0), NAMEABLE_GET_NAME));

                     assertEquals("sTrangeCase", requestOrThrow(properties.get(1), NAMEABLE_GET_NAME));
                     assertTrue(requestOrThrow(properties.get(1), PROPERTY_IS_MUTABLE));
                     C_Nameable type = ((C_Nameable) requestOrThrow(properties.get(1), PROPERTY_GET_TYPE));
                     assertEquals("String", requestOrThrow(type, NAMEABLE_GET_NAME));
                     C_Field field = requestOrThrow(properties.get(1), PROPERTY_GET_FIELD);
                     assertEquals("sTrangeCase", requestOrThrow(field, NAMEABLE_GET_NAME));
                     C_Method getter = requestOrThrow(properties.get(1), PROPERTY_GET_GETTER);
                     assertEquals("getsTrangeCase", requestOrThrow(getter, NAMEABLE_GET_NAME));
                     C_Method setter = requestOrThrow(properties.get(1), PROPERTY_GET_SETTER);
                     assertEquals("setsTrangeCase", requestOrThrow(setter, NAMEABLE_GET_NAME));
                  });
   }

   @Test
   void SCREAMING_SNAKE_CASE()
   {
      withSource("Pojo", """
            class Pojo {
               private String NOT_A_CONSTANT;
               public String getNOT_A_CONSTANT() {return NOT_A_CONSTANT;}
               public void setNOT_A_CONSTANT(String NOT_A_CONSTANT) {this.NOT_A_CONSTANT = NOT_A_CONSTANT;}
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Pojo");
                     List<? extends C_Property> properties = requestOrThrow(cClass, CLASS_GET_PROPERTIES);

                     assertEquals(2, properties.size());
                     assertEquals("class", requestOrThrow(properties.get(0), NAMEABLE_GET_NAME));

                     assertEquals("NOT_A_CONSTANT", requestOrThrow(properties.get(1), NAMEABLE_GET_NAME));
                     assertTrue(requestOrThrow(properties.get(1), PROPERTY_IS_MUTABLE));
                     C_Nameable type = ((C_Nameable) requestOrThrow(properties.get(1), PROPERTY_GET_TYPE));
                     assertEquals("String", requestOrThrow(type, NAMEABLE_GET_NAME));
                     C_Field field = requestOrThrow(properties.get(1), PROPERTY_GET_FIELD);
                     assertEquals("NOT_A_CONSTANT", requestOrThrow(field, NAMEABLE_GET_NAME));
                     C_Method getter = requestOrThrow(properties.get(1), PROPERTY_GET_GETTER);
                     assertEquals("getNOT_A_CONSTANT", requestOrThrow(getter, NAMEABLE_GET_NAME));
                     C_Method setter = requestOrThrow(properties.get(1), PROPERTY_GET_SETTER);
                     assertEquals("setNOT_A_CONSTANT", requestOrThrow(setter, NAMEABLE_GET_NAME));
                  });
   }


   @Test
   void voidMethod()
   {
      withSource("Pojo", """
            class Pojo {
               public void foo() {}
            }""")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Pojo");
                     List<? extends C_Property> properties = requestOrThrow(cClass, CLASS_GET_PROPERTIES);

                     assertEquals(1, properties.size());
                     assertEquals("class", requestOrThrow(properties.get(0), NAMEABLE_GET_NAME));
                  });
   }

   @Test
   void overwrittenAccessor()
   {
      withSource("Parent", "abstract class Parent{public abstract Long getId();}")
            .withSource("Child", "abstract class Child extends Parent{public abstract Long getId();}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Child");
                     List<? extends C_Property> properties = requestOrThrow(cClass, CLASS_GET_PROPERTIES);

                     assertEquals(2, properties.size());
                     assertEquals("class", requestOrThrow(properties.get(0), NAMEABLE_GET_NAME));
                     assertEquals("id", requestOrThrow(properties.get(1), NAMEABLE_GET_NAME));

                     C_Method getter = requestOrThrow(properties.get(1), PROPERTY_GET_GETTER);
                     C_Declared cDeclared = requestOrThrow(getter, EXECUTABLE_GET_SURROUNDING);
                     assertEquals("Child", requestOrThrow(cDeclared, NAMEABLE_GET_NAME));
                  });
   }
}
