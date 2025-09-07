package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.C;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.test.TestFactory;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
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
      assertEquals("\"value\"", Dsl.annotationValue("value").render(createRenderingContext()));
   }

   @Test
   void stringNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue((String) null).render(createRenderingContext()));
   }

   @Test
   void boolean_()
   {
      assertEquals("true", Dsl.annotationValue(true).render(createRenderingContext()));
   }

   @Test
   void byte_()
   {
      //noinspection ConstantExpression
      assertEquals("1", Dsl.annotationValue((byte) 1).render(createRenderingContext()));
   }

   @Test
   void short_()
   {
      //noinspection ConstantExpression
      assertEquals("2", Dsl.annotationValue((short) 2).render(createRenderingContext()));
   }

   @Test
   void int_()
   {
      assertEquals("3", Dsl.annotationValue(3).render(createRenderingContext()));
   }

   @Test
   void long_()
   {
      assertEquals("3L", Dsl.annotationValue(3L).render(createRenderingContext()));
   }

   @Test
   void char_()
   {
      assertEquals("'c'", Dsl.annotationValue('c').render(createRenderingContext()));
   }

   @Test
   void float_()
   {
      assertEquals("4.0F", Dsl.annotationValue(4F).render(createRenderingContext()));
   }

   @Test
   void double_()
   {
      assertEquals("5.0D", Dsl.annotationValue(5D).render(createRenderingContext()));
   }

   @Test
   void c_enum_constant()
   {
      assertEquals("java.lang.annotation.ElementType.ANNOTATION_TYPE",
                   Dsl.annotationValue(TestFactory.create(C.EnumConstant.class,
                                                          "renderDeclaration",
                                                          "java.lang.annotation.ElementType.ANNOTATION_TYPE"))
                      .render(createRenderingContext()));
   }

   @Test
   void c_enum_constant_null()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue((C.EnumConstant) null).render(createRenderingContext()));
   }

   @Test
   void enum_constant()
   {
      assertEquals("java.lang.annotation.ElementType.ANNOTATION_TYPE",
                   Dsl.annotationValue(ElementType.ANNOTATION_TYPE).render(createRenderingContext()));
   }

   @Test
   void enum_constantNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((Enum<?>) null)).render(createRenderingContext()));
   }

   @Test
   void class_()
   {
      assertEquals("java.lang.String.class", Dsl.annotationValue(String.class).render(createRenderingContext()));
   }

   @Test
   void classNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((Class<?>) null)).render(createRenderingContext()));
   }

   @Test
   void c_typeArray()
   {
      assertEquals("boolean[].class", Dsl.annotationValue((TestFactory.create(C.Array.class, "renderType", "boolean[]"))).render(createRenderingContext()));
   }

   @Test
   void c_typeDeclared()
   {
      TestProvider.addValue("org.example.MyType");

      assertEquals("org.example.MyType.class", Dsl.annotationValue((TestFactory.create(C.Class.class))).render(createRenderingContext()));
   }

   @Test
   void c_typeVoid()
   {
      assertEquals("void.class", Dsl.annotationValue((TestFactory.create(C.Void.class))).render(createRenderingContext()));
   }

   @Test
   void c_typePrimitive()
   {
      assertEquals("boolean.class", Dsl.annotationValue(TestFactory.create(C.Primitive.class, "renderType", "boolean")).render(createRenderingContext()));
   }

   @Test
   void c_typeNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((C.Type) null)).render(createRenderingContext()));
   }

   @Test
   void c_annotationUsage()
   {
      assertEquals("@org.example.MyAnnotation",
                   Dsl.annotationValue((TestFactory.create(C.AnnotationUsage.class, "renderDeclaration", "@org.example.MyAnnotation")))
                      .render(createRenderingContext()));
   }

   @Test
   void c_annotationUsageNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((C.AnnotationUsage) null)).render(createRenderingContext()));
   }

   @Test
   void c_annotationValues()
   {
      assertEquals("{\"test\"}", Dsl.annotationValue((TestFactory.create(C.AnnotationValue.class, "\"test\""))).render(createRenderingContext()));
   }

   @Test
   void c_annotationValuesNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((C.AnnotationValue) null)).render(createRenderingContext()));
   }

   @Test
   void annotationValueRenderable()
   {
      assertEquals("{1, 1.0F}", Dsl.annotationValue(Dsl.annotationValue(1), Dsl.annotationValue(1F)).render(createRenderingContext()));
   }

   @Test
   void annotationValueRenderableNull()
   {
      assertThrows(NullPointerException.class, () -> Dsl.annotationValue(((AnnotationValueRenderable) null)).render(createRenderingContext()));
   }
}
