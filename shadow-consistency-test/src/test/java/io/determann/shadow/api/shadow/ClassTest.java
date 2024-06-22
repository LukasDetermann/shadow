package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.TypeConverter;
import io.determann.shadow.api.lang_model.LangModelQueries;
import io.determann.shadow.api.lang_model.query.GenericLangModel;
import io.determann.shadow.api.lang_model.query.InterfaceLangModel;
import io.determann.shadow.api.property.Property;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.*;

class ClassTest extends DeclaredTest<Class>
{
   ClassTest()
   {
      super(shadowApi -> shadowApi.getClassOrThrow("java.lang.Object"));
   }

   @Test
   void getSuperClassTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class integer = shadowApi.getClassOrThrow("java.lang.Integer");
                               Declared number = shadowApi.getClassOrThrow("java.lang.Number");
                               assertEquals(query(integer).getSuperClass(), number);

                               Declared object = shadowApi.getClassOrThrow("java.lang.Object");
                               assertNull(query(convert(object).toClassOrThrow()).getSuperClass());

                               assertEquals(object, query(convert(number).toClassOrThrow()).getSuperClass());
                            })
                   .compile();
   }

   @Test
   void getPermittedSubClassesTest()
   {
      ProcessorTest.process(shadowApi -> assertEquals(List.of("PermittedSubClassesExample.Child"),
                                                      query(shadowApi.getClassOrThrow("PermittedSubClassesExample"))
                                                               .getPermittedSubClasses()
                                                               .stream()
                                                               .map(Object::toString)
                                                               .toList()))
                   .withCodeToCompile("PermittedSubClassesExample.java", """
                         public sealed class PermittedSubClassesExample permits PermittedSubClassesExample.Child
                         {
                            public static final class Child extends PermittedSubClassesExample {}
                         }
                         """)
                   .compile();
   }

   @Test
   void getPropertiesTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Property> properties = query(shadowApi.getClassOrThrow("PropertiesExample")).getProperties();

                               assertEquals(2, properties.size());

                               Property id = properties.get(1);
                               assertEquals("id", LangModelQueries.query((Nameable) id.getField().get()).getName());
                               assertEquals(shadowApi.getConstants().getPrimitiveInt(), query(id.getField().get()).getType());

                               assertEquals("getId", LangModelQueries.query((Nameable) id.getGetter()).getName());
                               assertEquals(shadowApi.getConstants().getPrimitiveInt(), LangModelQueries.query(id.getGetter()).getReturnType());
                               assertEquals(Collections.emptyList(), LangModelQueries.query(id.getGetter()).getParameters());

                               assertEquals("setId", LangModelQueries.query((Nameable) id.getSetterOrThrow()).getName());
                               assertEquals(shadowApi.getConstants().getVoid(), LangModelQueries.query(id.getSetterOrThrow()).getReturnType());
                               assertEquals(List.of(shadowApi.getConstants().getPrimitiveInt()), LangModelQueries.query(id.getSetterOrThrow()).getParameterTypes());
                            })
                   .withCodeToCompile("PropertiesExample.java", """
                         public class PropertiesExample {
                            private int id;

                            public int getId() {return id;}
                            public void setId(int id) {this.id = id;}
                         }
                         """)
                   .compile();
   }

   @Test
   void testGetOuterType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(shadowApi.getClassOrThrow("OuterTypeExample"),
                                            query(shadowApi.getClassOrThrow("OuterTypeExample.InnerClass")).getOuterType().orElseThrow());

                               assertEquals(Optional.empty(),
                                            query(shadowApi.getClassOrThrow("OuterTypeExample")).getOuterType());

                               assertEquals(Optional.empty(),
                                            query(shadowApi.getClassOrThrow("OuterTypeExample.StaticInnerClass")).getOuterType());
                            })
                   .withCodeToCompile("OuterTypeExample.java", """
                         public class OuterTypeExample {
                            private class InnerClass {}
                            private static class StaticInnerClass {}
                         }
                         """)
                   .compile();
   }

   @Test
   void getDirectInterfacesTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Interface> directInterfaces = query(shadowApi.getClassOrThrow("DirectInterfacesExample.Child"))
                                                                           .getDirectInterfaces();

                               assertEquals(1, directInterfaces.size());
                               assertEquals(shadowApi.getInterfaceOrThrow("Direct"), directInterfaces.get(0));
                            })
                   .withCodeToCompile("Direct.java", "public interface Direct{}")
                   .withCodeToCompile("InDirect.java", "public interface InDirect{}")
                   .withCodeToCompile("DirectInterfacesExample.java", """
                         public class DirectInterfacesExample implements InDirect {
                            public static class Child extends DirectInterfacesExample implements Direct {}
                         }
                         """)
                   .compile();
   }

   @Test
   void testWithGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                         "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.withGenerics(shadowApi.getClassOrThrow("java.lang.String"), "java.lang.String"));

                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"),
                                            query(shadowApi.withGenerics(shadowApi.getClassOrThrow("WithGenericsExample.Inner"), "java.lang.String"))
                                                     .getGenericTypes()
                                                     .get(0));

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String")),
                                            query(shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric"),
                                                                   "java.lang.String"))
                                                     .getGenericTypes());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String"),
                                                    shadowApi.getClassOrThrow("java.lang.Number")),
                                            query(shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                   "java.lang.String",
                                                                   "java.lang.Number"))
                                                     .getGenericTypes());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample <A extends java.lang.Comparable<B>, B extends java.lang.Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
                   .withCodeToCompile("WithGenericsExample.java", """
                         public class WithGenericsExample {
                            class Inner<T> {}
                         }
                         """)
                   .compile();
   }

   @Test
   void testInterpolateGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class declared = shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample"),
                                                                                 shadowApi.getClassOrThrow("java.lang.String"),
                                                                                 shadowApi.getConstants().getUnboundWildcard());
                               Class capture = shadowApi.interpolateGenerics(declared);
                               Shadow interpolated = convert(query(capture).getGenericTypes().get(1))
                                     .toGeneric()
                                     .map(LangModelQueries::query)
                                     .map(GenericLangModel::getExtends)
                                     .map(Converter::convert)
                                     .flatMap(TypeConverter::toInterface)
                                     .map(LangModelQueries::query)
                                     .map(InterfaceLangModel::getGenericTypes)
                                     .map(shadows -> shadows.get(0))
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolated);

                               Class independentExample = shadowApi.withGenerics(shadowApi.getClassOrThrow(
                                                                                       "InterpolateGenericsExample.IndependentGeneric"),
                                                                                           shadowApi.getConstants().getUnboundWildcard());
                               Class independentCapture = shadowApi.interpolateGenerics(independentExample);
                               Shadow interpolatedIndependent = convert(query(independentCapture).getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(LangModelQueries::query)
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Class dependentExample = shadowApi.withGenerics(shadowApi.getClassOrThrow(
                                                                                     "InterpolateGenericsExample.DependentGeneric"),
                                                                                         shadowApi.getConstants().getUnboundWildcard(),
                                                                                         shadowApi.getClassOrThrow("java.lang.String"));
                               Class dependentCapture = shadowApi.interpolateGenerics(dependentExample);
                               Shadow interpolatedDependent = convert(query(dependentCapture).getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(LangModelQueries::query)
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolatedDependent);
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample <A extends Comparable<B>, B extends Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
                   .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(query(shadowApi.getClassOrThrow("java.lang.Long"))
                                                .isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                               assertTrue(query(shadowApi.getClassOrThrow("java.lang.Long")).isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Long")));
                               assertFalse(query(shadowApi.getClassOrThrow("java.lang.Number"))
                                                 .isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Long")));
                            })
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               //same as isSubtypeOF
                               assertTrue(query(shadowApi.getClassOrThrow("java.lang.Long"))
                                                   .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                               assertTrue(query(shadowApi.getClassOrThrow("java.lang.Long"))
                                                   .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Long")));
                               assertFalse(query(shadowApi.getClassOrThrow("java.lang.Number"))
                                                    .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Long")));

                               assertTrue(query(shadowApi.getClassOrThrow("java.lang.Integer"))
                                                   .isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Number")),
                                            query(shadowApi.getClassOrThrow("ClassParent"))
                                                  .getDirectSuperTypes());

                               assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Number"),
                                                    shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                    shadowApi.getInterfaceOrThrow("java.util.function.Consumer")),
                                            query(shadowApi.getClassOrThrow("ClassMixedParent"))
                                                     .getDirectSuperTypes());
                            })
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(Set.of(),
                                            query(shadowApi.getClassOrThrow("java.lang.Object")).getSuperTypes());

                               assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                   shadowApi.getClassOrThrow("java.lang.Number"),
                                                   shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                            query(shadowApi.getClassOrThrow("ClassParent")).getSuperTypes());

                               assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                   shadowApi.getClassOrThrow("java.lang.Number"),
                                                   shadowApi.getInterfaceOrThrow("java.io.Serializable"),
                                                   shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                   shadowApi.getInterfaceOrThrow("java.util.function.Consumer")),
                                            query(shadowApi.getClassOrThrow("ClassMixedParent")).getSuperTypes());
                            })
                   .withCodeToCompile("ClassNoParent.java", "class ClassNoParent {}")
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }
}
