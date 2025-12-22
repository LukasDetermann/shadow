package io.determann.shadow.article.consistency_test;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Operations;
import io.determann.shadow.api.query.Provider;
import io.determann.shadow.api.query.Response;
import io.determann.shadow.api.reflection.Adapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiExampleTest
{

//tag::request[]
@Test
void request()
{
   //adapter for the reflection api
   C.Class systemClass = Adapter.generalize(System.class);
   //request the field "out" for the class java.lang.System
   Response<C.Field> out = Provider.request(systemClass,
                                            Operations.DECLARED_GET_FIELD,
                                            "out");

   switch (out)
   {
      //the implementation may not support this operation
      //e.g. it's impossible to access fields with reflection
      case Response.Unsupported<C.Field> unsupported -> Assertions.fail();
      //the implementation may support this operation, but there is no
      //result for this instance
      //e.g. the class java.lang.System does not have a field called "out"
      case Response.Empty<C.Field> empty -> Assertions.fail();
      //accessing fields via reflection is possible and java.lang.System
      //does have a field called "out" therefore a result is expected
      case Response.Result<C.Field> result -> assertNotNull(result.value());
   }
}
//end::request[]

//tag::requestOrEmpty[]
@Test
void requestOrEmpty()
{
   //adapter for the reflection api
   C.Class systemClass = Adapter.generalize(System.class);
   //request the field "out" for the class java.lang.System.
   //If its unsupported an Empty Optional is returned
   Optional<C.Field> out = Provider.requestOrEmpty(systemClass,
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
   C.Class systemClass = Adapter.generalize(System.class);
   //request the field "out" for the class java.lang.System.
   //If its unsupported an Exception is thrown
   C.Field out = Provider.requestOrThrow(systemClass,
                                         Operations.DECLARED_GET_FIELD,
                                         "out");

   assertNotNull(out);
}
//end::requestOrThrow[]
}
