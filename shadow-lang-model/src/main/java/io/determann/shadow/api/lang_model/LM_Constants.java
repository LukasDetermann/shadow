package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.type.C_Null;
import io.determann.shadow.api.shadow.type.C_Void;

public interface LM_Constants
{
   LM_Wildcard getUnboundWildcard();

   C_Null getNull();

   C_Void getVoid();

   LM_Primitive getPrimitiveBoolean();

   LM_Primitive getPrimitiveByte();

   LM_Primitive getPrimitiveShort();

   LM_Primitive getPrimitiveInt();

   LM_Primitive getPrimitiveLong();

   LM_Primitive getPrimitiveChar();

   LM_Primitive getPrimitiveFloat();

   LM_Primitive getPrimitiveDouble();
}
