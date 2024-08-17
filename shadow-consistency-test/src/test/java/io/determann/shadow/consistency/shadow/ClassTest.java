package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.QualifiedNameableLamgModel;
import io.determann.shadow.api.lang_model.shadow.structure.PropertyLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.InterfaceLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.type.Class;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest extends DeclaredTest<Class>
{
   ClassTest()
   {
      super(context -> context.getClassOrThrow("java.lang.Object"));
   }

   @Test
   void getSuperClassTest()
   {
      ProcessorTest.process(context ->
                            {
                               ClassLangModel integer = context.getClassOrThrow("java.lang.Integer");
                               ClassLangModel number = context.getClassOrThrow("java.lang.Number");
                               assertEquals(integer.getSuperClass(), number);

                               ClassLangModel object = context.getClassOrThrow("java.lang.Object");
                               assertNull(object.getSuperClass());

                               assertEquals(object, number.getSuperClass());
                            })
                   .compile();
   }

   @Test
   void getPermittedSubClassesTest()
   {
      ProcessorTest.process(context -> assertEquals(List.of("PermittedSubClassesExample.Child"),
                                                      context.getClassOrThrow("PermittedSubClassesExample")
                                                               .getPermittedSubClasses()
                                                               .stream()
                                                               .map(QualifiedNameableLamgModel::getQualifiedName)
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
      ProcessorTest.process(context ->
                            {
                               List<PropertyLangModel> properties = context.getClassOrThrow("PropertiesExample").getProperties();

                               assertEquals(2, properties.size());

                               PropertyLangModel id = properties.get(1);
                               assertEquals("id", id.getField().get().getName());
                               assertEquals(context.getConstants().getPrimitiveInt(), id.getField().get().getType());

                               assertEquals("getId", id.getGetter().getName());
                               assertEquals(context.getConstants().getPrimitiveInt(), id.getGetter().getReturnType());
                               assertEquals(Collections.emptyList(), id.getGetter().getParameters());

                               assertEquals("setId", id.getSetterOrThrow().getName());
                               assertEquals(context.getConstants().getVoid(), id.getSetterOrThrow().getReturnType());
                               assertEquals(List.of(context.getConstants().getPrimitiveInt()), id.getSetterOrThrow().getParameterTypes());
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(context.getClassOrThrow("OuterTypeExample"),
                                            context.getClassOrThrow("OuterTypeExample.InnerClass").getOuterType().orElseThrow());

                               assertEquals(Optional.empty(),
                                            context.getClassOrThrow("OuterTypeExample").getOuterType());

                               assertEquals(Optional.empty(),
                                            context.getClassOrThrow("OuterTypeExample.StaticInnerClass").getOuterType());
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
      ProcessorTest.process(context ->
                            {
                               List<InterfaceLangModel> directInterfaces = context.getClassOrThrow("DirectInterfacesExample.Child")
                                                                                    .getDirectInterfaces();

                               assertEquals(1, directInterfaces.size());
                               assertEquals(context.getInterfaceOrThrow("Direct"), directInterfaces.get(0));
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
      ProcessorTest.process(context ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                         "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getClassOrThrow("java.lang.String"), "java.lang.String"));

                               assertEquals(context.getClassOrThrow("java.lang.String"),
                                            context.withGenerics(context.getClassOrThrow("WithGenericsExample.Inner"), "java.lang.String")
                                                     .getGenericTypes()
                                                     .get(0));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric"),
                                                                   "java.lang.String")
                                                     .getGenericTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                   "java.lang.String",
                                                                   "java.lang.Number")
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
      ProcessorTest.process(context ->
                            {
                               ClassLangModel declared = context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample"),
                                                                                 context.getClassOrThrow("java.lang.String"),
                                                                                 context.getConstants().getUnboundWildcard());
                               ClassLangModel capture = context.interpolateGenerics(declared);
                               ShadowLangModel interpolated = Optional.of((GenericLangModel) capture.getGenericTypes().get(1))
                                                                      .map(GenericLangModel::getExtends)
                                                                      .map(InterfaceLangModel.class::cast)
                                                                      .map(InterfaceLangModel::getGenericTypes)
                                                                      .map(shadows -> shadows.get(0))
                                                                      .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               ClassLangModel independentExample = context.withGenerics(context.getClassOrThrow(
                                                                                       "InterpolateGenericsExample.IndependentGeneric"),
                                                                                           context.getConstants().getUnboundWildcard());
                               ClassLangModel independentCapture = context.interpolateGenerics(independentExample);
                               ShadowLangModel interpolatedIndependent = Optional.of(((GenericLangModel) independentCapture.getGenericTypes().get(0)))
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               ClassLangModel dependentExample = context.withGenerics(context.getClassOrThrow(
                                                                                     "InterpolateGenericsExample.DependentGeneric"),
                                                                                         context.getConstants().getUnboundWildcard(),
                                                                                         context.getClassOrThrow("java.lang.String"));
                               ClassLangModel dependentCapture = context.interpolateGenerics(dependentExample);
                               ShadowLangModel interpolatedDependent = Optional.of(((GenericLangModel) dependentCapture.getGenericTypes().get(0)))
                                     .map(GenericLangModel::getExtends)
                                     .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolatedDependent);
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
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getClassOrThrow("java.lang.Long")
                                                .isSubtypeOf(context.getClassOrThrow("java.lang.Number")));

                               assertTrue(context.getClassOrThrow("java.lang.Long").isSubtypeOf(context.getClassOrThrow("java.lang.Long")));

                               assertFalse(context.getClassOrThrow("java.lang.Number")
                                                 .isSubtypeOf(context.getClassOrThrow("java.lang.Long")));
                            })
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(context ->
                            {
                               //same as isSubtypeOF
                               assertTrue(context.getClassOrThrow("java.lang.Long")
                                                   .isAssignableFrom(context.getClassOrThrow("java.lang.Number")));
                               assertTrue(context.getClassOrThrow("java.lang.Long")
                                                   .isAssignableFrom(context.getClassOrThrow("java.lang.Long")));
                               assertFalse(context.getClassOrThrow("java.lang.Number")
                                                    .isAssignableFrom(context.getClassOrThrow("java.lang.Long")));

                               assertTrue(context.getClassOrThrow("java.lang.Integer")
                                                   .isAssignableFrom(context.getConstants().getPrimitiveInt()));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of(context.getClassOrThrow("java.lang.Number")),
                                            context.getClassOrThrow("ClassParent")
                                                  .getDirectSuperTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.Number"),
                                                    context.getInterfaceOrThrow("java.lang.Comparable"),
                                                    context.getInterfaceOrThrow("java.util.function.Consumer")),
                                            context.getClassOrThrow("ClassMixedParent")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(Set.of(),
                                            context.getClassOrThrow("java.lang.Object").getSuperTypes());

                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getClassOrThrow("java.lang.Number"),
                                                   context.getInterfaceOrThrow("java.io.Serializable")),
                                            context.getClassOrThrow("ClassParent").getSuperTypes());

                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getClassOrThrow("java.lang.Number"),
                                                   context.getInterfaceOrThrow("java.io.Serializable"),
                                                   context.getInterfaceOrThrow("java.lang.Comparable"),
                                                   context.getInterfaceOrThrow("java.util.function.Consumer")),
                                            context.getClassOrThrow("ClassMixedParent").getSuperTypes());
                            })
                   .withCodeToCompile("ClassNoParent.java", "class ClassNoParent {}")
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }
}
