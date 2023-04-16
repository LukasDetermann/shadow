package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstructorTest extends ExecutableTest<Constructor>
{
   ConstructorTest()
   {
      super(shadowApi -> shadowApi.getClassOrThrow("DefaultConstructorExample")
                                  .getConstructors()
                                  .get(0));
   }

   @Test
   void testGetParameters()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(0,
                                              shadowApi.getClassOrThrow("DefaultConstructorExample")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getParameters()
                                                       .size());

                                 List<Constructor> constructors = shadowApi.getClassOrThrow("ConstructorExample")
                                                                           .getConstructors();
                                 assertEquals(3, constructors.size());
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.Long"),
                                              constructors.get(0)
                                                          .getParameters()
                                                          .get(0)
                                                          .getType());
                              })
                     .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                     .withCodeToCompile("ConstructorExample.java", "                           import java.io.IOException;\n" +
                                                                   "\n" +
                                                                   "                           public class ConstructorExample {\n" +
                                                                   "                              public ConstructorExample(Long id) {}\n" +
                                                                   "                              public ConstructorExample(String name) throws IOException {}\n" +
                                                                   "                              public ConstructorExample(String... names) {}\n" +
                                                                   "                           }")
                     .compile();
   }

   @Test
   @Override
   void testGetReturnType()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getConstants().getVoid(),
                                                        shadowApi.getClassOrThrow("ConstructorExample")
                                                                 .getConstructors()
                                                                 .get(0)
                                                                 .getReturnType()))
                     .withCodeToCompile("ConstructorExample.java", "                           import java.io.IOException;\n" +
                                                                   "\n" +
                                                                   "                           public class ConstructorExample {\n" +
                                                                   "                              public ConstructorExample(Long id) {}\n" +
                                                                   "                              public ConstructorExample(String name) throws IOException {}\n" +
                                                                   "                              public ConstructorExample(String... names) {}\n" +
                                                                   "                           }")
                     .compile();
   }

   @Test
   @Override
   void testGetParameterTypes()
   {
      CompilationTest.process(shadowApi -> assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Long")),
                                                        shadowApi.getClassOrThrow("ConstructorExample")
                                                                 .getConstructors()
                                                                 .get(0)
                                                                 .getParameterTypes()))
                     .withCodeToCompile("ConstructorExample.java", "                           import java.io.IOException;\n" +
                                                                   "\n" +
                                                                   "                           public class ConstructorExample {\n" +
                                                                   "                              public ConstructorExample(Long id) {}\n" +
                                                                   "                              public ConstructorExample(String name) throws IOException {}\n" +
                                                                   "                              public ConstructorExample(String... names) {}\n" +
                                                                   "                           }")
                     .compile();
   }

   @Test
   @Override
   void testGetThrows()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of(),
                                              shadowApi.getClassOrThrow("ConstructorExample")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getThrows());

                                 assertEquals(List.of(shadowApi.getClassOrThrow("java.io.IOException")),
                                              shadowApi.getClassOrThrow("ConstructorExample")
                                                       .getConstructors()
                                                       .get(1)
                                                       .getThrows());
                              })
                     .withCodeToCompile("ConstructorExample.java", "                           import java.io.IOException;\n" +
                                                                   "\n" +
                                                                   "                           public class ConstructorExample {\n" +
                                                                   "                              public ConstructorExample(Long id) {}\n" +
                                                                   "                              public ConstructorExample(String name) throws IOException {}\n" +
                                                                   "                              public ConstructorExample(String... names) {}\n" +
                                                                   "                           }")
                     .compile();
   }

   @Test
   @Override
   void testIsVarArgs()
   {
      CompilationTest.process(shadowApi -> assertTrue(shadowApi.getClassOrThrow(
                                                                     "ConstructorExample")
                                                               .getConstructors()
                                                               .get(2)
                                                               .isVarArgs()))
                     .withCodeToCompile("ConstructorExample.java", "                           import java.io.IOException;\n" +
                                                                   "\n" +
                                                                   "                           public class ConstructorExample {\n" +
                                                                   "                              public ConstructorExample(Long id) {}\n" +
                                                                   "                              public ConstructorExample(String name) throws IOException {}\n" +
                                                                   "                              public ConstructorExample(String... names) {}\n" +
                                                                   "                           }")
                     .compile();
   }

   @Test
   @Override
   void testGetSurrounding()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Class aClass = shadowApi.getClassOrThrow("ConstructorExample");
                                 assertEquals(aClass, aClass.getConstructors().get(0).getSurrounding());
                              })
                     .withCodeToCompile("ConstructorExample.java", "                           import java.io.IOException;\n" +
                                                                   "\n" +
                                                                   "                           public class ConstructorExample {\n" +
                                                                   "                              public ConstructorExample(Long id) {}\n" +
                                                                   "                              public ConstructorExample(String name) throws IOException {}\n" +
                                                                   "                              public ConstructorExample(String... names) {}\n" +
                                                                   "                           }")
                     .compile();
   }

   @Test
   @Override
   void testGetPackage()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackagesOrThrow("io.determann.shadow.example.processed.test.constructor").get(0),
                                                        shadowApi.getClassOrThrow(
                                                                       "io.determann.shadow.example.processed.test.constructor.ConstructorExample")
                                                                 .getPackage()))
                     .withCodeToCompile("ConstructorExample.java",
                                        "                           package io.determann.shadow.example.processed.test.constructor;\n" +
                                        "                                                      \n" +
                                        "                           import java.io.IOException;\n" +
                                        "\n" +
                                        "                           public class ConstructorExample {\n" +
                                        "                              public ConstructorExample(Long id) {}\n" +
                                        "                              public ConstructorExample(String name) throws IOException {}\n" +
                                        "                              public ConstructorExample(String... names) {}\n" +
                                        "                           }")
                     .compile();
   }

   @Test
   void testGetReceiverType()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(Optional.empty(),
                                              shadowApi.getClassOrThrow("DefaultConstructorExample")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getReceiverType());

                                 assertEquals(shadowApi.getClassOrThrow("ReceiverExample"),
                                              shadowApi.getClassOrThrow("ReceiverExample.Inner")
                                                       .getConstructors()
                                                       .get(0)
                                                       .getReceiverType()
                                                       .orElseThrow());
                              })
                     .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                     .withCodeToCompile("ReceiverExample.java", "                           public class ReceiverExample {\n" +
                                                                "                              public class Inner {\n" +
                                                                "                                 public Inner(ReceiverExample ReceiverExample.this) {}\n" +
                                                                "                              }\n" +
                                                                "                           }")
                     .compile();
   }
}
