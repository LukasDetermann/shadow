package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.ReturnLangModel;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Return;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.RETURN_GET_TYPE;
import static io.determann.shadow.meta_meta.Provider.request;

public class ReturnImpl implements ReturnLangModel
{
   private final LangModelContext context;
   private final TypeMirror typeMirror;

   public ReturnImpl(LangModelContext context, TypeMirror typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return getDirectAnnotationUsages();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return LangModelAdapter.generalize(context, getTypeMirror().getAnnotationMirrors());
   }

   @Override
   public Shadow getType()
   {
      return LangModelAdapter.generalize(context, getTypeMirror());
   }

   public TypeMirror getTypeMirror()
   {
      return typeMirror;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Return otherReturn))
      {
         return false;
      }
      return request(otherReturn, RETURN_GET_TYPE).map(shadow -> Objects.equals(shadow, getType())).orElse(false);
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
