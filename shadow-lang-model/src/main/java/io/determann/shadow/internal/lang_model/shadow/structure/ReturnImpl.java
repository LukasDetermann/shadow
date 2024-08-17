package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.AnnotationUsageLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.ReturnLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.structure.Return;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.shadow.Operations.RETURN_GET_TYPE;
import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

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
   public List<AnnotationUsageLangModel> getAnnotationUsages()
   {
      return getDirectAnnotationUsages();
   }

   @Override
   public List<AnnotationUsageLangModel> getDirectAnnotationUsages()
   {
      return LangModelAdapter.generalize(context, getTypeMirror().getAnnotationMirrors());
   }

   @Override
   public ShadowLangModel getType()
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
      return Provider.requestOrEmpty(otherReturn, RETURN_GET_TYPE).map(shadow -> Objects.equals(shadow, getType())).orElse(false);
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
