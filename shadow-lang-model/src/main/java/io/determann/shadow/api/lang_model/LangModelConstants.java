package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.lang_model.shadow.type.PrimitiveLangModel;
import io.determann.shadow.api.lang_model.shadow.type.WildcardLangModel;
import io.determann.shadow.api.shadow.type.Null;
import io.determann.shadow.api.shadow.type.Void;

public interface LangModelConstants
{
   WildcardLangModel getUnboundWildcard();

   Null getNull();

   Void getVoid();

   PrimitiveLangModel getPrimitiveBoolean();

   PrimitiveLangModel getPrimitiveByte();

   PrimitiveLangModel getPrimitiveShort();

   PrimitiveLangModel getPrimitiveInt();

   PrimitiveLangModel getPrimitiveLong();

   PrimitiveLangModel getPrimitiveChar();

   PrimitiveLangModel getPrimitiveFloat();

   PrimitiveLangModel getPrimitiveDouble();
}
