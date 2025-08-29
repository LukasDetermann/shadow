package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.internal.annotation_processing.ApiHolder;

import javax.lang.model.type.TypeMirror;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public abstract class TypeImpl<MIRROR extends TypeMirror> implements ApiHolder
{
   private final Ap.Context context;

   private final MIRROR typeMirror;

   protected TypeImpl(Ap.Context context, MIRROR typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   public boolean representsSameType(C.Type type)
   {
      return adapt(getApi()).toTypes().isSameType(getMirror(), adapt((Ap.Type) type).toTypeMirror());
   }

   public MIRROR getMirror()
   {
      return typeMirror;
   }

   @Override
   public Ap.Context getApi()
   {
      return context;
   }

   @Override
   public String toString()
   {
      return getMirror().toString();
   }

   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }
}
