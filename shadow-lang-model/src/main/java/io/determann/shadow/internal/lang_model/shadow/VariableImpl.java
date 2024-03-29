package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.Documented;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class VariableImpl<SURROUNDING extends Shadow> extends ShadowImpl<TypeMirror>
      implements Variable<SURROUNDING>,
                 Documented
{
   private final VariableElement variableElement;

   protected VariableImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement.asType());
      this.variableElement = variableElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return LangModelAdapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(LangModelAdapter.getType(shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isAssignable(LangModelAdapter.getType(shadow), getMirror());
   }

   @Override
   public Shadow getType()
   {
      return LangModelAdapter.getShadow(getApi(), getElement().asType());
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter
                     .getShadow(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   @Override
   public TypeKind getTypeKind()
   {
      return switch (getElement().getKind())
      {
         case ENUM_CONSTANT -> TypeKind.ENUM_CONSTANT;
         case FIELD -> TypeKind.FIELD;
         case PARAMETER -> TypeKind.PARAMETER;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public SURROUNDING getSurrounding()
   {
      return LangModelAdapter.getShadow(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public Module getModule()
   {
      return LangModelAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getName()
   {
      return LangModelAdapter.getName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return LangModelAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return LangModelAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return LangModelAdapter.getDirectAnnotationUsages(getApi(), getElement());
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
                          getType(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Variable<?> otherVariable))
      {
         return false;
      }
      return Objects.equals(getName(), otherVariable.getName()) &&
             Objects.equals(getType(), otherVariable.getType()) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers());
   }
}
