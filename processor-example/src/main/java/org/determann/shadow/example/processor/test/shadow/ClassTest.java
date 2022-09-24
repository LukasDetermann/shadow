package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.converter.ShadowConverter;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.*;
import org.determann.shadow.api.wrapper.Property;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class ClassTest extends DeclaredTest<Class>
{
   protected ClassTest()
   {
      super(() -> SHADOW_API.getClass("java.lang.Object"));
   }

   @Test
   void getSuperClassTest()
   {
      Class integer = SHADOW_API.getClass("java.lang.Integer");
      Declared number = SHADOW_API.getClass("java.lang.Number");
      assertEquals(integer.getSuperClass(), number);

      Declared object = SHADOW_API.getClass("java.lang.Object");
      assertNull(SHADOW_API.convert(object).toClass().getSuperClass());

      assertEquals(object, SHADOW_API.convert(number).toClass().getSuperClass());
   }

   @Test
   void getPermittedSubClassesTest()
   {
      assertEquals(List.of("org.determann.shadow.example.processed.test.clazz.PermittedSubClassesExample.Child"),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.PermittedSubClassesExample")
                             .getPermittedSubClasses()
                             .stream()
                             .map(Object::toString)
                             .toList());
   }

   @Test
   void getPropertiesTest()
   {
      List<Property> properties = SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.PropertiesExample")
                                            .getProperties();

      assertEquals(1, properties.size());

      Property id = properties.get(0);
      assertEquals("id", id.getField().getSimpleName());
      assertEquals(SHADOW_API.getConstants().getPrimitiveInt(), id.getField().getType());

      assertEquals("getId", id.getGetter().getSimpleName());
      assertEquals(SHADOW_API.getConstants().getPrimitiveInt(), id.getGetter().getReturnType());
      assertEquals(Collections.emptyList(), id.getGetter().getParameters());

      assertEquals("setId", id.getSetter().getSimpleName());
      assertEquals(SHADOW_API.getConstants().getVoid(), id.getSetter().getReturnType());
      assertEquals(List.of(SHADOW_API.getConstants().getPrimitiveInt()), id.getSetter().getParameterTypes());
   }

   @Test
   void testGetOuterType()
   {
      assertEquals(SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.OuterTypeExample"),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.OuterTypeExample.InnerClass")
                             .getOuterType()
                             .orElseThrow());

      assertEquals(Optional.empty(),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.OuterTypeExample")
                             .getOuterType());

      assertEquals(Optional.empty(),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.OuterTypeExample.StaticInnerClass")
                             .getOuterType());
   }

   @Test
   void getDirectInterfacesTest()
   {
      List<Interface> directInterfaces = SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.DirectInterfacesExample.Child")
                                                   .getDirectInterfaces();

      assertEquals(1, directInterfaces.size());
      assertEquals(SHADOW_API.getInterface("org.determann.shadow.example.processed.test.clazz.Direct"),
                   directInterfaces.get(0));
   }

   @Test
   void testWithGenerics()
   {
      assertThrows(IllegalArgumentException.class,
                   () -> SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.InterpolateGenericsExample.DependentGeneric")
                                   .withGenerics("java.lang.String"));

      assertThrows(IllegalArgumentException.class, () -> SHADOW_API.getClass("java.lang.String").withGenerics("java.lang.String"));

      assertEquals(SHADOW_API.getClass("java.lang.String"),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.WithGenericsExample.Inner")
                             .withGenerics("java.lang.String")
                             .getGenerics()
                             .get(0));

      assertEquals(List.of(SHADOW_API.getClass("java.lang.String")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.InterpolateGenericsExample.IndependentGeneric")
                             .withGenerics("java.lang.String")
                             .getGenerics());

      assertEquals(List.of(SHADOW_API.getClass("java.lang.String"), SHADOW_API.getClass("java.lang.Number")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.InterpolateGenericsExample.DependentGeneric")
                             .withGenerics("java.lang.String", "java.lang.Number")
                             .getGenerics());
   }

   @Test
   void testInterpolateGenerics()
   {
      Class declared = SHADOW_API.getClass("org.determann.shadow.example.processed.test.clazz.InterpolateGenericsExample")
                                 .withGenerics(SHADOW_API.getClass("java.lang.String"),
                                               SHADOW_API.getConstants().getUnboundWildcard());
      Class capture = declared.interpolateGenerics();
      Shadow<TypeMirror> interpolated = SHADOW_API.convert(capture.getGenerics().get(1))
                                                  .toOptionalGeneric()
                                                  .map(Generic::getExtends)
                                                  .map(SHADOW_API::convert)
                                                  .flatMap(ShadowConverter::toOptionalInterface)
                                                  .map(Interface::getGenerics)
                                                  .map(shadows -> shadows.get(0))
                                                  .orElseThrow();
      assertEquals(SHADOW_API.getClass("java.lang.String"), interpolated);

      Class independentExample = SHADOW_API.getClass(
                                                 "org.determann.shadow.example.processed.test.clazz.InterpolateGenericsExample.IndependentGeneric")
                                           .withGenerics(SHADOW_API.getConstants().getUnboundWildcard());
      Class independentCapture = independentExample.interpolateGenerics();
      Shadow<TypeMirror> interpolatedIndependent = SHADOW_API.convert(independentCapture.getGenerics().get(0))
                                                             .toOptionalGeneric()
                                                             .map(Generic::getExtends)
                                                             .orElseThrow();
      assertEquals(SHADOW_API.getClass("java.lang.Object"), interpolatedIndependent);

      Class dependentExample = SHADOW_API.getClass(
                                               "org.determann.shadow.example.processed.test.clazz.InterpolateGenericsExample.DependentGeneric")
                                         .withGenerics(SHADOW_API.getConstants().getUnboundWildcard(),
                                                       SHADOW_API.getClass("java.lang.String"));
      Class dependentCapture = dependentExample.interpolateGenerics();
      Shadow<TypeMirror> interpolatedDependent = SHADOW_API.convert(dependentCapture.getGenerics().get(0))
                                                           .toOptionalGeneric()
                                                           .map(Generic::getExtends)
                                                           .orElseThrow();
      assertEquals(SHADOW_API.getClass("java.lang.String"), interpolatedDependent);
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      assertTrue(SHADOW_API.getClass("java.lang.Long").isSubtypeOf(SHADOW_API.getClass("java.lang.Number")));
      assertTrue(SHADOW_API.getClass("java.lang.Long").isSubtypeOf(SHADOW_API.getClass("java.lang.Long")));
      assertFalse(SHADOW_API.getClass("java.lang.Number").isSubtypeOf(SHADOW_API.getClass("java.lang.Long")));
   }

   @Test
   void testIsAssignableFrom()
   {
      //same as isSubtypeOF
      assertTrue(SHADOW_API.getClass("java.lang.Long").isAssignableFrom(SHADOW_API.getClass("java.lang.Number")));
      assertTrue(SHADOW_API.getClass("java.lang.Long").isAssignableFrom(SHADOW_API.getClass("java.lang.Long")));
      assertFalse(SHADOW_API.getClass("java.lang.Number").isAssignableFrom(SHADOW_API.getClass("java.lang.Long")));

      assertTrue(SHADOW_API.getClass("java.lang.Integer").isAssignableFrom(SHADOW_API.getConstants().getPrimitiveInt()));
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      assertEquals(List.of(SHADOW_API.getClass("java.lang.Number")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.ClassParent")
                             .getDirectSuperTypes());

      assertEquals(List.of(SHADOW_API.getClass("java.lang.Number"),
                           SHADOW_API.getInterface("java.lang.Comparable"),
                           SHADOW_API.getInterface("java.util.function.Consumer")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.ClassMixedParent")
                             .getDirectSuperTypes());
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      assertEquals(Set.of(),
                   SHADOW_API.getClass("java.lang.Object").getSuperTypes());

      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"),
                          SHADOW_API.getClass("java.lang.Number"),
                          SHADOW_API.getInterface("java.io.Serializable")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.ClassParent")
                             .getSuperTypes());

      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"),
                          SHADOW_API.getClass("java.lang.Number"),
                          SHADOW_API.getInterface("java.io.Serializable"),
                          SHADOW_API.getInterface("java.lang.Comparable"),
                          SHADOW_API.getInterface("java.util.function.Consumer")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.ClassMixedParent")
                             .getSuperTypes());
   }
}
