package com.derivandi.api.processor;

import com.derivandi.api.D;

public interface Constants
{
   D.Wildcard getUnboundWildcard();

   D.Null getNull();

   D.Void getVoid();

   D.boolean_ getPrimitiveBoolean();

   D.byte_ getPrimitiveByte();

   D.short_ getPrimitiveShort();

   D.int_ getPrimitiveInt();

   D.long_ getPrimitiveLong();

   D.char_ getPrimitiveChar();

   D.float_ getPrimitiveFloat();

   D.double_ getPrimitiveDouble();
}
