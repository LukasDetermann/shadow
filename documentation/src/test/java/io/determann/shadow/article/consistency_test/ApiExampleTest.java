package io.determann.shadow.article.consistency_test;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.Response;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.*;

public class ApiExampleTest
{

//tag::request[]
@Test
void request()
{
   //adapter for the reflection api
   Class systemClass = ReflectionAdapter.generalize(System.class);
   //request the field "out" for the class java.lang.System
   Response<Field> out = Provider.request(systemClass,
                                          Operations.DECLARED_GET_FIELD,
                                          "out");

   switch (out)
   {
      //the implementation may not support this operation
      //e.g. it's impossible to access fields with reflection
      case Response.Unsupported<Field> unsupported -> Assertions.fail();
      //the implementation may support this operation, but there is no
      //result for this instance
      //e.g. the class java.lang.System does not have a field called "out"
      case Response.Empty<Field> empty -> Assertions.fail();
      //accessing fields via reflection is possible and java.lang.System
      //does have a field called "out" therefore a result is expected
      case Response.Result<Field> result -> assertNotNull(result.value());
   }
}
//end::request[]

//tag::requestOrEmpty[]
@Test
void requestOrEmpty()
{
   //adapter for the reflection api
   Class systemClass = ReflectionAdapter.generalize(System.class);
   //request the field "out" for the class java.lang.System.
   //If its unsupported an Empty Optional is returned
   Optional<Field> out = Provider.requestOrEmpty(systemClass,
                                                 Operations.DECLARED_GET_FIELD,
                                                 "out");

   assertTrue(out.isPresent());
}
//end::requestOrEmpty[]

//tag::requestOrThrow[]
@Test
void requestOrThrow()
{
   //adapter for the reflection api
   Class systemClass = ReflectionAdapter.generalize(System.class);
   //request the field "out" for the class java.lang.System.
   //If its unsupported an Exception is thrown
   Field out = Provider.requestOrThrow(systemClass,
                                       Operations.DECLARED_GET_FIELD,
                                       "out");

   assertNotNull(out);
}
//end::requestOrThrow[]

//tag::consistency[]
@Test
void classDeclarationRendering()
{
   String code =
         "public class DependentGenerics<A extends Comparable<B>, B extends Comparable<A>> {}";

   String expectedCode =
         "public class DependentGenerics<A extends Comparable<B>, B extends Comparable<A>> {}\n";

   ConsistencyTest.<Class>compileTime(c -> c.getClassOrThrow("DependentGenerics"))
         .runtime(cl -> ReflectionAdapter.generalize(cl.apply("DependentGenerics")))
         .withCode("DependentGenerics.java", code)
         .test(aClass -> assertEquals(expectedCode, render(DEFAULT, aClass).declaration()));
}
//end::consistency[]
//@formatter:on
}
