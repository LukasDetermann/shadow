package io.determann.shadow.api.lang_model;

public interface Constants
{
   LM.Wildcard getUnboundWildcard();

   LM.Null getNull();

   LM.Void getVoid();

   LM.boolean_ getPrimitiveBoolean();

   LM.byte_ getPrimitiveByte();

   LM.short_ getPrimitiveShort();

   LM.int_ getPrimitiveInt();

   LM.long_ getPrimitiveLong();

   LM.char_ getPrimitiveChar();

   LM.float_ getPrimitiveFloat();

   LM.double_ getPrimitiveDouble();
}
