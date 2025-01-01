package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;

class EnumConstantTest
{
   @Test
   void getSurrounding()
   {
      TckTest.test(implementation ->
                   {
                      C_Enum retentionPolicy = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
                      C_EnumConstant source = requestOrThrow(retentionPolicy, ENUM_GET_ENUM_CONSTANT, "SOURCE");
                      Assertions.assertEquals(retentionPolicy, requestOrThrow(source, ENUM_CONSTANT_GET_SURROUNDING));
                   });
   }
}
