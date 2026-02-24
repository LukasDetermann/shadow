package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Context;
import io.determann.shadow.internal.annotation_processing.ApiHolder;

import javax.lang.model.type.TypeMirror;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public abstract class TypeImpl<MIRROR extends TypeMirror> implements ApiHolder
{
   private final Context context;

   private final MIRROR typeMirror;

   protected TypeImpl(Context context, MIRROR typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   public boolean isSameType(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isSameType(getMirror(), adapt(type).toTypeMirror());
   }

   public MIRROR getMirror()
   {
      return typeMirror;
   }

   @Override
   public Context getApi()
   {
      return context;
   }
}
