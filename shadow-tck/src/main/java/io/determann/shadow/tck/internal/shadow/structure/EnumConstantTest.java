package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

class EnumConstantTest
{
   @Test
   void getSurrounding()
   {
      TckTest.test(implementation ->
                   {
                      C.Enum retentionPolicy = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
                      C.EnumConstant source = requestOrThrow(retentionPolicy, ENUM_GET_ENUM_CONSTANT, "SOURCE");
                      Assertions.assertEquals(retentionPolicy, requestOrThrow(source, ENUM_CONSTANT_GET_SURROUNDING));
                   });
   }
}
