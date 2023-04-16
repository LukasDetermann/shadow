package io.determann.shadow.impl;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.ShadowConstants;
import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.Wildcard;

import javax.lang.model.type.TypeKind;

public class ShadowConstantsImpl implements ShadowConstants
{
   private final ShadowApi shadowApi;

   ShadowConstantsImpl(ShadowApi shadowApi)
   {
      this.shadowApi = shadowApi;
   }

   @Override
   public Wildcard getUnboundWildcard()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getWildcardType(null, null));
   }


   @Override
   public Null getNull()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getNullType());
   }

   @Override
   public Void getVoid()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getNoType(TypeKind.VOID));
   }

   @Override
   public Primitive getPrimitiveBoolean()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.BOOLEAN));
   }

   @Override
   public Primitive getPrimitiveByte()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.BYTE));
   }

   @Override
   public Primitive getPrimitiveShort()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.SHORT));
   }

   @Override
   public Primitive getPrimitiveInt()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.INT));
   }

   @Override
   public Primitive getPrimitiveLong()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.LONG));
   }

   @Override
   public Primitive getPrimitiveChar()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.CHAR));
   }

   @Override
   public Primitive getPrimitiveFloat()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.FLOAT));
   }

   @Override
   public Primitive getPrimitiveDouble()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getPrimitiveType(TypeKind.DOUBLE));
   }
}
