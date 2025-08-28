package io.determann.shadow.api.annotation_processing;

public interface Constants
{
   AP.Wildcard getUnboundWildcard();

   AP.Null getNull();

   AP.Void getVoid();

   AP.boolean_ getPrimitiveBoolean();

   AP.byte_ getPrimitiveByte();

   AP.short_ getPrimitiveShort();

   AP.int_ getPrimitiveInt();

   AP.long_ getPrimitiveLong();

   AP.char_ getPrimitiveChar();

   AP.float_ getPrimitiveFloat();

   AP.double_ getPrimitiveDouble();
}
