package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.AnnotationUsageLangModel;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;

public class GenericImpl extends ShadowImpl<TypeVariable> implements GenericLangModel
{
   private final TypeParameterElement typeParameterElement;

   public GenericImpl(LangModelContext context, TypeParameterElement typeParameterElement)
   {
      super(context, ((TypeVariable) typeParameterElement.asType()));
      this.typeParameterElement = typeParameterElement;
   }

   public GenericImpl(LangModelContext context, TypeVariable typeMirror)
   {
      super(context, typeMirror);
      this.typeParameterElement = (TypeParameterElement) LangModelAdapter.getTypes(getApi()).asElement(typeMirror);
   }

   @Override
   public ShadowLangModel getExtends()
   {
      return LangModelAdapter.generalize(getApi(), getMirror().getUpperBound());
   }

   @Override
   public Optional<ShadowLangModel> getSuper()
   {
      TypeMirror lowerBound = getMirror().getLowerBound();
      if (lowerBound == null || lowerBound.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(LangModelAdapter.generalize(getApi(), lowerBound));
   }

   @Override
   public Object getEnclosing()
   {
      return LangModelAdapter.generalize(getApi(), getElement().getGenericElement());
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.GENERIC;
   }

   public TypeParameterElement getElement()
   {
      return typeParameterElement;
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public List<AnnotationUsageLangModel> getAnnotationUsages()
   {
      return generalize(getApi(), LangModelAdapter.getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsageLangModel> getDirectAnnotationUsages()
   {
      return generalize(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public String toString()
   {
      return GenericSupport.toString(this);
   }

   @Override
   public int hashCode()
   {
      return GenericSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return GenericSupport.equals(this, other);
   }
}
