package io.determann.shadow.api;

import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.Wildcard;

public interface ShadowConstants
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
