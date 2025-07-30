package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.C_Void;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.api.test.TestFactory;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnnotationValueDslTest
{
   @AfterEach
   void reset()
   {
      TestProvider.reset();
   }

   @Test
   void string()
   {
      assertEquals("\"value\"", Dsl.annotationValue("value").render(DEFAULT));
   }

   @Test
   void stringNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue((String) null).render(DEFAULT));
   }

   @Test
   void boolean_()
   {
      assertEquals("true", Dsl.annotationValue(true).render(DEFAULT));
   }

   @Test
   void byte_()
   {
      //noinspection ConstantExpression
      assertEquals("1", Dsl.annotationValue((byte) 1).render(DEFAULT));
   }

   @Test
   void short_()
   {
      //noinspection ConstantExpression
      assertEquals("2", Dsl.annotationValue((short) 2).render(DEFAULT));
   }

   @Test
   void int_()
   {
      assertEquals("3", Dsl.annotationValue(3).render(DEFAULT));
   }

   @Test
   void long_()
   {
      assertEquals("3L", Dsl.annotationValue(3L).render(DEFAULT));
   }

   @Test
   void char_()
   {
      assertEquals("'c'", Dsl.annotationValue('c').render(DEFAULT));
   }

   @Test
   void float_()
   {
      assertEquals("4.0F", Dsl.annotationValue(4F).render(DEFAULT));
   }

   @Test
   void double_()
   {
      assertEquals("5.0D", Dsl.annotationValue(5D).render(DEFAULT));
   }

   @Test
   void c_enum_constant()
   {
      assertEquals("java.lang.annotation.ElementType.ANNOTATION_TYPE",
                   Dsl.annotationValue(TestFactory.create(C_EnumConstant.class,
                                                          "renderDeclaration",
                                                          "java.lang.annotation.ElementType.ANNOTATION_TYPE"))
                      .render(DEFAULT));
   }

   @Test
   void c_enum_constant_null()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue((C_EnumConstant) null).render(DEFAULT));
   }

   @Test
   void enum_constant()
   {
      assertEquals("java.lang.annotation.ElementType.ANNOTATION_TYPE",
                   Dsl.annotationValue(ElementType.ANNOTATION_TYPE).render(DEFAULT));
   }

   @Test
   void enum_constantNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((Enum<?>) null)).render(DEFAULT));
   }

   @Test
   void class_()
   {
      assertEquals("java.lang.String.class", Dsl.annotationValue(String.class).render(DEFAULT));
   }

   @Test
   void classNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((Class<?>) null)).render(DEFAULT));
   }

   @Test
   void c_typeArray()
   {
      assertEquals("boolean[].class", Dsl.annotationValue((TestFactory.create(C_Array.class, "renderType", "boolean[]"))).render(DEFAULT));
   }

   @Test
   void c_typeDeclared()
   {
      TestProvider.addValue("org.example.MyType");

      assertEquals("org.example.MyType.class", Dsl.annotationValue((TestFactory.create(C_Class.class))).render(DEFAULT));
   }

   @Test
   void c_typeVoid()
   {
      assertEquals("void.class", Dsl.annotationValue((TestFactory.create(C_Void.class))).render(DEFAULT));
   }

   @Test
   void c_typePrimitive()
   {
      assertEquals("boolean.class", Dsl.annotationValue(TestFactory.create(C_Primitive.class, "renderType", "boolean")).render(DEFAULT));
   }

   @Test
   void c_typeNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((C_Type) null)).render(DEFAULT));
   }

   @Test
   void c_annotationUsage()
   {
      assertEquals("@org.example.MyAnnotation",
                   Dsl.annotationValue((TestFactory.create(C_AnnotationUsage.class, "renderDeclaration", "@org.example.MyAnnotation")))
                      .render(DEFAULT));
   }

   @Test
   void c_annotationUsageNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((C_AnnotationUsage) null)).render(DEFAULT));
   }

   @Test
   void c_annotationValues()
   {
      assertEquals("{\"test\"}", Dsl.annotationValue((TestFactory.create(C_AnnotationValue.class, "\"test\""))).render(DEFAULT));
   }

   @Test
   void c_annotationValuesNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((C_AnnotationValue) null)).render(DEFAULT));
   }

   @Test
   void annotationValueRenderable()
   {
      assertEquals("{1, 1.0F}", Dsl.annotationValue(Dsl.annotationValue(1), Dsl.annotationValue(1F)).render(DEFAULT));
   }

   @Test
   void annotationValueRenderableNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((AnnotationValueRenderable) null)).render(DEFAULT));
   }
}
