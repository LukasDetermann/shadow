package io.determann.shadow.impl.annotation_processing;

import io.determann.shadow.api.ShadowConstants;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.MirrorAdapter;
import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.Wildcard;

import javax.lang.model.type.TypeKind;

import static io.determann.shadow.api.annotation_processing.MirrorAdapter.getShadow;

public class ShadowConstantsImpl implements ShadowConstants
{
   private final AnnotationProcessingContext annotationProcessingContext;

   ShadowConstantsImpl(AnnotationProcessingContext annotationProcessingContext)
   {
      this.annotationProcessingContext = annotationProcessingContext;
   }

   @Override
   public Wildcard getUnboundWildcard()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getWildcardType(null, null));
   }

   @Override
   public Null getNull()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getNullType());
   }

   @Override
   public Void getVoid()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getNoType(TypeKind.VOID));
   }

   @Override
   public Primitive getPrimitiveBoolean()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.BOOLEAN));
   }

   @Override
   public Primitive getPrimitiveByte()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.BYTE));
   }

   @Override
   public Primitive getPrimitiveShort()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.SHORT));
   }

   @Override
   public Primitive getPrimitiveInt()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.INT));
   }

   @Override
   public Primitive getPrimitiveLong()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.LONG));
   }

   @Override
   public Primitive getPrimitiveChar()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.CHAR));
   }

   @Override
   public Primitive getPrimitiveFloat()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.FLOAT));
   }

   @Override
   public Primitive getPrimitiveDouble()
   {
      return getShadow(annotationProcessingContext, MirrorAdapter.getProcessingEnv(annotationProcessingContext).getTypeUtils().getPrimitiveType(TypeKind.DOUBLE));
   }
}
