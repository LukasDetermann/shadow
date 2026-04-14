package com.derivandi.api.processor;

import com.derivandi.api.Ap;

public interface Constants
{
   Ap.Wildcard getUnboundWildcard();

   Ap.Null getNull();

   Ap.Void getVoid();

   Ap.boolean_ getPrimitiveBoolean();

   Ap.byte_ getPrimitiveByte();

   Ap.short_ getPrimitiveShort();

   Ap.int_ getPrimitiveInt();

   Ap.long_ getPrimitiveLong();

   Ap.char_ getPrimitiveChar();

   Ap.float_ getPrimitiveFloat();

   Ap.double_ getPrimitiveDouble();
}
