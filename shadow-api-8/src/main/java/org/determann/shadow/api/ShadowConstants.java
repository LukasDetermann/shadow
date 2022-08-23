package org.determann.shadow.api;

import org.determann.shadow.api.shadow.Null;
import org.determann.shadow.api.shadow.Primitive;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.Wildcard;

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
