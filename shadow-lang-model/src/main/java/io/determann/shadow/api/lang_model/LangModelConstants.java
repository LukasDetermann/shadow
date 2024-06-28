package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.shadow.type.Null;
import io.determann.shadow.api.shadow.type.Primitive;
import io.determann.shadow.api.shadow.type.Void;
import io.determann.shadow.api.shadow.type.Wildcard;

public interface LangModelConstants
{
   Wildcard getUnboundWildcard();

   Null getNull();

   Void getVoid();

   Primitive getPrimitiveBoolean();

   Primitive getPrimitiveByte();

   Primitive getPrimitiveShort();

   Primitive getPrimitiveInt();

   Primitive getPrimitiveLong();

   Primitive getPrimitiveChar();

   Primitive getPrimitiveFloat();

   Primitive getPrimitiveDouble();
}
