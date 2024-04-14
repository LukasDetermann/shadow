package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.LangModelAdapter.generalize;

public class GenericImpl extends ShadowImpl<TypeVariable> implements Generic
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
   public Shadow getExtends()
   {
      return LangModelAdapter.generalize(getApi(), getMirror().getUpperBound());
   }

   @Override
   public Optional<Shadow> getSuper()
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
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return generalize(getApi(), LangModelAdapter.getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return generalize(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getExtends(),
                          getSuper());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Generic otherGeneric))
      {
         return false;
      }
      return Objects.equals(getName(), otherGeneric.getName()) &&
             Objects.equals(getExtends(), otherGeneric.getExtends()) &&
             Objects.equals(getSuper(), otherGeneric.getSuper());
   }
}
