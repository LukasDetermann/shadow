package io.determann.shadow.impl;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.ShadowConstants;
import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.Wildcard;

import javax.lang.model.type.TypeKind;

import static io.determann.shadow.api.MirrorAdapter.getShadow;

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
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getWildcardType(null, null));
   }

   @Override
   public Null getNull()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getNullType());
   }

   @Override
   public Void getVoid()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getNoType(TypeKind.VOID));
   }

   @Override
   public Primitive getPrimitiveBoolean()
   {
      return getShadow(shadowApi,
                       MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.BOOLEAN));
   }

   @Override
   public Primitive getPrimitiveByte()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.BYTE));
   }

   @Override
   public Primitive getPrimitiveShort()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.SHORT));
   }

   @Override
   public Primitive getPrimitiveInt()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.INT));
   }

   @Override
   public Primitive getPrimitiveLong()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.LONG));
   }

   @Override
   public Primitive getPrimitiveChar()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.CHAR));
   }

   @Override
   public Primitive getPrimitiveFloat()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.FLOAT));
   }

   @Override
   public Primitive getPrimitiveDouble()
   {
      return getShadow(shadowApi, MirrorAdapter.getProcessingEnv(shadowApi).getTypeUtils().getPrimitiveType(TypeKind.DOUBLE));
   }
}
