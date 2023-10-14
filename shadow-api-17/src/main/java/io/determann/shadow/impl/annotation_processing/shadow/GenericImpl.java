package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVariable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GenericImpl extends ShadowImpl<TypeVariable> implements Generic
{
   private final TypeParameterElement typeParameterElement;

   public GenericImpl(ShadowApi shadowApi, TypeParameterElement typeParameterElement)
   {
      super(shadowApi, ((TypeVariable) typeParameterElement.asType()));
      this.typeParameterElement = typeParameterElement;
   }

   public GenericImpl(ShadowApi shadowApi, TypeVariable typeMirror)
   {
      super(shadowApi, typeMirror);
      this.typeParameterElement = (TypeParameterElement) MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().asElement(typeMirror);
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return MirrorAdapter.getModifiers(getElement());
   }

   @Override
   public Shadow getExtends()
   {
      return MirrorAdapter.getShadow(getApi(), getMirror().getUpperBound());
   }

   @Override
   public Shadow getSuper()
   {
      return MirrorAdapter.getShadow(getApi(), getMirror().getLowerBound());
   }

   @Override
   public Shadow getEnclosing()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().getGenericElement());
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.GENERIC;
   }

   @Override
   public Package getPackage()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getPackageOf(getElement()));
   }

   @Override
   public Shadow erasure()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().erasure(getMirror()));
   }

   public TypeParameterElement getElement()
   {
      return typeParameterElement;
   }

   @Override
   public Module getModule()
   {
      return MirrorAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getSimpleName()
   {
      return MirrorAdapter.getSimpleName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return MirrorAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return MirrorAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return MirrorAdapter.getDirectAnnotationUsages(getApi(), getElement());
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getSimpleName(),
                          getExtends(),
                          getSuper(),
                          getEnclosing());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      GenericImpl otherGeneric = (GenericImpl) other;
      return Objects.equals(getSimpleName(), otherGeneric.getSimpleName()) &&
             Objects.equals(getExtends(), otherGeneric.getExtends()) &&
             Objects.equals(getSuper(), otherGeneric.getSuper()) &&
             Objects.equals(getEnclosing(), otherGeneric.getEnclosing());
   }
}
