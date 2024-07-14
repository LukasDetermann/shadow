package io.determann.shadow.article.consistency_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Arrays;

class ReceiverConsistencyTest
{
   //@formatter:off
//tag::method[]
@Test
void methodReceiver()
{
   Executable method = Arrays.stream(MethodExample.class.getMethods())
                             .filter(method1 -> method1.getName().equals("foo"))
                             .findFirst()
                             .get();

   Parameter[] methodParameters = method.getParameters();
   Assertions.assertEquals(0, methodParameters.length);
}
//end::method[]

//tag::constructor[]
@Test
void constructorReceiver()
{
   Executable constructor = ConstructorExample.Inner.class.getConstructors()[0];

   Parameter[] constructorParameters = constructor.getParameters();
   Assertions.assertEquals(1, constructorParameters.length);
   Assertions.assertEquals(ConstructorExample.class, constructorParameters[0].getType());
}
//end::constructor[]
//@formatter:on
}
